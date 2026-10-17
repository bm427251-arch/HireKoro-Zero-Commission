/**
 * HireKoro Cloud Functions
 * Zero Commission Model - Razorpay Webhook & Provider Automated Checks
 */

const functions = require("firebase-functions");
const admin = require("firebase-admin");
const crypto = require("crypto");

admin.initializeApp();
const db = admin.firestore();

// 1. Verify Razorpay Payment Webhook
// Webhook endpoint to configure in Razorpay Dashboard: https://<project-id>.cloudfunctions.net/verifyRazorpayPayment
exports.verifyRazorpayPayment = functions.https.onRequest(async (req, res) => {
  try {
    const secret = process.env.RAZORPAY_WEBHOOK_SECRET || "hirekoro_webhook_secret_key";
    const signature = req.headers["x-razorpay-signature"];

    const shasum = crypto.createHmac("sha256", secret);
    shasum.update(JSON.stringify(req.body));
    const digest = shasum.digest("hex");

    if (digest !== signature && process.env.NODE_ENV === "production") {
      return res.status(400).json({ error: "Invalid Razorpay Webhook Signature" });
    }

    const event = req.body.event;
    const payload = req.body.payload;

    if (event === "payment.captured") {
      const payment = payload.payment.entity;
      const amountInRupees = payment.amount / 100;
      const notes = payment.notes || {};
      const providerId = notes.providerId;
      const slabNumber = parseInt(notes.slabNumber || "1", 10);
      const paymentType = notes.type || "subscription"; // "subscription" or "wallet_add"

      if (paymentType === "subscription" && providerId) {
        // Activate subscription in Firestore: providers/{id}/subscription
        const expiryDate = new Date();
        expiryDate.setDate(expiryDate.getDate() + 30);

        await db.collection("providers").doc(providerId).set(
          {
            subscription: {
              slab: slabNumber,
              amountPaid: amountInRupees,
              paymentId: payment.id,
              status: "active",
              activatedAt: admin.firestore.FieldValue.serverTimestamp(),
              expiryDate: admin.firestore.Timestamp.fromDate(expiryDate),
            },
            isSubscriptionActive: true,
          },
          { merge: true }
        );

        // Record in transactions ledger
        await db.collection("transactions").add({
          providerId,
          type: "SUBSCRIPTION_PAYMENT",
          slab: slabNumber,
          amount: amountInRupees,
          razorpayPaymentId: payment.id,
          createdAt: admin.firestore.FieldValue.serverTimestamp(),
        });
      } else if (paymentType === "wallet_add") {
        const userId = notes.userId || providerId;
        if (userId) {
          const walletRef = db.collection("wallets").doc(userId);
          await db.runTransaction(async (t) => {
            const doc = await t.get(walletRef);
            const currentBal = doc.exists ? (doc.data().balance || 0) : 0;
            t.set(
              walletRef,
              {
                balance: currentBal + amountInRupees,
                updatedAt: admin.firestore.FieldValue.serverTimestamp(),
              },
              { merge: true }
            );
          });
        }
      }
    }

    return res.status(200).json({ status: "success" });
  } catch (error) {
    console.error("Webhook error:", error);
    return res.status(500).json({ error: error.message });
  }
});

// 2. Daily Cron: onSubscriptionExpiry
exports.onSubscriptionExpiry = functions.pubsub
  .schedule("every 24 hours")
  .onRun(async (context) => {
    const now = admin.firestore.Timestamp.now();
    const expiredSnap = await db
      .collection("providers")
      .where("isSubscriptionActive", "==", true)
      .where("subscription.expiryDate", "<=", now)
      .get();

    const batch = db.batch();
    expiredSnap.docs.forEach((doc) => {
      batch.update(doc.ref, {
        "subscription.status": "expired",
        isSubscriptionActive: false,
      });
    });

    await batch.commit();
    console.log(`Deactivated ${expiredSnap.size} expired provider subscriptions.`);
    return null;
  });

// 3. onRatingCheck: Quality Audit
// If provider average rating falls below 3.5, auto-assign Under Review status
exports.onRatingCheck = functions.firestore
  .document("ratings/{ratingId}")
  .onCreate(async (snap, context) => {
    const newRating = snap.data();
    const providerId = newRating.providerId;
    if (!providerId) return null;

    const allRatingsSnap = await db
      .collection("ratings")
      .where("providerId", "==", providerId)
      .get();

    let total = 0;
    allRatingsSnap.docs.forEach((d) => {
      total += d.data().rating || 0;
    });

    const average = total / allRatingsSnap.size;
    const isUnderReview = average < 3.5;

    await db.collection("providers").doc(providerId).update({
      rating: parseFloat(average.toFixed(1)),
      reviewCount: allRatingsSnap.size,
      isUnderReview: isUnderReview,
    });

    console.log(`Updated provider ${providerId} rating to ${average} (Under Review: ${isUnderReview})`);
    return null;
  });

// 4. onWalletCredit: Referral Milestone & Credit Handler
exports.onWalletCredit = functions.firestore
  .document("referrals/{referralId}")
  .onCreate(async (snap, context) => {
    const refData = snap.data();
    const referrerId = refData.referrerId;
    const role = refData.newPartyRole || "user";

    // User gets 50, Provider gets 100
    const creditAmount = role === "provider" ? 100 : 50;
    const walletRef = db.collection("wallets").doc(referrerId);

    await db.runTransaction(async (t) => {
      const doc = await t.get(walletRef);
      const balance = doc.exists ? (doc.data().balance || 0) : 0;
      const refCount = doc.exists ? (doc.data().referralCount || 0) + 1 : 1;

      // Check 5 referrals milestone -> Extra 500
      let bonus = 0;
      if (refCount === 5) {
        bonus = 500;
      }

      t.set(
        walletRef,
        {
          balance: balance + creditAmount + bonus,
          referralCount: refCount,
          lastCredit: creditAmount + bonus,
          updatedAt: admin.firestore.FieldValue.serverTimestamp(),
        },
        { merge: true }
      );
    });

    return null;
  });

import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';

class SubscriptionScreen extends StatelessWidget {
  const SubscriptionScreen({super.key});

  void _paySlab(BuildContext context, int slabNum, double price) async {
    final paise = (price * 100).toInt();
    final url = Uri.parse('https://razorpay.me/@bharatmitrainfotech?amount=$paise');
    if (await canLaunchUrl(url)) {
      await launchUrl(url, mode: LaunchMode.externalApplication);
      if (context.mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Payment link opened! Webhook will activate Slab $slabNum on capture.')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return ListView(
      padding: const EdgeInsets.all(16),
      children: [
        const Text('Zero Commission Subscription Slabs', style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold, color: Color(0xFF2B2B2B))),
        const Text('No commission on any booking. Choose your category slab.', style: TextStyle(fontSize: 12, color: Colors.black45)),
        const SizedBox(height: 16),
        _buildSlabCard(
          context,
          1,
          'Slab 1: ₹199/month',
          'First Month Trial ₹99',
          'Toto, Bike, Labour, Small Mistri',
          199.0,
        ),
        const SizedBox(height: 12),
        _buildSlabCard(
          context,
          2,
          'Slab 2: ₹399/month',
          '7 Days Free Trial',
          'Car Driver, AC Mistri, Painter, Electrician',
          399.0,
          isPopular: true,
        ),
        const SizedBox(height: 12),
        _buildSlabCard(
          context,
          3,
          'Slab 3: ₹699/month',
          '7 Days Free Trial',
          'Truck, Bus, JCB, Interior Designer',
          699.0,
        ),
        const SizedBox(height: 12),
        _buildSlabCard(
          context,
          4,
          'Slab 4: ₹999/month',
          '7 Days Free Trial',
          'Agency & Multi-Vehicle Fleets',
          999.0,
        ),
      ],
    );
  }

  Widget _buildSlabCard(
    BuildContext context,
    int slabNum,
    String title,
    String trialText,
    String professions,
    double price, {
    bool isPopular = false,
  }) {
    return Card(
      elevation: 2,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(14),
        side: isPopular ? const BorderSide(color: Color(0xFFFFD700), width: 2) : BorderSide.none,
      ),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(title, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                  decoration: BoxDecoration(
                    color: const Color(0xFFFFD700).withOpacity(0.2),
                    borderRadius: BorderRadius.circular(6),
                  ),
                  child: Text(trialText, style: const TextStyle(fontSize: 10, fontWeight: FontWeight.bold, color: Color(0xFF2B2B2B))),
                ),
              ],
            ),
            const SizedBox(height: 8),
            Text('Eligible: $professions', style: const TextStyle(fontSize: 12, color: Colors.black54)),
            const SizedBox(height: 14),
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                backgroundColor: const Color(0xFFFFD700),
                foregroundColor: Colors.black,
                minimumSize: const Size.fromHeight(40),
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
              ),
              onPressed: () => _paySlab(context, slabNum, price),
              child: Text('Pay Now via Razorpay (₹${price.toInt()})', style: const TextStyle(fontWeight: FontWeight.bold)),
            ),
          ],
        ),
      ),
    );
  }
}

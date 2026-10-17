package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.LocalizationManager
import com.example.data.model.Language
import com.example.data.repository.HireKoroRepository
import com.example.ui.theme.*

@Composable
fun ReferEarnScreen(
    repository: HireKoroRepository,
    language: Language
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val referralCount by repository.referralCount.collectAsState()
    val milestoneClaimed by repository.referralMilestoneClaimed.collectAsState()
    val leaderboard by repository.referralLeaderboard.collectAsState()
    val code = repository.myReferralCode

    var copiedSnackbar by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HireKoroBackground)
            .padding(16.dp)
            .testTag("refer_earn_screen")
    ) {
        Text(
            text = LocalizationManager.getString("menu_refer", language),
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = HireKoroCharcoal
        )
        Text(
            text = "User gets ₹50 • Provider gets ₹100 • 5 Referrals = Extra ₹500 Milestone!",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Code Banner Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = HireKoroCharcoal)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = LocalizationManager.getString("refer_code_label", language).uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    color = HireKoroYellow,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = code,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = HireKoroBlack,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(code))
                            copiedSnackbar = true
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).height(42.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (copiedSnackbar) "Copied!" else "Copy Code", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Join HireKoro for Zero Commission Drivers, Vehicles, and Technicians! Use my referral code $code to get ₹50 free wallet balance! Download at: https://hirekoro.app"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Referral Code"))
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HireKoroYellow,
                            contentColor = HireKoroBlack
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).height(42.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(LocalizationManager.getString("share_code", language), fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Milestone Card: 5 Referrals -> Extra ₹500
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = HireKoroYellowDark)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Milestone Reward",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = HireKoroCharcoal
                        )
                    }
                    Surface(
                        color = if (milestoneClaimed) HireKoroGreen.copy(alpha = 0.15f) else HireKoroYellow.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (milestoneClaimed) "CLAIMED (+₹500)" else "$referralCount/5 Invites",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (milestoneClaimed) HireKoroGreen else HireKoroCharcoal,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { (referralCount / 5f).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = HireKoroYellow,
                    trackColor = Color(0xFFEAEAEA)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = LocalizationManager.getString("milestone_title", language),
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Referral Hall of Fame (Top Earners)",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = HireKoroCharcoal
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(leaderboard) { leader ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = when (leader.rank) {
                                1 -> HireKoroYellow
                                2 -> Color(0xFFC0C0C0)
                                3 -> Color(0xFFCD7F32)
                                else -> Color(0xFFEEEEEE)
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "#${leader.rank}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = HireKoroBlack
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = leader.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = HireKoroCharcoal
                            )
                            Text(
                                text = "${leader.count} Friends Invited",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        Text(
                            text = "₹${leader.rewardEarned.toInt()} Earned",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            color = HireKoroGreen
                        )
                    }
                }
            }
        }
    }
}

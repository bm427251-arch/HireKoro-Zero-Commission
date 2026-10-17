package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.LocalizationManager
import com.example.data.model.Language
import com.example.data.repository.HireKoroRepository
import com.example.ui.theme.*

@Composable
fun WalletScreen(
    repository: HireKoroRepository,
    language: Language
) {
    val context = LocalContext.current
    val balance by repository.walletBalance.collectAsState()
    val transactions by repository.walletTransactions.collectAsState()
    val withdrawals by repository.withdrawals.collectAsState()

    var showAddMoneyDialog by remember { mutableStateOf(false) }
    var showWithdrawDialog by remember { mutableStateOf(false) }
    var amountToAdd by remember { mutableStateOf("500") }
    var withdrawAmount by remember { mutableStateOf("500") }
    var withdrawUpi by remember { mutableStateOf("bm427251@okaxis") }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HireKoroBackground)
            .padding(16.dp)
            .testTag("wallet_screen")
    ) {
        Text(
            text = LocalizationManager.getString("wallet_balance", language),
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = HireKoroCharcoal
        )
        Text(
            text = "Zero commission wallet for subscriptions, direct rewards & payouts",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Balance Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = HireKoroCharcoal)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AVAILABLE BALANCE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                    Surface(
                        color = HireKoroYellow,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Razorpay Verified",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = HireKoroBlack,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "₹${"%,.2f".format(balance)}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = HireKoroYellow
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Actions: Add Money (via Razorpay) & Withdraw
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { showAddMoneyDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HireKoroYellow,
                            contentColor = HireKoroBlack
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).height(42.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = LocalizationManager.getString("add_money", language),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = { showWithdrawDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).height(42.dp)
                    ) {
                        Icon(Icons.Default.ArrowOutward, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = LocalizationManager.getString("withdraw_money", language),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Transaction History",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = HireKoroCharcoal
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(transactions) { tx ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = if (tx.isCredit) HireKoroGreen.copy(alpha = 0.15f) else HireKoroRed.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (tx.isCredit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                    contentDescription = null,
                                    tint = if (tx.isCredit) HireKoroGreen else HireKoroRed
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = tx.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = HireKoroCharcoal
                            )
                            Text(
                                text = "${tx.timestamp} • ${tx.description}",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        Text(
                            text = "${if (tx.isCredit) "+" else "-"}₹${tx.amount.toInt()}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = if (tx.isCredit) HireKoroGreen else HireKoroRed
                        )
                    }
                }
            }
        }
    }

    // Add Money via Razorpay Link Dialog
    if (showAddMoneyDialog) {
        AlertDialog(
            onDismissRequest = { showAddMoneyDialog = false },
            title = { Text("Add Money via Razorpay", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Redirects to verified Razorpay link (bharatmitrainfotech) to load funds securely into your HireKoro wallet:",
                        fontSize = 13.sp,
                        color = Color(0xFF555555)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = amountToAdd,
                        onValueChange = { amountToAdd = it },
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        label = { Text("Amount (₹)", color = Color.Black) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = HireKoroYellow,
                            unfocusedBorderColor = Color(0xFFCCCCCC),
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color(0xFF555555)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Cloud Function verifyRazorpayPayment handles webhook confirmation.",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = amountToAdd.toDoubleOrNull() ?: 500.0
                        // Open Razorpay link with amount in paise (100 paise = 1 rupee)
                        val paise = (parsed * 100).toLong()
                        val razorpayUrl = "${HireKoroRepository.RAZORPAY_PAYMENT_URL}?amount=$paise"
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(razorpayUrl))
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                        // Credit wallet
                        repository.addMoneyToWallet(parsed)
                        showAddMoneyDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HireKoroYellow,
                        contentColor = HireKoroBlack
                    )
                ) {
                    Text("Proceed to Razorpay", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMoneyDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }

    // Withdraw Dialog
    if (showWithdrawDialog) {
        AlertDialog(
            onDismissRequest = { showWithdrawDialog = false },
            title = { Text("Request Withdrawal", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Zero commission payout directly to your UPI ID:",
                        fontSize = 13.sp,
                        color = Color(0xFF555555)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = withdrawAmount,
                        onValueChange = { withdrawAmount = it },
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        label = { Text("Withdraw Amount (₹)", color = Color.Black) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = HireKoroYellow,
                            unfocusedBorderColor = Color(0xFFCCCCCC),
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color(0xFF555555)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = withdrawUpi,
                        onValueChange = { withdrawUpi = it },
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        label = { Text("UPI ID (e.g. mobile@upi)", color = Color.Black) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = HireKoroYellow,
                            unfocusedBorderColor = Color(0xFFCCCCCC),
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color(0xFF555555)
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = withdrawAmount.toDoubleOrNull() ?: 0.0
                        val success = repository.requestWithdrawal(parsed, withdrawUpi)
                        showWithdrawDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HireKoroCharcoal,
                        contentColor = Color.White
                    )
                ) {
                    Text("Submit Withdrawal", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWithdrawDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}

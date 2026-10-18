package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.localization.LocalizationManager
import com.example.data.model.Booking
import com.example.data.model.Language
import com.example.data.repository.HireKoroRepository
import com.example.ui.theme.*

@Composable
fun ExitConfirmDialog(
    language: Language,
    onConfirmExit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = LocalizationManager.getString("exit_title", language),
                fontWeight = FontWeight.Bold,
                color = HireKoroCharcoal
            )
        },
        text = {
            Text(
                text = LocalizationManager.getString("exit_message", language),
                fontSize = 15.sp,
                color = Color(0xFF444444)
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirmExit,
                colors = ButtonDefaults.buttonColors(
                    containerColor = HireKoroCharcoal,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("exit_confirm_button")
            ) {
                Text(LocalizationManager.getString("yes", language))
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("exit_cancel_button")
            ) {
                Text(LocalizationManager.getString("no", language), color = HireKoroCharcoal)
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}

@Composable
fun SecretAdminLoginDialog(
    onLoginSuccess: () -> Unit,
    onDismiss: () -> Unit
) {
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.ic_hirekoro_logo),
                    contentDescription = "HireKoro Logo",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(HireKoroCharcoal)
                        .padding(4.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Title "Admin Login" Black Bold
                Text(
                    text = "Admin Login",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "Enter admin credentials to unlock portal",
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Field 1: Email ID
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = {
                        emailInput = it
                        errorMessage = null
                    },
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    label = { Text("Admin Email ID", color = Color.Black) },
                    placeholder = { Text("Enter admin email", color = Color(0xFF777777)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = Color.Black
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_email_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = HireKoroYellow,
                        unfocusedBorderColor = Color(0xFFCCCCCC),
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color(0xFF555555),
                        focusedLeadingIconColor = Color.Black,
                        unfocusedLeadingIconColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Field 2: Password (NEW ADD)
                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = {
                        passwordInput = it
                        errorMessage = null
                    },
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    label = { Text("Password", color = Color.Black) },
                    placeholder = { Text("Enter admin password", color = Color(0xFF777777)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password",
                            tint = Color.Black
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = Color.Black
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_password_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = HireKoroYellow,
                        unfocusedBorderColor = Color(0xFFCCCCCC),
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color(0xFF555555),
                        focusedLeadingIconColor = Color.Black,
                        unfocusedLeadingIconColor = Color.Black,
                        focusedTrailingIconColor = Color.Black,
                        unfocusedTrailingIconColor = Color.Black
                    )
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = HireKoroRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Login Button Yellow #FFD700 Black text "Admin Login"
                Button(
                    onClick = {
                        val email = emailInput.trim()
                        val pass = passwordInput.trim()

                        if (email.isEmpty() || pass.isEmpty()) {
                            errorMessage = "Please enter both Admin Email ID and Password."
                            return@Button
                        }

                        val isAdminEmail = email.equals("admin@hirekoro.com", ignoreCase = true) ||
                                          email.equals("bm427251@gmail.com", ignoreCase = true) ||
                                          email.equals(HireKoroRepository.ADMIN_EMAIL, ignoreCase = true)

                        val isCorrectPassword = pass == "HireKoro@2024" || pass == "admin123" || pass == "Admin@123"

                        if (isAdminEmail && isCorrectPassword) {
                            errorMessage = null
                            onLoginSuccess()
                        } else {
                            errorMessage = "Invalid Admin Email or Password"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD700),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("admin_login_button")
                ) {
                    Text(
                        text = "Admin Login",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Cancel",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {},
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}

@Composable
fun RateProviderDialog(
    booking: Booking,
    onRatingSubmitted: (stars: Int, tags: List<String>, comment: String) -> Unit,
    onDismiss: () -> Unit
) {
    var rating by remember { mutableIntStateOf(5) }
    val availableTags = listOf("On Time", "Good Behaviour", "Fair Rate", "Skilled", "Professional")
    val selectedTags = remember { mutableStateListOf("On Time", "Good Behaviour", "Professional") }
    var comment by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Rate Your Experience with ${booking.providerName}",
                    fontWeight = FontWeight.ExtraBold,
                    color = HireKoroCharcoal,
                    fontSize = 17.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Booking #${booking.id} • ${booking.profession}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Stars
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    (1..5).forEach { starIndex ->
                        IconButton(
                            onClick = { rating = starIndex },
                            modifier = Modifier.size(44.dp).testTag("star_rate_$starIndex")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "$starIndex Stars",
                                tint = if (starIndex <= rating) HireKoroYellowDark else Color(0xFFD1D5DB),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }

                Text(
                    text = when (rating) {
                        1 -> "★ Poor"
                        2 -> "★★ Fair"
                        3 -> "★★★ Good"
                        4 -> "★★★★ Very Good"
                        else -> "★★★★★ Excellent!"
                    },
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = HireKoroCharcoal
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Select Experience Tags:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = HireKoroCharcoal,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Tags chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    availableTags.take(3).forEach { tag ->
                        val isSelected = selectedTags.contains(tag)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                            },
                            label = { Text(tag, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = HireKoroYellow,
                                selectedLabelColor = HireKoroBlack
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    availableTags.drop(3).forEach { tag ->
                        val isSelected = selectedTags.contains(tag)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                            },
                            label = { Text(tag, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = HireKoroYellow,
                                selectedLabelColor = HireKoroBlack
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    label = { Text("Write your review (Optional)", color = Color.Black) },
                    placeholder = { Text("e.g. Arrived right on time, polite and expert work.", color = Color(0xFF666666)) },
                    modifier = Modifier.fillMaxWidth().testTag("rating_comment_input"),
                    maxLines = 3,
                    shape = RoundedCornerShape(10.dp),
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
                    onRatingSubmitted(rating, selectedTags.toList(), comment)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = HireKoroYellow,
                    contentColor = HireKoroBlack
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("submit_rating_btn")
            ) {
                Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Submit Rating", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel", color = Color.Gray, fontSize = 12.sp)
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White
    )
}

@Composable
fun ReportIssueDialog(
    booking: Booking,
    onIssueReported: (reason: String, description: String, evidence: String) -> Unit,
    onDismiss: () -> Unit
) {
    val reasons = listOf("Didn't come", "Overcharged", "Rude", "Vehicle not as shown")
    var selectedReason by remember { mutableStateOf(reasons[0]) }
    var description by remember { mutableStateOf("") }
    var attachedEvidence by remember { mutableStateOf("Photo & Bill Attached") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = HireKoroRed,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Report Issue",
                    fontWeight = FontWeight.Bold,
                    color = HireKoroCharcoal,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Booking #${booking.id} • ${booking.providerName}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text("Reason for Complaint:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))

                reasons.forEach { r ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedReason = r }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedReason == r,
                            onClick = { selectedReason = r },
                            colors = RadioButtonDefaults.colors(selectedColor = HireKoroRed)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(r, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    label = { Text("Describe the issue in detail", color = Color.Black) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = HireKoroRed,
                        unfocusedBorderColor = Color(0xFFCCCCCC),
                        focusedLabelColor = HireKoroRed,
                        unfocusedLabelColor = Color(0xFF555555)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Evidence attached: $attachedEvidence (Storage Ready)",
                    fontSize = 11.sp,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onIssueReported(selectedReason, description, attachedEvidence)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = HireKoroRed,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Submit Complaint", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}

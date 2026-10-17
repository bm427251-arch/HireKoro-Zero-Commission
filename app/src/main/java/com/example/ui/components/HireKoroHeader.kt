package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Language
import com.example.ui.theme.HireKoroBlack
import com.example.ui.theme.HireKoroCharcoal
import com.example.ui.theme.HireKoroYellow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HireKoroHeader(
    onMenuClick: () -> Unit,
    currentLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    onSecretAdminTriggered: () -> Unit
) {
    var showLangMenu by remember { mutableStateOf(false) }
    val progress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var isHolding by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth().testTag("hirekoro_header"),
        color = HireKoroCharcoal,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Hamburger Menu
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.testTag("hamburger_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open Navigation Menu",
                    tint = HireKoroYellow,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Center: H Logo with 7-second Long Press + Circular Progress
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isHolding = true
                                scope.launch {
                                    // Animate from 0f to 1f over 7 seconds (7000ms)
                                    val completed = progress.animateTo(
                                        targetValue = 1f,
                                        animationSpec = tween(
                                            durationMillis = 7000,
                                            easing = LinearEasing
                                        )
                                    )
                                    if (progress.value >= 1f) {
                                        onSecretAdminTriggered()
                                        progress.snapTo(0f)
                                    }
                                }
                                tryAwaitRelease()
                                isHolding = false
                                scope.launch {
                                    progress.snapTo(0f)
                                }
                            }
                        )
                    }
            ) {
                // Circular Progress Indicator around logo when holding
                if (progress.value > 0f) {
                    CircularProgressIndicator(
                        progress = { progress.value },
                        modifier = Modifier.size(54.dp),
                        color = HireKoroYellow,
                        strokeWidth = 4.dp,
                        trackColor = Color.White.copy(alpha = 0.2f),
                        strokeCap = StrokeCap.Round
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isHolding) Color.White.copy(alpha = 0.15f) else Color.Transparent)
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_hirekoro_logo),
                        contentDescription = "HireKoro Logo (Hold 7s for Admin)",
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Hire",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "Koro",
                                color = HireKoroYellow,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            )
                        }
                        if (isHolding) {
                            Text(
                                text = "Admin: ${(progress.value * 7).toInt()}/7s",
                                color = HireKoroYellow,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = "0% Commission",
                                color = Color.LightGray,
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }

            // Right: Language Selector
            Box {
                Button(
                    onClick = { showLangMenu = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HireKoroYellow,
                        contentColor = HireKoroBlack
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp).testTag("language_selector_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = "Select Language",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = currentLanguage.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                DropdownMenu(
                    expanded = showLangMenu,
                    onDismissRequest = { showLangMenu = false }
                ) {
                    Language.entries.forEach { lang ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = lang.displayName,
                                    fontWeight = if (lang == currentLanguage) FontWeight.Bold else FontWeight.Normal,
                                    color = if (lang == currentLanguage) HireKoroCharcoal else Color.Black
                                )
                            },
                            onClick = {
                                onLanguageSelected(lang)
                                showLangMenu = false
                            }
                        )
                    }
                }
            }
        }
    }
}

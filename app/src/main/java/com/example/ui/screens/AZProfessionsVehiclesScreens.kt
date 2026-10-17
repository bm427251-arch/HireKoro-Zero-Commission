package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.LocalizationManager
import com.example.data.model.Language
import com.example.data.model.MainTab
import com.example.data.model.Profession
import com.example.data.model.Vehicle
import com.example.data.repository.HireKoroRepository
import com.example.ui.theme.HireKoroBackground
import com.example.ui.theme.HireKoroCharcoal
import com.example.ui.theme.HireKoroYellow
import java.util.Locale

@Composable
fun AZProfessionsScreen(
    repository: HireKoroRepository,
    language: Language,
    onSelectProfession: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val professions by repository.professions.collectAsState()

    val filtered = remember(searchQuery, professions) {
        if (searchQuery.isBlank()) professions
        else professions.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true) ||
            it.description.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HireKoroBackground)
            .padding(16.dp)
            .testTag("az_professions_screen")
    ) {
        Text(
            text = "A-Z Professions Directory",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = HireKoroCharcoal
        )
        Text(
            text = "All verified on-demand technical, driving and household trades",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            ),
            placeholder = { Text("Search Driver, Plumber, AC Mistri, Cook...", color = Color(0xFF666666)) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color.Black) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("az_professions_search_input"),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = HireKoroYellow,
                unfocusedBorderColor = Color(0xFFCCCCCC),
                focusedLeadingIconColor = Color.Black,
                unfocusedLeadingIconColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filtered) { p ->
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
                            color = HireKoroYellow.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (p.category == "Driver") Icons.Default.DirectionsCar else Icons.Default.Build,
                                    contentDescription = null,
                                    tint = HireKoroCharcoal
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = p.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = HireKoroCharcoal
                            )
                            Text(
                                text = "${p.category} • Slab ${p.slabAssigned} • ${p.description}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        Button(
                            onClick = { onSelectProfession(p.name) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = HireKoroCharcoal,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Find", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AZVehiclesScreen(
    repository: HireKoroRepository,
    language: Language,
    onSelectVehicle: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val vehicles by repository.vehicles.collectAsState()

    val filtered = remember(searchQuery, vehicles) {
        if (searchQuery.isBlank()) vehicles
        else vehicles.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true) ||
            it.description.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HireKoroBackground)
            .padding(16.dp)
            .testTag("az_vehicles_screen")
    ) {
        Text(
            text = "A-Z Vehicles Fleet Directory",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = HireKoroCharcoal
        )
        Text(
            text = "Toto, Auto, JCB, Ambulance, Trucks, Sedans & Heavy Fleet",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            ),
            placeholder = { Text("Search JCB, Toto, Innova, Ambulance, Truck...", color = Color(0xFF666666)) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color.Black) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("az_vehicles_search_input"),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = HireKoroYellow,
                unfocusedBorderColor = Color(0xFFCCCCCC),
                focusedLeadingIconColor = Color.Black,
                unfocusedLeadingIconColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filtered) { v ->
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
                            color = HireKoroYellow.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = when {
                                        v.category == "Heavy Machinery" -> Icons.Default.Engineering
                                        v.category == "Commercial" -> Icons.Default.LocalShipping
                                        else -> Icons.Default.DirectionsCar
                                    },
                                    contentDescription = null,
                                    tint = HireKoroCharcoal
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = v.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = HireKoroCharcoal
                            )
                            Text(
                                text = "${v.category} • Slab ${v.slabAssigned} • ${v.capacity}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        Button(
                            onClick = { onSelectVehicle(v.name) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = HireKoroYellow,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Hire", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

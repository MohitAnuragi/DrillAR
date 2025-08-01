package com.example.drillar.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drillar.data.Drill
import com.example.drillar.ui.theme.DrillARTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrillSelectionScreen(
    drills: List<Drill>,
    onStartArClick: (Drill) -> Unit
) {
    var selectedDrill by remember { mutableStateOf(drills.firstOrNull()) }
    var expanded by remember { mutableStateOf(false) }

    // Modern color scheme
    val containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
    val cardColor = MaterialTheme.colorScheme.surface
    val buttonColor = MaterialTheme.colorScheme.primaryContainer

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                            MaterialTheme.colorScheme.surface
                        ),
                        startY = 0f,
                        endY = 1000f
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
            ) {
                // Header with icon
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsMartialArts,
                        contentDescription = "Drill Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp))

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Select a Drill",
                        fontWeight = FontWeight.Bold,
                        fontSize  = 22.sp,
                        fontFamily = FontFamily.Serif

                        )
                }
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = containerColor,
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.padding(5.dp)
                    ) {
                        TextField(
                            value = selectedDrill?.name ?: "Select a drill",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Available Drills") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(
                                // containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(cardColor)
                        ) {
                            drills.forEach { drill ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            drill.name,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    },
                                    onClick = {
                                        selectedDrill = drill
                                        expanded = false
                                    },
                                    colors = MenuItemColors(
                                        textColor = MaterialTheme.colorScheme.onSurface,
                                        leadingIconColor = MaterialTheme.colorScheme.primary,
                                        trailingIconColor = MaterialTheme.colorScheme.primary,
                                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                                    )
                                )
                            }
                        }
                    }
                }

                // Drill details card with improved styling
                selectedDrill?.let { drill ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .animateContentSize(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = cardColor
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 4.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = "Drill",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = drill.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Divider(
                                color = MaterialTheme.colorScheme.outlineVariant,
                                thickness = 1.dp
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Description",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = drill.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Tips",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = drill.tips,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        selectedDrill?.let {
                            onStartArClick(it)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(56.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "AR",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Start AR Experience", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}


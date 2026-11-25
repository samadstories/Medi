package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.Medicine
import com.example.i18n.AppLanguage
import com.example.i18n.LocalStrings
import com.example.ui.components.EmptyStatePanel
import com.example.ui.components.MedicalSafetyBanner
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AlertCautionAmber
import com.example.ui.theme.AlertCautionContainer
import com.example.ui.theme.AlertSeriousRed
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.VerifiedGreenContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideEffectsScreen(
    medicine: Medicine?,
    language: AppLanguage,
    onOpenDetails: (String) -> Unit,
    onBack: () -> Unit
) {
    val strings = LocalStrings.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.sideEffectsPageTitle) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("side_effects_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (medicine == null) {
            EmptyStatePanel(
                message = strings.noResults,
                modifier = Modifier.padding(innerPadding).padding(16.dp)
            )
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = medicine.name.get(language),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${medicine.brandName} • ${medicine.saltComposition}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                MedicalSafetyBanner(
                    title = strings.seriousReactionsBadge,
                    body = strings.seriousUrgentBanner,
                    isUrgent = true
                )
            }

            // Common Side Effects Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, AlertCautionAmber.copy(alpha = 0.45f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatusBadge(
                            text = strings.commonReactionsBadge,
                            containerColor = AlertCautionContainer,
                            contentColor = AlertCautionAmber
                        )
                        Text(
                            text = medicine.monograph.commonSideEffects.get(language),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Serious Adverse Reactions Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f)),
                    border = BorderStroke(1.5.dp, AlertSeriousRed)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.WarningAmber, contentDescription = null, tint = AlertSeriousRed)
                            Text(
                                text = strings.seriousReactionsBadge,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = AlertSeriousRed
                            )
                        }
                        Text(
                            text = medicine.monograph.seriousSideEffects.get(language),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Precautions & Warnings + Contraindications
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = strings.fieldWarnings,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(medicine.monograph.warnings.get(language), style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = strings.fieldContraindications,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(medicine.monograph.contraindications.get(language), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            item {
                Button(
                    onClick = { onOpenDetails(medicine.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("side_effects_open_full_monograph")
                ) {
                    Text("${strings.fieldOfficialSources}: ${medicine.lastVerifiedDate}")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafetyAndInteractionCheckerScreen(
    medicines: List<Medicine>,
    language: AppLanguage,
    initialModeInteraction: Boolean,
    onOpenMedicineDetail: (String) -> Unit,
    onBack: () -> Unit
) {
    val strings = LocalStrings.current
    var selectedA by remember(medicines) { mutableStateOf(medicines.firstOrNull()) }
    var selectedB by remember(medicines) { mutableStateOf(medicines.getOrNull(1) ?: medicines.firstOrNull()) }
    var checkTriggered by remember { mutableStateOf(initialModeInteraction) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (initialModeInteraction) strings.interactionCheckerTitle else strings.safetyCheckerTitle)
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("safety_checker_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { MedicalSafetyBanner() }

            // Medicine Safety Checker section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.55f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.HealthAndSafety, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Text(strings.safetyCheckerTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }

                        MedicineDropdownPicker(
                            label = strings.selectMedicineA,
                            selected = selectedA,
                            medicines = medicines,
                            language = language,
                            onSelect = { selectedA = it },
                            testTag = "safety_picker_a"
                        )

                        val med = selectedA
                        if (med != null) {
                            StatusBadge(
                                text = if (med.prescriptionRequired) strings.rxRequiredYes else strings.rxRequiredNo,
                                containerColor = if (med.prescriptionRequired) AlertCautionContainer else VerifiedGreenContainer,
                                contentColor = if (med.prescriptionRequired) AlertCautionAmber else VerifiedGreen
                            )
                            Text(strings.fieldPregnancyWarnings, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                            Text(med.monograph.pregnancyWarnings.get(language), style = MaterialTheme.typography.bodySmall)
                            Text(strings.fieldContraindications, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                            Text(med.monograph.contraindications.get(language), style = MaterialTheme.typography.bodySmall)
                            Text(strings.fieldSeriousSideEffects, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, color = AlertSeriousRed)
                            Text(med.monograph.seriousSideEffects.get(language), style = MaterialTheme.typography.bodySmall)

                            OutlinedButton(
                                onClick = { onOpenMedicineDetail(med.id) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Filled.Info, contentDescription = null)
                                Spacer(modifier = Modifier.size(6.dp))
                                Text(med.name.get(language))
                            }
                        }
                    }
                }
            }

            // Drug Interaction Checker section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.55f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.Shield, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                            Text(strings.interactionCheckerTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }

                        MedicineDropdownPicker(
                            label = strings.selectMedicineB,
                            selected = selectedB,
                            medicines = medicines,
                            language = language,
                            onSelect = { selectedB = it },
                            testTag = "safety_picker_b"
                        )

                        Button(
                            onClick = { checkTriggered = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("check_interaction_button")
                        ) {
                            Text(strings.checkInteractionBtn)
                        }

                        if (checkTriggered && selectedA != null && selectedB != null) {
                            val medA = selectedA!!
                            val medB = selectedB!!
                            val sameSalt = medA.genericName.en.equals(medB.genericName.en, ignoreCase = true) ||
                                medA.id == medB.id
                            val crossMatchA = medA.monograph.drugInteractions.en.lowercase()
                                .contains(medB.genericName.en.lowercase().substringBefore(" "))
                            val crossMatchB = medB.monograph.drugInteractions.en.lowercase()
                                .contains(medA.genericName.en.lowercase().substringBefore(" "))

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (sameSalt || crossMatchA || crossMatchB) {
                                        MaterialTheme.colorScheme.errorContainer
                                    } else {
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                    }
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    if (sameSalt) {
                                        Text(
                                            text = "Duplicate Salt Alert: Both selections contain ${medA.genericName.get(language)}.",
                                            fontWeight = FontWeight.Bold,
                                            color = AlertSeriousRed
                                        )
                                    } else if (crossMatchA || crossMatchB) {
                                        Text(
                                            text = "Potential Reference Interaction Listed",
                                            fontWeight = FontWeight.Bold,
                                            color = AlertSeriousRed
                                        )
                                    } else {
                                        Text(
                                            text = "Verified Interaction Notes Comparison",
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = "${medA.name.get(language)}: ${medA.monograph.drugInteractions.get(language)}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = "${medB.name.get(language)}: ${medB.monograph.drugInteractions.get(language)}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = strings.consultDoctorNote,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MedicineDropdownPicker(
    label: String,
    selected: Medicine?,
    medicines: List<Medicine>,
    language: AppLanguage,
    onSelect: (Medicine) -> Unit,
    testTag: String
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selected?.name?.get(language) ?: label)
                Icon(Icons.Filled.ArrowDropDown, contentDescription = label)
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            medicines.forEach { med ->
                DropdownMenuItem(
                    text = { Text("${med.name.get(language)} (${med.brandName})") },
                    onClick = {
                        onSelect(med)
                        expanded = false
                    }
                )
            }
        }
    }
}

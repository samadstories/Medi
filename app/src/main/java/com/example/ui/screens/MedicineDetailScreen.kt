package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.AlarmAdd
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.Medicine
import com.example.data.model.PriceOffer
import com.example.data.model.UiState
import com.example.data.remote.FdaMonographSnippet
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MedicineDetailScreen(
    medicine: Medicine?,
    allMedicines: List<Medicine>,
    priceOffersForMedicine: List<PriceOffer>,
    language: AppLanguage,
    isFavorite: Boolean,
    fdaLiveState: UiState<FdaMonographSnippet>?,
    onRecordViewed: (Medicine) -> Unit,
    onToggleFavorite: (Medicine, Boolean) -> Unit,
    onComparePrices: (String) -> Unit,
    onOpenSideEffects: (String) -> Unit,
    onOpenAlternativeMedicine: (String) -> Unit,
    onQuickAddReminder: (Medicine) -> Unit,
    onFetchOpenFda: (String) -> Unit,
    onBack: () -> Unit
) {
    val strings = LocalStrings.current
    val context = LocalContext.current
    var showAlternativesSheet by remember { mutableStateOf(false) }

    LaunchedEffect(medicine?.id) {
        if (medicine != null) {
            onRecordViewed(medicine)
        }
    }

    if (medicine == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(strings.appName) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            EmptyStatePanel(
                message = strings.noResults,
                modifier = Modifier.padding(padding).padding(16.dp)
            )
        }
        return
    }

    val genericOptions = remember(priceOffersForMedicine) {
        priceOffersForMedicine.filter { it.genericOption }
    }
    val categoryAlternatives = remember(allMedicines, medicine.id) {
        allMedicines.filter { it.id != medicine.id && it.categoryId == medicine.categoryId }
    }

    fun shareMedicine() {
        val summary = buildString {
            appendLine("${medicine.name.get(language)} (${medicine.brandName})")
            appendLine("${strings.fieldGenericName}: ${medicine.genericName.get(language)}")
            appendLine("${strings.fieldSaltComposition}: ${medicine.saltComposition}")
            appendLine("${strings.fieldUsedFor}: ${medicine.monograph.usedFor.get(language)}")
            appendLine("${strings.fieldOfficialSources}: ${medicine.officialSources} (${medicine.lastVerifiedDate})")
            appendLine()
            append(strings.consultDoctorNote)
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, medicine.name.get(language))
            putExtra(Intent.EXTRA_TEXT, summary)
        }
        context.startActivity(Intent.createChooser(intent, strings.shareAction))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(medicine.name.get(language), maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onToggleFavorite(medicine, isFavorite) },
                        modifier = Modifier.testTag("detail_favorite_button")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = strings.favorites
                        )
                    }
                    IconButton(
                        onClick = ::shareMedicine,
                        modifier = Modifier.testTag("detail_share_button")
                    ) {
                        Icon(Icons.Filled.Share, contentDescription = strings.shareAction)
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 4. Medicine Image + 1, 2, 3 Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            if (medicine.imageUrl.isNotBlank()) {
                                AsyncImage(
                                    model = medicine.imageUrl,
                                    contentDescription = strings.fieldMedicineImage,
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(id = R.drawable.img_medicine_placeholder),
                                    error = painterResource(id = R.drawable.img_medicine_placeholder),
                                    modifier = Modifier
                                        .size(88.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.img_medicine_placeholder),
                                    contentDescription = strings.fieldMedicineImage,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(88.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "${strings.fieldMedicineName}: ${medicine.name.get(language)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${strings.fieldBrandName}: ${medicine.brandName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${strings.fieldGenericName}: ${medicine.genericName.get(language)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            StatusBadge(
                                text = if (medicine.prescriptionRequired) strings.rxRequiredYes else strings.rxRequiredNo,
                                containerColor = if (medicine.prescriptionRequired) AlertCautionContainer else VerifiedGreenContainer,
                                contentColor = if (medicine.prescriptionRequired) AlertCautionAmber else VerifiedGreen
                            )
                        }

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AssistChip(
                                onClick = { onComparePrices(medicine.id) },
                                leadingIcon = {
                                    Icon(Icons.AutoMirrored.Filled.CompareArrows, null, modifier = Modifier.size(18.dp))
                                },
                                label = { Text(strings.comparePricesAction) },
                                modifier = Modifier.testTag("detail_compare_chip")
                            )
                            AssistChip(
                                onClick = { onOpenSideEffects(medicine.id) },
                                leadingIcon = {
                                    Icon(Icons.Filled.WarningAmber, null, modifier = Modifier.size(18.dp))
                                },
                                label = { Text(strings.sideEffectsAction) },
                                modifier = Modifier.testTag("detail_side_effects_chip")
                            )
                            AssistChip(
                                onClick = { showAlternativesSheet = true },
                                leadingIcon = {
                                    Icon(Icons.Filled.SwapHoriz, null, modifier = Modifier.size(18.dp))
                                },
                                label = { Text("${strings.genericFinderAction} / ${strings.alternativesAction}") },
                                modifier = Modifier.testTag("detail_alternatives_chip")
                            )
                            AssistChip(
                                onClick = { onQuickAddReminder(medicine) },
                                leadingIcon = {
                                    Icon(Icons.Filled.AlarmAdd, null, modifier = Modifier.size(18.dp))
                                },
                                label = { Text(strings.addReminderAction) },
                                modifier = Modifier.testTag("detail_add_reminder_chip")
                            )
                        }
                    }
                }
            }

            // 5. Salt / Composition
            item { MonographSectionCard(strings.fieldSaltComposition, medicine.saltComposition) }
            // 6. What is this medicine used for?
            item { MonographSectionCard(strings.fieldUsedFor, medicine.monograph.usedFor.get(language)) }
            // 7. Uses and Benefits
            item { MonographSectionCard(strings.fieldUsesAndBenefits, medicine.monograph.usesAndBenefits.get(language)) }
            // 8. How it works
            item { MonographSectionCard(strings.fieldHowItWorks, medicine.monograph.howItWorks.get(language)) }
            // 9. Common Side Effects
            item { MonographSectionCard(strings.fieldCommonSideEffects, medicine.monograph.commonSideEffects.get(language)) }
            // 10. Serious Side Effects (Highlighted Urgent Card)
            item {
                MonographSectionCard(
                    title = strings.fieldSeriousSideEffects,
                    body = medicine.monograph.seriousSideEffects.get(language),
                    isSerious = true
                )
            }
            // 11. Warnings and Precautions
            item { MonographSectionCard(strings.fieldWarnings, medicine.monograph.warnings.get(language)) }
            // 12. Drug Interactions
            item { MonographSectionCard(strings.fieldDrugInteractions, medicine.monograph.drugInteractions.get(language)) }
            // 13. Contraindications
            item { MonographSectionCard(strings.fieldContraindications, medicine.monograph.contraindications.get(language)) }
            // 14. Pregnancy and Breastfeeding Warnings
            item { MonographSectionCard(strings.fieldPregnancyWarnings, medicine.monograph.pregnancyWarnings.get(language)) }
            // 15. Storage Instructions
            item { MonographSectionCard(strings.fieldStorageInstructions, medicine.monograph.storageInstructions.get(language)) }
            // 16. Manufacturer Details
            item { MonographSectionCard(strings.fieldManufacturerDetails, medicine.manufacturerDetails.get(language)) }
            // 17. Prescription Required (Yes/No)
            item {
                MonographSectionCard(
                    strings.fieldPrescriptionRequired,
                    if (medicine.prescriptionRequired) strings.rxRequiredYes else strings.rxRequiredNo
                )
            }
            // 18. Official Information Sources + Last Verified Date
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.Verified, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Text(
                                text = strings.fieldOfficialSources,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(medicine.officialSources, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "${strings.lastVerifiedLabel}: ${medicine.lastVerifiedDate}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        if (medicine.openFdaGenericQuery.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedButton(
                                onClick = { onFetchOpenFda(medicine.openFdaGenericQuery) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("fetch_openfda_button")
                            ) {
                                Icon(Icons.Filled.CloudSync, contentDescription = null)
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(strings.fetchFdaLabelAction)
                            }
                        }
                    }
                }
            }

            // Live OpenFDA Panel
            if (fdaLiveState != null) {
                item {
                    when (fdaLiveState) {
                        is UiState.Loading -> {
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(22.dp))
                                    Text("Fetching official U.S. FDA OpenFDA drug label...")
                                }
                            }
                        }

                        is UiState.Error -> {
                            MedicalSafetyBanner(
                                title = strings.fdaLiveTitle,
                                body = fdaLiveState.message,
                                isUrgent = false
                            )
                        }

                        is UiState.Success -> {
                            val snippet = fdaLiveState.data
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "${strings.fdaLiveTitle} (${snippet.effectiveDate})",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Text("Labeler: ${snippet.manufacturer}", style = MaterialTheme.typography.labelMedium)
                                    if (snippet.indications.isNotBlank()) {
                                        Text("Indications: ${snippet.indications}", style = MaterialTheme.typography.bodySmall)
                                    }
                                    if (snippet.warnings.isNotBlank()) {
                                        Text("FDA Warnings: ${snippet.warnings}", style = MaterialTheme.typography.bodySmall)
                                    }
                                    if (snippet.adverseReactions.isNotBlank()) {
                                        Text("Adverse Reactions: ${snippet.adverseReactions}", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item { MedicalSafetyBanner() }
        }
    }

    if (showAlternativesSheet) {
        ModalBottomSheet(onDismissRequest = { showAlternativesSheet = false }) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "${strings.genericFinderAction} & ${strings.alternativesAction}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = strings.consultDoctorNote,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (genericOptions.isNotEmpty()) {
                    item {
                        Text(
                            text = "${strings.genericBadge} Options (Same Salt: ${medicine.saltComposition})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = VerifiedGreen
                        )
                    }
                    items(genericOptions, key = { it.id }) { offer ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = VerifiedGreenContainer.copy(alpha = 0.45f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(offer.brandName, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "${offer.packSize} • Verified Price: ₹${"%.2f".format(offer.sellingPriceInr)} (₹${"%.2f".format(offer.pricePerUnitInr)}/unit)",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(offer.sourceName, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }

                if (categoryAlternatives.isNotEmpty()) {
                    item {
                        Text(
                            text = strings.alternativesAction,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(categoryAlternatives, key = { it.id }) { alt ->
                        Card(
                            onClick = {
                                showAlternativesSheet = false
                                onOpenAlternativeMedicine(alt.id)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(Icons.Filled.Medication, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Column {
                                    Text(alt.name.get(language), fontWeight = FontWeight.SemiBold)
                                    Text(alt.saltComposition, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = {
                            showAlternativesSheet = false
                            onComparePrices(medicine.id)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(strings.comparePricesAction)
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }
        }
    }
}

@Composable
private fun MonographSectionCard(
    title: String,
    body: String,
    isSerious: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isSerious) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f)
            else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSerious) AlertSeriousRed.copy(alpha = 0.55f)
            else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (isSerious) AlertSeriousRed else MaterialTheme.colorScheme.primary
            )
            Text(
                text = body.ifBlank { "-" },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

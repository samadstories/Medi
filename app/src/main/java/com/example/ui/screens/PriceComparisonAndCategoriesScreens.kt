package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.model.AvailabilityStatus
import com.example.data.model.Category
import com.example.data.model.Medicine
import com.example.data.model.PriceOffer
import com.example.i18n.AppLanguage
import com.example.i18n.LocalStrings
import com.example.ui.components.EmptyStatePanel
import com.example.ui.components.MedicalSafetyBanner
import com.example.ui.components.StatusBadge
import com.example.ui.components.categoryIconFor
import com.example.ui.theme.AlertCautionAmber
import com.example.ui.theme.AlertCautionContainer
import com.example.ui.theme.AlertSeriousContainer
import com.example.ui.theme.AlertSeriousRed
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.VerifiedGreenContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceComparisonScreen(
    language: AppLanguage,
    initialMedicineId: String?,
    medicines: List<Medicine>,
    priceOffers: List<PriceOffer>,
    onLogCompare: (String) -> Unit,
    onOpenMedicineDetail: (String) -> Unit,
    onBack: (() -> Unit)? = null
) {
    val strings = LocalStrings.current
    var selectedMedicineId by remember(initialMedicineId, medicines) {
        mutableStateOf(initialMedicineId ?: medicines.firstOrNull()?.id)
    }
    var genericOnly by remember { mutableStateOf(false) }

    LaunchedEffect(selectedMedicineId) {
        selectedMedicineId?.let(onLogCompare)
    }

    val filteredOffers = remember(priceOffers, selectedMedicineId, genericOnly) {
        priceOffers.filter { offer ->
            val medMatch = selectedMedicineId == null || offer.medicineId == selectedMedicineId
            val genMatch = !genericOnly || offer.genericOption
            medMatch && genMatch
        }.sortedBy { it.pricePerUnitInr }
    }

    val selectedMedicine = remember(medicines, selectedMedicineId) {
        medicines.firstOrNull { it.id == selectedMedicineId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.priceComparison) },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("compare_back_button")) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
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
            // Verified Price Source Notice
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Filled.Verified, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text(
                            text = strings.priceSourceNote,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Medicine Selector Chips
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedMedicineId == null,
                            onClick = { selectedMedicineId = null },
                            label = { Text(strings.viewAll) },
                            modifier = Modifier.testTag("compare_filter_all")
                        )
                    }
                    items(medicines, key = { it.id }) { med ->
                        FilterChip(
                            selected = selectedMedicineId == med.id,
                            onClick = { selectedMedicineId = med.id },
                            label = { Text(med.genericName.get(language)) },
                            modifier = Modifier.testTag("compare_filter_${med.id}")
                        )
                    }
                }
            }

            // Generic Only Filter + Open Details shortcut
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = genericOnly,
                        onClick = { genericOnly = !genericOnly },
                        leadingIcon = if (genericOnly) {
                            { Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        } else null,
                        label = { Text(strings.filterGenericOnly) },
                        modifier = Modifier.testTag("compare_generic_only_chip")
                    )

                    if (selectedMedicine != null) {
                        FilterChip(
                            selected = false,
                            onClick = { onOpenMedicineDetail(selectedMedicine.id) },
                            label = { Text(selectedMedicine.brandName.substringBefore("/").trim()) },
                            modifier = Modifier.testTag("compare_open_med_detail")
                        )
                    }
                }
            }

            // Side-by-Side Comparison Matrix
            if (filteredOffers.size >= 2) {
                item {
                    Text(
                        text = strings.sideBySideTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(filteredOffers, key = { "matrix_${it.id}" }) { offer ->
                            SideBySideOfferColumn(offer = offer)
                        }
                    }
                }
            }

            // Detailed Offer Cards
            if (filteredOffers.isEmpty()) {
                item {
                    EmptyStatePanel(message = strings.noResults, icon = Icons.Filled.Compare)
                }
            } else {
                items(filteredOffers, key = { it.id }) { offer ->
                    PriceOfferDetailCard(offer = offer)
                }
            }

            item { MedicalSafetyBanner() }
        }
    }
}

@Composable
private fun SideBySideOfferColumn(offer: PriceOffer) {
    val strings = LocalStrings.current
    Card(
        modifier = Modifier
            .width(215.dp)
            .testTag("side_by_side_card_${offer.id}"),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(
            width = if (offer.genericOption) 1.5.dp else 1.dp,
            color = if (offer.genericOption) VerifiedGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            StatusBadge(
                text = if (offer.genericOption) strings.genericBadge else strings.brandedBadge,
                containerColor = if (offer.genericOption) VerifiedGreenContainer else MaterialTheme.colorScheme.primaryContainer,
                contentColor = if (offer.genericOption) VerifiedGreen else MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = offer.brandName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            HorizontalDivider()
            MatrixRow(strings.packSizeLabel, offer.packSize)
            MatrixRow(strings.mrpLabel, "₹${"%.2f".format(offer.mrpInr)}")
            MatrixRow(strings.sellingPriceLabel, "₹${"%.2f".format(offer.sellingPriceInr)}")
            MatrixRow(strings.pricePerUnitLabel, "₹${"%.2f".format(offer.pricePerUnitInr)}")
            MatrixRow(strings.discountLabel, "${offer.discountPercent}%")
            MatrixRow(strings.lastUpdatedLabel, offer.lastUpdatedDate)
        }
    }
}

@Composable
private fun MatrixRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun PriceOfferDetailCard(offer: PriceOffer) {
    val strings = LocalStrings.current
    val availStatus = offer.availabilityStatus()
    val (availLabel, availBg, availFg) = when (availStatus) {
        AvailabilityStatus.IN_STOCK -> Triple(strings.inStockLabel, VerifiedGreenContainer, VerifiedGreen)
        AvailabilityStatus.LIMITED -> Triple(strings.limitedStockLabel, AlertCautionContainer, AlertCautionAmber)
        AvailabilityStatus.OUT_OF_STOCK -> Triple(strings.outOfStockLabel, AlertSeriousContainer, AlertSeriousRed)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("price_offer_card_${offer.id}"),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.55f))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = offer.brandName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${offer.genericName} • ${offer.saltComposition}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                StatusBadge(
                    text = if (offer.genericOption) strings.genericBadge else strings.brandedBadge,
                    containerColor = if (offer.genericOption) VerifiedGreenContainer else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (offer.genericOption) VerifiedGreen else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "${strings.packSizeLabel}: ${offer.packSize}",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "${strings.manufacturerLabel}: ${offer.manufacturerName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (offer.mrpInr > offer.sellingPriceInr) {
                            Text(
                                text = "₹${"%.2f".format(offer.mrpInr)}",
                                style = MaterialTheme.typography.bodySmall,
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "₹${"%.2f".format(offer.sellingPriceInr)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "${strings.pricePerUnitLabel}: ₹${"%.2f".format(offer.pricePerUnitInr)} • ${offer.discountPercent}% ${strings.discountLabel}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = VerifiedGreen
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(text = availLabel, containerColor = availBg, contentColor = availFg)
                Text(
                    text = "${offer.sourceName} • ${offer.lastUpdatedDate}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    language: AppLanguage,
    categories: List<Category>,
    medicines: List<Medicine>,
    onSelectCategory: (String) -> Unit,
    onBack: () -> Unit
) {
    val strings = LocalStrings.current
    val countByCategory = remember(medicines) {
        medicines.groupingBy { it.categoryId }.eachCount()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.medicineCategories) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("categories_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 156.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories, key = { it.id }) { cat ->
                Card(
                    onClick = { onSelectCategory(cat.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("category_grid_${cat.id}"),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.55f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = categoryIconFor(cat.iconKey),
                                contentDescription = cat.localizedName(language),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = cat.localizedName(language),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            minLines = 2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        val count = countByCategory[cat.id] ?: 0
                        Text(
                            text = "$count monograph${if (count == 1) "" else "s"}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.model.Category
import com.example.data.model.Medicine
import com.example.data.model.PriceOffer
import com.example.data.model.RecentlyViewed
import com.example.data.model.UiState
import com.example.i18n.AppLanguage
import com.example.i18n.LocalStrings
import com.example.ui.components.LanguageSelectorChips
import com.example.ui.components.MediGuideBrandLogo
import com.example.ui.components.MedicalSafetyBanner
import com.example.ui.components.MedicineSummaryCard
import com.example.ui.components.StatusBadge
import com.example.ui.components.categoryIconFor
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.VerifiedGreenContainer

@Composable
fun HomeDashboardScreen(
    language: AppLanguage,
    isAdmin: Boolean,
    medicinesState: UiState<List<Medicine>>,
    priceOffersState: UiState<List<PriceOffer>>,
    categoriesState: UiState<List<Category>>,
    favoriteIds: Set<String>,
    recentlyViewed: List<RecentlyViewed>,
    onSelectLanguage: (AppLanguage) -> Unit,
    onOpenSearch: () -> Unit,
    onOpenScanner: () -> Unit,
    onOpenVoiceSearch: () -> Unit,
    onOpenCategories: () -> Unit,
    onCategorySelected: (String) -> Unit,
    onOpenMedicineDetail: (String) -> Unit,
    onOpenPriceCompare: (String?) -> Unit,
    onOpenFavorites: () -> Unit,
    onOpenSafetyChecker: () -> Unit,
    onOpenInteractionChecker: () -> Unit,
    onOpenAdmin: () -> Unit,
    onToggleFavorite: (Medicine, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    var showLanguageCard by remember { mutableStateOf(false) }

    val medicines = (medicinesState as? UiState.Success)?.data.orEmpty()
    val priceOffers = (priceOffersState as? UiState.Success)?.data.orEmpty()
    val categories = (categoriesState as? UiState.Success)?.data.orEmpty()
    val lowestPriceByMedicine = remember(priceOffers) {
        priceOffers.groupBy { it.medicineId }
            .mapValues { (_, offers) -> offers.minOfOrNull { it.pricePerUnitInr } }
    }

    val recentMeds = remember(recentlyViewed, medicines) {
        val medById = medicines.associateBy { it.id }
        recentlyViewed.mapNotNull { medById[it.medicineId] }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Header + Language + Favorites + Admin shortcut
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.55f))
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.06f)
                                )
                            )
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            MediGuideBrandLogo(size = 46.dp)
                            Column {
                                Text(
                                    text = strings.appName,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = strings.appTagline,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (isAdmin) {
                                IconButton(
                                    onClick = onOpenAdmin,
                                    modifier = Modifier.testTag("home_admin_button")
                                ) {
                                    Icon(
                                        Icons.Filled.AdminPanelSettings,
                                        contentDescription = strings.adminDashboardTitle,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            IconButton(
                                onClick = onOpenFavorites,
                                modifier = Modifier.testTag("home_favorites_button")
                            ) {
                                Icon(
                                    Icons.Filled.Bookmark,
                                    contentDescription = strings.favorites,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Surface(
                                onClick = { showLanguageCard = !showLanguageCard },
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.testTag("home_language_toggle")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.Language,
                                        contentDescription = strings.languageSelection,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = language.code.uppercase(),
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                    }

                    if (showLanguageCard) {
                        LanguageSelectorChips(
                            selectedLanguage = language,
                            onSelectLanguage = {
                                onSelectLanguage(it)
                                showLanguageCard = false
                            }
                        )
                    }

                    // Clinical Search Bar
                    Surface(
                        onClick = onOpenSearch,
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_search_bar")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = strings.navSearch,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = strings.searchPlaceholder,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = onOpenVoiceSearch,
                                modifier = Modifier
                                    .size(40.dp)
                                    .testTag("home_voice_search_button")
                            ) {
                                Icon(
                                    Icons.Filled.Mic,
                                    contentDescription = strings.searchVoice,
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                            IconButton(
                                onClick = onOpenScanner,
                                modifier = Modifier
                                    .size(40.dp)
                                    .testTag("home_camera_scan_button")
                            ) {
                                Icon(
                                    Icons.Filled.CameraAlt,
                                    contentDescription = strings.searchScan,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Tools Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionTile(
                    title = strings.priceComparison,
                    icon = Icons.AutoMirrored.Filled.CompareArrows,
                    onClick = { onOpenPriceCompare(null) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quick_price_compare")
                )
                QuickActionTile(
                    title = strings.safetyCheckerTitle,
                    icon = Icons.Filled.HealthAndSafety,
                    onClick = onOpenSafetyChecker,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quick_safety_checker")
                )
                QuickActionTile(
                    title = strings.interactionCheckerTitle,
                    icon = Icons.Filled.Shield,
                    onClick = onOpenInteractionChecker,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quick_interaction_checker")
                )
            }
        }

        // Language Quick Switcher Strip (always accessible on Home)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = strings.languageSelection,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                LanguageSelectorChips(
                    selectedLanguage = language,
                    onSelectLanguage = onSelectLanguage
                )
            }
        }

        // Medicine Categories
        item {
            SectionHeader(
                title = strings.medicineCategories,
                actionLabel = strings.viewAll,
                onAction = onOpenCategories,
                actionTag = "view_all_categories"
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(categories, key = { it.id }) { cat ->
                    Card(
                        onClick = { onCategorySelected(cat.id) },
                        modifier = Modifier
                            .width(126.dp)
                            .testTag("home_cat_${cat.id}"),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = categoryIconFor(cat.iconKey),
                                    contentDescription = cat.localizedName(language),
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(
                                text = cat.localizedName(language),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 2,
                                minLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // Price Comparison Highlight Banner
        item {
            Card(
                onClick = { onOpenPriceCompare(null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_price_highlight_card"),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Compare,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = strings.priceComparison,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = strings.priceComparisonSubtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        StatusBadge(
                            text = "${priceOffers.size} verified MRP & Jan Aushadhi entries",
                            containerColor = VerifiedGreenContainer,
                            contentColor = VerifiedGreen
                        )
                    }
                }
            }
        }

        // Recently Viewed (if available)
        if (recentMeds.isNotEmpty()) {
            item {
                SectionHeader(
                    title = strings.recentlyViewed,
                    actionLabel = null,
                    onAction = {},
                    actionTag = ""
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(recentMeds, key = { it.id }) { med ->
                        AssistChip(
                            onClick = { onOpenMedicineDetail(med.id) },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.History,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = med.name.get(language),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            modifier = Modifier.testTag("recent_chip_${med.id}")
                        )
                    }
                }
            }
        }

        // Popular Medicines
        item {
            SectionHeader(
                title = strings.popularMedicines,
                actionLabel = strings.viewAll,
                onAction = onOpenSearch,
                actionTag = "view_all_popular"
            )
        }

        when (medicinesState) {
            is UiState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is UiState.Error -> {
                item {
                    MedicalSafetyBanner(
                        title = "Catalog Notice",
                        body = medicinesState.message,
                        isUrgent = true
                    )
                }
            }

            is UiState.Success -> {
                items(medicines.take(6), key = { it.id }) { med ->
                    val isFav = med.id in favoriteIds
                    MedicineSummaryCard(
                        medicine = med,
                        language = language,
                        isFavorite = isFav,
                        lowestPriceInr = lowestPriceByMedicine[med.id],
                        onClick = { onOpenMedicineDetail(med.id) },
                        onToggleFavorite = { onToggleFavorite(med, isFav) },
                        onCompareClick = { onOpenPriceCompare(med.id) }
                    )
                }
            }
        }

        // Health Information Cards
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = strings.healthInformation,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                strings.healthTips.forEach { (title, desc) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Filled.LocalPharmacy,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = desc,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Mandatory Clinical Disclaimer Banner
        item {
            MedicalSafetyBanner()
        }
    }
}

@Composable
private fun QuickActionTile(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.55f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
            }
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionLabel: String?,
    onAction: () -> Unit,
    actionTag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        if (!actionLabel.isNullOrBlank()) {
            TextButton(
                onClick = onAction,
                modifier = if (actionTag.isNotBlank()) Modifier.testTag(actionTag) else Modifier
            ) {
                Text(actionLabel)
            }
        }
    }
}

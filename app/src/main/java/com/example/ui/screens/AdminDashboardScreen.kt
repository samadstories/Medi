package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.AnalyticsEvent
import com.example.data.model.ApiConnection
import com.example.data.model.Category
import com.example.data.model.Manufacturer
import com.example.data.model.Medicine
import com.example.data.model.PriceOffer
import com.example.data.model.TranslationOverride
import com.example.i18n.AppLanguage
import com.example.i18n.LocalStrings
import com.example.ui.components.MedicalSafetyBanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    isAdmin: Boolean,
    busy: Boolean,
    adminBanner: String?,
    language: AppLanguage,
    medicines: List<Medicine>,
    priceOffers: List<PriceOffer>,
    categories: List<Category>,
    manufacturers: List<Manufacturer>,
    translations: List<TranslationOverride>,
    apiConnections: List<ApiConnection>,
    analyticsEvents: List<AnalyticsEvent>,
    onDismissBanner: () -> Unit,
    onSeedCatalog: () -> Unit,
    onSaveMedicine: (Medicine) -> Unit,
    onDeleteMedicine: (String) -> Unit,
    onSavePriceOffer: (PriceOffer) -> Unit,
    onDeletePriceOffer: (String) -> Unit,
    onSaveCategory: (Category) -> Unit,
    onSaveManufacturer: (Manufacturer) -> Unit,
    onSaveTranslation: (TranslationOverride) -> Unit,
    onSaveAndTestApi: (ApiConnection) -> Unit,
    onBack: () -> Unit
) {
    val strings = LocalStrings.current

    if (!isAdmin) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(strings.adminDashboardTitle) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                MedicalSafetyBanner(
                    title = "Admin Access Restricted",
                    body = "Only verified MediGuide administrators may modify catalog monographs, pricing, or API connections.",
                    isUrgent = true
                )
            }
        }
        return
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    var editingMedicine by remember { mutableStateOf<Medicine?>(null) }
    var showMedicineDialog by remember { mutableStateOf(false) }
    var editingPrice by remember { mutableStateOf<PriceOffer?>(null) }
    var showPriceDialog by remember { mutableStateOf(false) }
    var showCatDialog by remember { mutableStateOf(false) }
    var showMfrDialog by remember { mutableStateOf(false) }
    var showTransDialog by remember { mutableStateOf(false) }
    var showApiDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        strings.adminAnalyticsTab,
        strings.adminMedicinesTab,
        strings.adminPricesTab,
        strings.adminCatalogTab
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.adminDashboardTitle) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("admin_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (busy) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            if (!adminBanner.isNullOrBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(adminBanner, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                        TextButton(onClick = onDismissBanner) { Text("OK") }
                    }
                }
            }

            ScrollableTabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { idx, title ->
                    Tab(
                        selected = selectedTab == idx,
                        onClick = { selectedTab = idx },
                        text = { Text(title) },
                        modifier = Modifier.testTag("admin_tab_$idx")
                    )
                }
            }

            when (selectedTab) {
                0 -> {
                    val eventCounts = analyticsEvents.groupingBy { it.eventType }.eachCount()
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Button(
                                onClick = onSeedCatalog,
                                enabled = !busy,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("admin_seed_catalog_btn")
                            ) {
                                Icon(Icons.Filled.CloudUpload, contentDescription = null)
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(strings.seedCatalogBtn)
                            }
                        }
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                AdminStatCard("Medicines", medicines.size.toString(), Modifier.weight(1f))
                                AdminStatCard("Price Offers", priceOffers.size.toString(), Modifier.weight(1f))
                            }
                        }
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                AdminStatCard("Categories", categories.size.toString(), Modifier.weight(1f))
                                AdminStatCard("Manufacturers", manufacturers.size.toString(), Modifier.weight(1f))
                            }
                        }
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                AdminStatCard("Views", (eventCounts["VIEW"] ?: 0).toString(), Modifier.weight(1f))
                                AdminStatCard("Searches", ((eventCounts["SEARCH"] ?: 0) + (eventCounts["SCAN"] ?: 0) + (eventCounts["VOICE"] ?: 0)).toString(), Modifier.weight(1f))
                                AdminStatCard("Compares", (eventCounts["COMPARE"] ?: 0).toString(), Modifier.weight(1f))
                            }
                        }
                    }
                }

                1 -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Button(
                                onClick = {
                                    editingMedicine = null
                                    showMedicineDialog = true
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("admin_add_medicine_btn")
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = null)
                                Spacer(modifier = Modifier.size(6.dp))
                                Text(strings.addMedicineBtn)
                            }
                        }
                        items(medicines, key = { it.id }) { med ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(med.name.get(language), fontWeight = FontWeight.Bold)
                                        Text("${med.brandName} • ${med.categoryId}", style = MaterialTheme.typography.bodySmall)
                                    }
                                    Row {
                                        IconButton(onClick = {
                                            editingMedicine = med
                                            showMedicineDialog = true
                                        }) {
                                            Icon(Icons.Filled.Edit, contentDescription = "Edit")
                                        }
                                        IconButton(onClick = { onDeleteMedicine(med.id) }) {
                                            Icon(Icons.Filled.Delete, contentDescription = strings.deleteText, tint = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Button(
                                onClick = {
                                    editingPrice = null
                                    showPriceDialog = true
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("admin_add_price_btn")
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = null)
                                Spacer(modifier = Modifier.size(6.dp))
                                Text(strings.addPriceOfferBtn)
                            }
                        }
                        items(priceOffers, key = { it.id }) { offer ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(offer.brandName, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = "₹${"%.2f".format(offer.sellingPriceInr)} (MRP ₹${"%.2f".format(offer.mrpInr)}) • ${offer.packSize}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Row {
                                        IconButton(onClick = {
                                            editingPrice = offer
                                            showPriceDialog = true
                                        }) {
                                            Icon(Icons.Filled.Edit, contentDescription = "Edit")
                                        }
                                        IconButton(onClick = { onDeletePriceOffer(offer.id) }) {
                                            Icon(Icons.Filled.Delete, contentDescription = strings.deleteText, tint = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                3 -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(onClick = { showCatDialog = true }, modifier = Modifier.weight(1f)) {
                                    Text(strings.addCategoryBtn)
                                }
                                OutlinedButton(onClick = { showMfrDialog = true }, modifier = Modifier.weight(1f)) {
                                    Text(strings.addManufacturerBtn)
                                }
                            }
                        }
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(onClick = { showTransDialog = true }, modifier = Modifier.weight(1f)) {
                                    Text(strings.addTranslationBtn)
                                }
                                OutlinedButton(onClick = { showApiDialog = true }, modifier = Modifier.weight(1f)) {
                                    Text("+ API")
                                }
                            }
                        }
                        item {
                            Text(strings.apiConnectionsTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        items(apiConnections, key = { it.id }) { conn ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(conn.name, fontWeight = FontWeight.Bold)
                                    Text(conn.baseUrl, style = MaterialTheme.typography.bodySmall)
                                    if (conn.statusMessage.isNotBlank()) {
                                        Text(conn.statusMessage, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                    }
                                    OutlinedButton(onClick = { onSaveAndTestApi(conn) }) {
                                        Icon(Icons.Filled.NetworkCheck, contentDescription = null)
                                        Spacer(modifier = Modifier.size(6.dp))
                                        Text(strings.testConnectionBtn)
                                    }
                                }
                            }
                        }
                        if (translations.isNotEmpty()) {
                            item {
                                Text("Translations (${translations.size})", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            }
                            items(translations, key = { it.id }) { tr ->
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(tr.key, fontWeight = FontWeight.Bold)
                                        Text("EN: ${tr.en} | HI: ${tr.hi} | BN: ${tr.bn}", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showMedicineDialog) {
        AdminMedicineEditorDialog(
            initial = editingMedicine,
            onDismiss = { showMedicineDialog = false },
            onSave = {
                onSaveMedicine(it)
                showMedicineDialog = false
            }
        )
    }
    if (showPriceDialog) {
        AdminPriceEditorDialog(
            initial = editingPrice,
            onDismiss = { showPriceDialog = false },
            onSave = {
                onSavePriceOffer(it)
                showPriceDialog = false
            }
        )
    }
    AdminCatalogMetaDialogs(
        showCategoryDialog = showCatDialog,
        showManufacturerDialog = showMfrDialog,
        showTranslationDialog = showTransDialog,
        showApiDialog = showApiDialog,
        onDismissAll = {
            showCatDialog = false
            showMfrDialog = false
            showTransDialog = false
            showApiDialog = false
        },
        onSaveCategory = onSaveCategory,
        onSaveManufacturer = onSaveManufacturer,
        onSaveTranslation = onSaveTranslation,
        onSaveApi = onSaveAndTestApi
    )
}

@Composable
private fun AdminStatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.55f))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

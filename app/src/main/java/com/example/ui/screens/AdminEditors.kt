package com.example.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.ApiConnection
import com.example.data.model.AvailabilityStatus
import com.example.data.model.Category
import com.example.data.model.LocalizedText
import com.example.data.model.Manufacturer
import com.example.data.model.Medicine
import com.example.data.model.MedicineMonograph
import com.example.data.model.PriceOffer
import com.example.data.model.TranslationOverride
import com.example.i18n.LocalStrings

@Composable
fun AdminMedicineEditorDialog(
    initial: Medicine?,
    onDismiss: () -> Unit,
    onSave: (Medicine) -> Unit
) {
    val strings = LocalStrings.current
    var nameEn by remember { mutableStateOf(initial?.name?.en ?: "") }
    var nameHi by remember { mutableStateOf(initial?.name?.hi ?: "") }
    var nameBn by remember { mutableStateOf(initial?.name?.bn ?: "") }
    var brandName by remember { mutableStateOf(initial?.brandName ?: "") }
    var genericEn by remember { mutableStateOf(initial?.genericName?.en ?: "") }
    var salt by remember { mutableStateOf(initial?.saltComposition ?: "") }
    var categoryId by remember { mutableStateOf(initial?.categoryId ?: "fever_pain") }
    var manufacturerId by remember { mutableStateOf(initial?.manufacturerId ?: "mfr_bppi") }
    var mfrDetailsEn by remember { mutableStateOf(initial?.manufacturerDetails?.en ?: "") }
    var imageUrl by remember { mutableStateOf(initial?.imageUrl ?: "") }
    var usedForEn by remember { mutableStateOf(initial?.monograph?.usedFor?.en ?: "") }
    var commonSeEn by remember { mutableStateOf(initial?.monograph?.commonSideEffects?.en ?: "") }
    var seriousSeEn by remember { mutableStateOf(initial?.monograph?.seriousSideEffects?.en ?: "") }
    var warningsEn by remember { mutableStateOf(initial?.monograph?.warnings?.en ?: "") }
    var interactionsEn by remember { mutableStateOf(initial?.monograph?.drugInteractions?.en ?: "") }
    var rxRequired by remember { mutableStateOf(initial?.prescriptionRequired ?: true) }
    var sources by remember { mutableStateOf(initial?.officialSources ?: "CDSCO, NLEM 2022") }
    var verifiedDate by remember { mutableStateOf(initial?.lastVerifiedDate ?: "2026-11-24") }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imageUrl = uri.toString().take(900)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) strings.addMedicineBtn else "Edit Medicine") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(nameEn, { nameEn = it }, label = { Text("Name (EN)") }, singleLine = true)
                OutlinedTextField(nameHi, { nameHi = it }, label = { Text("Name (HI)") }, singleLine = true)
                OutlinedTextField(nameBn, { nameBn = it }, label = { Text("Name (BN)") }, singleLine = true)
                OutlinedTextField(brandName, { brandName = it }, label = { Text("Brand Name") }, singleLine = true)
                OutlinedTextField(genericEn, { genericEn = it }, label = { Text("Generic Name") }, singleLine = true)
                OutlinedTextField(salt, { salt = it }, label = { Text("Salt Composition") }, singleLine = true)
                OutlinedTextField(categoryId, { categoryId = it }, label = { Text("Category ID") }, singleLine = true)
                OutlinedTextField(mfrDetailsEn, { mfrDetailsEn = it }, label = { Text("Manufacturer Details") }, singleLine = true)
                OutlinedTextField(imageUrl, { imageUrl = it }, label = { Text("Image URL / Selected Photo") }, singleLine = true)
                OutlinedButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.PhotoLibrary, contentDescription = null)
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(strings.pickImageBtn)
                }
                OutlinedTextField(usedForEn, { usedForEn = it }, label = { Text("Used For") })
                OutlinedTextField(commonSeEn, { commonSeEn = it }, label = { Text("Common Side Effects") })
                OutlinedTextField(seriousSeEn, { seriousSeEn = it }, label = { Text("Serious Side Effects") })
                OutlinedTextField(warningsEn, { warningsEn = it }, label = { Text("Warnings & Precautions") })
                OutlinedTextField(interactionsEn, { interactionsEn = it }, label = { Text("Drug Interactions") })
                OutlinedTextField(sources, { sources = it }, label = { Text("Official Sources") }, singleLine = true)
                OutlinedTextField(verifiedDate, { verifiedDate = it }, label = { Text("Last Verified Date") }, singleLine = true)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = rxRequired, onCheckedChange = { rxRequired = it })
                    Text(strings.fieldPrescriptionRequired)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val baseMono = initial?.monograph ?: MedicineMonograph()
                    val safeNameEn = nameEn.ifBlank { brandName.ifBlank { "Medicine" } }
                    val safeGeneric = genericEn.ifBlank { safeNameEn }
                    val med = Medicine(
                        id = initial?.id.orEmpty(),
                        name = LocalizedText(safeNameEn, nameHi.ifBlank { safeNameEn }, nameBn.ifBlank { safeNameEn }),
                        brandName = brandName.ifBlank { safeNameEn },
                        genericName = LocalizedText(safeGeneric, initial?.genericName?.hi ?: safeGeneric, initial?.genericName?.bn ?: safeGeneric),
                        saltComposition = salt.ifBlank { safeGeneric },
                        categoryId = categoryId.ifBlank { "fever_pain" },
                        manufacturerId = manufacturerId.ifBlank { "mfr_bppi" },
                        manufacturerDetails = LocalizedText(mfrDetailsEn.ifBlank { "Verified GMP Manufacturer" }, mfrDetailsEn.ifBlank { "Verified GMP Manufacturer" }, mfrDetailsEn.ifBlank { "Verified GMP Manufacturer" }),
                        imageUrl = imageUrl,
                        monograph = baseMono.copy(
                            usedFor = baseMono.usedFor.copy(en = usedForEn.ifBlank { "Refer to official label" }),
                            usesAndBenefits = baseMono.usesAndBenefits.copy(en = baseMono.usesAndBenefits.en.ifBlank { usedForEn.ifBlank { "Refer to official label" } }),
                            howItWorks = baseMono.howItWorks.copy(en = baseMono.howItWorks.en.ifBlank { "Refer to official pharmacology label" }),
                            commonSideEffects = baseMono.commonSideEffects.copy(en = commonSeEn.ifBlank { "Mild GI discomfort possible" }),
                            seriousSideEffects = baseMono.seriousSideEffects.copy(en = seriousSeEn.ifBlank { "Seek urgent medical care for severe allergic reactions" }),
                            warnings = baseMono.warnings.copy(en = warningsEn.ifBlank { "Consult qualified physician before use" }),
                            drugInteractions = baseMono.drugInteractions.copy(en = interactionsEn.ifBlank { "Check with pharmacist" }),
                            contraindications = baseMono.contraindications.copy(en = baseMono.contraindications.en.ifBlank { "Hypersensitivity to active salt" }),
                            pregnancyWarnings = baseMono.pregnancyWarnings.copy(en = baseMono.pregnancyWarnings.en.ifBlank { "Consult obstetrician before use" }),
                            storageInstructions = baseMono.storageInstructions.copy(en = baseMono.storageInstructions.en.ifBlank { "Store below 30°C away from children" })
                        ),
                        prescriptionRequired = rxRequired,
                        officialSources = sources.ifBlank { "CDSCO & Official Label" },
                        lastVerifiedDate = verifiedDate.ifBlank { "2026-11-24" },
                        openFdaGenericQuery = initial?.openFdaGenericQuery ?: safeGeneric,
                        popularScore = initial?.popularScore ?: 500L,
                        published = true
                    )
                    onSave(med)
                },
                modifier = Modifier.testTag("admin_save_medicine_btn")
            ) {
                Text(strings.saveText)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(strings.cancelText) } }
    )
}

@Composable
fun AdminPriceEditorDialog(
    initial: PriceOffer?,
    onDismiss: () -> Unit,
    onSave: (PriceOffer) -> Unit
) {
    val strings = LocalStrings.current
    var medicineId by remember { mutableStateOf(initial?.medicineId ?: "paracetamol_500") }
    var brandName by remember { mutableStateOf(initial?.brandName ?: "") }
    var genericName by remember { mutableStateOf(initial?.genericName ?: "") }
    var salt by remember { mutableStateOf(initial?.saltComposition ?: "") }
    var packSize by remember { mutableStateOf(initial?.packSize ?: "10 Tablets") }
    var unitCountText by remember { mutableStateOf((initial?.unitCount ?: 10L).toString()) }
    var mrpText by remember { mutableStateOf((initial?.mrpInr ?: 10.0).toString()) }
    var sellingText by remember { mutableStateOf((initial?.sellingPriceInr ?: 10.0).toString()) }
    var mfrName by remember { mutableStateOf(initial?.manufacturerName ?: "") }
    var sourceName by remember { mutableStateOf(initial?.sourceName ?: "PMBJP Jan Aushadhi / NPPA") }
    var genericOption by remember { mutableStateOf(initial?.genericOption ?: false) }
    var avail by remember { mutableStateOf(initial?.availabilityStatus() ?: AvailabilityStatus.IN_STOCK) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) strings.addPriceOfferBtn else "Edit Verified Price") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(medicineId, { medicineId = it }, label = { Text("Medicine ID") }, singleLine = true)
                OutlinedTextField(brandName, { brandName = it }, label = { Text("Brand / Jan Aushadhi Name") }, singleLine = true)
                OutlinedTextField(genericName, { genericName = it }, label = { Text("Generic Name") }, singleLine = true)
                OutlinedTextField(salt, { salt = it }, label = { Text("Salt Composition") }, singleLine = true)
                OutlinedTextField(packSize, { packSize = it }, label = { Text("Pack Size") }, singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(unitCountText, { unitCountText = it }, label = { Text("Units") }, singleLine = true, modifier = Modifier.weight(1f))
                    OutlinedTextField(mrpText, { mrpText = it }, label = { Text("MRP ₹") }, singleLine = true, modifier = Modifier.weight(1f))
                    OutlinedTextField(sellingText, { sellingText = it }, label = { Text("Selling ₹") }, singleLine = true, modifier = Modifier.weight(1f))
                }
                OutlinedTextField(mfrName, { mfrName = it }, label = { Text("Manufacturer") }, singleLine = true)
                OutlinedTextField(sourceName, { sourceName = it }, label = { Text("Verified Source") }, singleLine = true)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = genericOption, onCheckedChange = { genericOption = it })
                    Text(strings.genericBadge)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    AvailabilityStatus.entries.forEach { st ->
                        FilterChip(
                            selected = avail == st,
                            onClick = { avail = st },
                            label = { Text(st.name.replace("_", " ")) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val units = unitCountText.toLongOrNull()?.coerceAtLeast(1L) ?: 10L
                    val mrp = mrpText.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
                    val selling = sellingText.toDoubleOrNull()?.coerceAtLeast(0.0) ?: mrp
                    val perUnit = selling / units.toDouble()
                    val discount = if (mrp > 0 && mrp >= selling) {
                        (((mrp - selling) / mrp) * 100).toLong().coerceIn(0L, 100L)
                    } else 0L
                    onSave(
                        PriceOffer(
                            id = initial?.id.orEmpty(),
                            medicineId = medicineId.ifBlank { "paracetamol_500" },
                            saltComposition = salt.ifBlank { genericName.ifBlank { "Verified Salt" } },
                            brandName = brandName.ifBlank { "Verified Medicine" },
                            genericName = genericName.ifBlank { brandName },
                            genericOption = genericOption,
                            packSize = packSize.ifBlank { "10 Tablets" },
                            unitCount = units,
                            mrpInr = mrp,
                            sellingPriceInr = selling,
                            pricePerUnitInr = perUnit,
                            discountPercent = discount,
                            manufacturerName = mfrName.ifBlank { "Verified GMP Manufacturer" },
                            availability = avail.name,
                            sourceName = sourceName.ifBlank { "NPPA / Official MRP" },
                            lastUpdatedDate = "2026-11-24",
                            published = true
                        )
                    )
                },
                modifier = Modifier.testTag("admin_save_price_btn")
            ) {
                Text(strings.saveText)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(strings.cancelText) } }
    )
}

@Composable
fun AdminCatalogMetaDialogs(
    showCategoryDialog: Boolean,
    showManufacturerDialog: Boolean,
    showTranslationDialog: Boolean,
    showApiDialog: Boolean,
    onDismissAll: () -> Unit,
    onSaveCategory: (Category) -> Unit,
    onSaveManufacturer: (Manufacturer) -> Unit,
    onSaveTranslation: (TranslationOverride) -> Unit,
    onSaveApi: (ApiConnection) -> Unit
) {
    val strings = LocalStrings.current

    if (showCategoryDialog) {
        var nameEn by remember { mutableStateOf("") }
        var nameHi by remember { mutableStateOf("") }
        var nameBn by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = onDismissAll,
            title = { Text(strings.addCategoryBtn) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(nameEn, { nameEn = it }, label = { Text("Category (EN)") }, singleLine = true)
                    OutlinedTextField(nameHi, { nameHi = it }, label = { Text("Category (HI)") }, singleLine = true)
                    OutlinedTextField(nameBn, { nameBn = it }, label = { Text("Category (BN)") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(onClick = {
                    val en = nameEn.ifBlank { "General Health" }
                    onSaveCategory(Category("", en, nameHi.ifBlank { en }, nameBn.ifBlank { en }, "medication", 20L, true))
                    onDismissAll()
                }) { Text(strings.saveText) }
            },
            dismissButton = { TextButton(onClick = onDismissAll) { Text(strings.cancelText) } }
        )
    }

    if (showManufacturerDialog) {
        var name by remember { mutableStateOf("") }
        var country by remember { mutableStateOf("India") }
        var license by remember { mutableStateOf("CDSCO GMP Certified") }
        AlertDialog(
            onDismissRequest = onDismissAll,
            title = { Text(strings.addManufacturerBtn) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(name, { name = it }, label = { Text("Manufacturer Name") }, singleLine = true)
                    OutlinedTextField(country, { country = it }, label = { Text("Country") }, singleLine = true)
                    OutlinedTextField(license, { license = it }, label = { Text("License Info") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(onClick = {
                    onSaveManufacturer(Manufacturer("", name.ifBlank { "Verified Lab" }, country.ifBlank { "India" }, license, true))
                    onDismissAll()
                }) { Text(strings.saveText) }
            },
            dismissButton = { TextButton(onClick = onDismissAll) { Text(strings.cancelText) } }
        )
    }

    if (showTranslationDialog) {
        var key by remember { mutableStateOf("app_tagline") }
        var en by remember { mutableStateOf(strings.appTagline) }
        var hi by remember { mutableStateOf("") }
        var bn by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = onDismissAll,
            title = { Text(strings.addTranslationBtn) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(key, { key = it }, label = { Text("Key (e.g. app_tagline)") }, singleLine = true)
                    OutlinedTextField(en, { en = it }, label = { Text("English") })
                    OutlinedTextField(hi, { hi = it }, label = { Text("Hindi (हिन्दी)") })
                    OutlinedTextField(bn, { bn = it }, label = { Text("Bengali (বাংলা)") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    val safeEn = en.ifBlank { strings.appTagline }
                    onSaveTranslation(TranslationOverride("", key.ifBlank { "app_tagline" }, safeEn, hi.ifBlank { safeEn }, bn.ifBlank { safeEn }, true))
                    onDismissAll()
                }) { Text(strings.saveText) }
            },
            dismissButton = { TextButton(onClick = onDismissAll) { Text(strings.cancelText) } }
        )
    }

    if (showApiDialog) {
        var name by remember { mutableStateOf("U.S. FDA OpenFDA Endpoint") }
        var baseUrl by remember { mutableStateOf("https://api.fda.gov/drug/label.json") }
        var providerType by remember { mutableStateOf("OPENFDA_REST") }
        AlertDialog(
            onDismissRequest = onDismissAll,
            title = { Text(strings.apiConnectionsTitle) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(name, { name = it }, label = { Text("Connection Name") }, singleLine = true)
                    OutlinedTextField(baseUrl, { baseUrl = it }, label = { Text("HTTPS Base URL") }, singleLine = true)
                    OutlinedTextField(providerType, { providerType = it }, label = { Text("Provider Type") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(onClick = {
                    onSaveApi(ApiConnection("", name.ifBlank { "API Endpoint" }, baseUrl, providerType.ifBlank { "CUSTOM_HTTPS" }, true))
                    onDismissAll()
                }) { Text(strings.saveText) }
            },
            dismissButton = { TextButton(onClick = onDismissAll) { Text(strings.cancelText) } }
        )
    }
}

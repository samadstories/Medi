package com.example.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.data.model.Category
import com.example.data.model.Medicine
import com.example.data.model.PriceOffer
import com.example.data.model.SearchHistoryEntry
import com.example.i18n.AppLanguage
import com.example.i18n.LocalStrings
import com.example.ui.components.EmptyStatePanel
import com.example.ui.components.MedicalSafetyBanner
import com.example.ui.components.MedicineSummaryCard
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MedicineSearchScreen(
    language: AppLanguage,
    query: String,
    selectedCategoryId: String?,
    medicines: List<Medicine>,
    filteredMedicines: List<Medicine>,
    categories: List<Category>,
    priceOffers: List<PriceOffer>,
    favoriteIds: Set<String>,
    searchHistory: List<SearchHistoryEntry>,
    onQueryChange: (String) -> Unit,
    onSubmitSearch: (String, String) -> Unit,
    onSelectCategory: (String?) -> Unit,
    onClearHistory: () -> Unit,
    onOpenScanner: () -> Unit,
    onOpenMedicineDetail: (String) -> Unit,
    onOpenPriceCompare: (String?) -> Unit,
    onToggleFavorite: (Medicine, Boolean) -> Unit,
    onBack: () -> Unit
) {
    val strings = LocalStrings.current
    val context = LocalContext.current

    val voiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spoken = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
                ?.trim()
            if (!spoken.isNullOrBlank()) {
                onQueryChange(spoken)
                onSubmitSearch(spoken, "VOICE")
            }
        }
    }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    when (language) {
                        AppLanguage.EN -> Locale.ENGLISH.toLanguageTag()
                        AppLanguage.HI -> "hi-IN"
                        AppLanguage.BN -> "bn-IN"
                    }
                )
                putExtra(RecognizerIntent.EXTRA_PROMPT, strings.searchVoice)
            }
            runCatching { voiceLauncher.launch(speechIntent) }
                .onFailure {
                    Toast.makeText(context, "Speech service unavailable on this device", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, strings.micPermissionNeeded, Toast.LENGTH_SHORT).show()
        }
    }

    fun startVoiceSearch() {
        val hasMic = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        if (hasMic) {
            val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(RecognizerIntent.EXTRA_PROMPT, strings.searchVoice)
            }
            runCatching { voiceLauncher.launch(speechIntent) }
                .onFailure {
                    Toast.makeText(context, "Speech service unavailable on this device", Toast.LENGTH_SHORT).show()
                }
        } else {
            micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    val lowestPriceByMedicine = remember(priceOffers) {
        priceOffers.groupBy { it.medicineId }
            .mapValues { (_, list) -> list.minOfOrNull { it.pricePerUnitInr } }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.navSearch) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("search_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onOpenScanner, modifier = Modifier.testTag("search_scan_action")) {
                        Icon(Icons.Filled.CameraAlt, contentDescription = strings.searchScan)
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
            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    placeholder = { Text(strings.searchPlaceholder) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    trailingIcon = {
                        Row {
                            if (query.isNotBlank()) {
                                IconButton(
                                    onClick = { onQueryChange("") },
                                    modifier = Modifier.testTag("search_clear_input")
                                ) {
                                    Icon(Icons.Filled.Clear, contentDescription = strings.clearHistory)
                                }
                            }
                            IconButton(
                                onClick = ::startVoiceSearch,
                                modifier = Modifier.testTag("search_voice_button")
                            ) {
                                Icon(Icons.Filled.Mic, contentDescription = strings.searchVoice)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_query_field")
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = strings.searchByLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Category Filter Chips
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedCategoryId == null,
                            onClick = { onSelectCategory(null) },
                            label = { Text(strings.viewAll) },
                            modifier = Modifier.testTag("filter_cat_all")
                        )
                    }
                    items(categories, key = { it.id }) { cat ->
                        FilterChip(
                            selected = selectedCategoryId == cat.id,
                            onClick = {
                                onSelectCategory(if (selectedCategoryId == cat.id) null else cat.id)
                            },
                            label = { Text(cat.localizedName(language)) },
                            modifier = Modifier.testTag("filter_cat_${cat.id}")
                        )
                    }
                }
            }

            // Search History Chips
            if (searchHistory.isNotEmpty() && query.isBlank()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = strings.searchHistory,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(
                            onClick = onClearHistory,
                            modifier = Modifier.testTag("clear_search_history_btn")
                        ) {
                            Text(strings.clearHistory)
                        }
                    }
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        searchHistory.take(8).forEach { entry ->
                            AssistChip(
                                onClick = { onQueryChange(entry.query) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.History,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                label = { Text(entry.query) }
                            )
                        }
                    }
                }
            }

            // Results
            if (filteredMedicines.isEmpty()) {
                item {
                    EmptyStatePanel(message = strings.noResults, icon = Icons.Filled.Search)
                }
            } else {
                items(filteredMedicines, key = { it.id }) { med ->
                    val isFav = med.id in favoriteIds
                    MedicineSummaryCard(
                        medicine = med,
                        language = language,
                        isFavorite = isFav,
                        lowestPriceInr = lowestPriceByMedicine[med.id],
                        onClick = {
                            if (query.isNotBlank()) onSubmitSearch(query, "TEXT")
                            onOpenMedicineDetail(med.id)
                        },
                        onToggleFavorite = { onToggleFavorite(med, isFav) },
                        onCompareClick = { onOpenPriceCompare(med.id) }
                    )
                }
            }

            item { MedicalSafetyBanner() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineScannerScreen(
    language: AppLanguage,
    medicines: List<Medicine>,
    findBestMatch: (String, List<Medicine>) -> Medicine?,
    onScanMatchedQuery: (String) -> Unit,
    onOpenMedicineDetail: (String) -> Unit,
    onBack: () -> Unit
) {
    val strings = LocalStrings.current
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    var recognizedText by remember { mutableStateOf("") }
    var matchedMedicine by remember { mutableStateOf<Medicine?>(null) }
    var scanningBusy by remember { mutableStateOf(false) }

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }
    val textRecognizer = remember {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    DisposableEffect(Unit) {
        onDispose { textRecognizer.close() }
    }

    fun captureAndRecognize() {
        scanningBusy = true
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val inputImage = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees
                        )
                        textRecognizer.process(inputImage)
                            .addOnSuccessListener { visionText ->
                                val raw = visionText.text.trim()
                                recognizedText = raw
                                matchedMedicine = findBestMatch(raw, medicines)
                                scanningBusy = false
                                imageProxy.close()
                            }
                            .addOnFailureListener {
                                scanningBusy = false
                                imageProxy.close()
                            }
                    } else {
                        scanningBusy = false
                        imageProxy.close()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    scanningBusy = false
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.scannerTitle) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("scanner_back_button")) {
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = strings.scannerHint,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (!hasCameraPermission) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(strings.cameraPermissionNeeded, style = MaterialTheme.typography.bodyMedium)
                        Button(
                            onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                            modifier = Modifier.testTag("grant_camera_permission_btn")
                        ) {
                            Text(strings.grantPermission)
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    AndroidView(
                        factory = { ctx ->
                            val previewView = PreviewView(ctx)
                            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                            cameraProviderFuture.addListener({
                                val provider = cameraProviderFuture.get()
                                val preview = Preview.Builder().build().also {
                                    it.surfaceProvider = previewView.surfaceProvider
                                }
                                runCatching {
                                    provider.unbindAll()
                                    provider.bindToLifecycle(
                                        lifecycleOwner,
                                        CameraSelector.DEFAULT_BACK_CAMERA,
                                        preview,
                                        imageCapture
                                    )
                                }
                            }, ContextCompat.getMainExecutor(ctx))
                            previewView
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Button(
                    onClick = ::captureAndRecognize,
                    enabled = !scanningBusy,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("scan_capture_button")
                ) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(if (scanningBusy) "Scanning..." else strings.scanCaptureBtn)
                }
            }

            if (recognizedText.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Recognized Label Text:",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = recognizedText.take(260),
                            style = MaterialTheme.typography.bodySmall
                        )
                        val match = matchedMedicine
                        if (match != null) {
                            Button(
                                onClick = { onOpenMedicineDetail(match.id) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("scanner_open_matched_med")
                            ) {
                                Text("Open Match: ${match.name.get(language)}")
                            }
                        } else {
                            OutlinedButton(
                                onClick = {
                                    val firstLine = recognizedText.lineSequence().firstOrNull()?.take(40).orEmpty()
                                    onScanMatchedQuery(firstLine)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Search Catalog with Scanned Text")
                            }
                        }
                    }
                }
            }

            MedicalSafetyBanner()
        }
    }
}

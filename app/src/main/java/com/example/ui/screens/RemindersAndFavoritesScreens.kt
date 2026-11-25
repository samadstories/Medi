package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAlarm
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.data.model.FavoriteMedicine
import com.example.data.model.Medicine
import com.example.data.model.MedicineReminder
import com.example.data.model.PriceOffer
import com.example.data.model.ScheduleSlot
import com.example.i18n.AppLanguage
import com.example.i18n.LocalStrings
import com.example.reminders.ReminderScheduler
import com.example.ui.components.EmptyStatePanel
import com.example.ui.components.MedicalSafetyBanner
import com.example.ui.components.MedicineSummaryCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RemindersScreen(
    language: AppLanguage,
    medicines: List<Medicine>,
    reminders: List<MedicineReminder>,
    prefillMedicineId: String?,
    onSaveReminder: (MedicineReminder) -> Unit,
    onDeleteReminder: (String) -> Unit,
    onBack: (() -> Unit)? = null
) {
    val strings = LocalStrings.current
    val context = LocalContext.current

    LaunchedEffect(reminders) {
        ReminderScheduler.syncReminders(context, reminders)
    }

    var hasNotifPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val notifPermLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> hasNotifPermission = granted }

    var showDialog by remember(prefillMedicineId) { mutableStateOf(prefillMedicineId != null) }
    var editingReminder by remember { mutableStateOf<MedicineReminder?>(null) }

    fun slotLabel(slot: ScheduleSlot): String = when (slot) {
        ScheduleSlot.MORNING -> strings.slotMorning
        ScheduleSlot.AFTERNOON -> strings.slotAfternoon
        ScheduleSlot.EVENING -> strings.slotEvening
        ScheduleSlot.NIGHT -> strings.slotNight
        ScheduleSlot.CUSTOM -> strings.slotCustom
    }

    val groupedReminders = remember(reminders) {
        ScheduleSlot.entries.associateWith { slot -> reminders.filter { it.slot() == slot } }
            .filterValues { it.isNotEmpty() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.dosageOrganizerTitle) },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("reminders_back_button")) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingReminder = null
                    showDialog = true
                },
                modifier = Modifier.testTag("add_reminder_fab")
            ) {
                Icon(Icons.Filled.AddAlarm, contentDescription = strings.addNewReminder)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!hasNotifPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(strings.notificationPermissionNeeded, style = MaterialTheme.typography.bodySmall)
                            Button(
                                onClick = { notifPermLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                                modifier = Modifier.testTag("grant_notification_permission_btn")
                            ) {
                                Text(strings.grantPermission)
                            }
                        }
                    }
                }
            }

            item { MedicalSafetyBanner() }

            if (reminders.isEmpty()) {
                item {
                    EmptyStatePanel(
                        message = strings.noRemindersYet,
                        icon = Icons.Filled.NotificationsActive
                    )
                }
            } else {
                groupedReminders.forEach { (slot, slotItems) ->
                    item(key = "slot_${slot.name}") {
                        Text(
                            text = slotLabel(slot),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    items(slotItems, key = { it.id }) { rem ->
                        Card(
                            onClick = {
                                editingReminder = rem
                                showDialog = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reminder_card_${rem.id}"),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.55f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        Icons.Filled.Schedule,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Column {
                                        Text(
                                            text = rem.medicineName,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${"%02d:%02d".format(rem.timeHour, rem.timeMinute)} • ${rem.dosage}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        if (rem.instructions.isNotBlank()) {
                                            Text(
                                                text = rem.instructions,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Switch(
                                        checked = rem.active,
                                        onCheckedChange = { active ->
                                            onSaveReminder(rem.copy(active = active))
                                        },
                                        modifier = Modifier.testTag("reminder_switch_${rem.id}")
                                    )
                                    IconButton(
                                        onClick = {
                                            ReminderScheduler.cancel(context, rem.id)
                                            onDeleteReminder(rem.id)
                                        },
                                        modifier = Modifier.testTag("reminder_delete_${rem.id}")
                                    ) {
                                        Icon(
                                            Icons.Filled.DeleteOutline,
                                            contentDescription = strings.deleteText,
                                            tint = MaterialTheme.colorScheme.error
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

    if (showDialog) {
        val initialMed = medicines.firstOrNull { it.id == (editingReminder?.medicineId ?: prefillMedicineId) }
        var selectedMed by remember(editingReminder, initialMed) { mutableStateOf(initialMed) }
        var medNameInput by remember(editingReminder, initialMed) {
            mutableStateOf(editingReminder?.medicineName ?: initialMed?.name?.get(language) ?: "")
        }
        var dosageInput by remember(editingReminder) {
            mutableStateOf(editingReminder?.dosage ?: "1 tablet (as prescribed)")
        }
        var instructionsInput by remember(editingReminder) {
            mutableStateOf(editingReminder?.instructions ?: "After meal")
        }
        var selectedSlot by remember(editingReminder) {
            mutableStateOf(editingReminder?.slot() ?: ScheduleSlot.MORNING)
        }
        var hourInput by remember(editingReminder) {
            mutableIntStateOf(editingReminder?.timeHour?.toInt() ?: selectedSlot.defaultHour)
        }
        var minuteInput by remember(editingReminder) {
            mutableIntStateOf(editingReminder?.timeMinute?.toInt() ?: selectedSlot.defaultMinute)
        }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(strings.addNewReminder) },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MedicineDropdownPicker(
                        label = strings.fieldMedicineName,
                        selected = selectedMed,
                        medicines = medicines,
                        language = language,
                        onSelect = {
                            selectedMed = it
                            medNameInput = it.name.get(language)
                        },
                        testTag = "reminder_med_picker"
                    )
                    OutlinedTextField(
                        value = medNameInput,
                        onValueChange = { medNameInput = it },
                        label = { Text(strings.fieldMedicineName) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reminder_name_input")
                    )
                    OutlinedTextField(
                        value = dosageInput,
                        onValueChange = { dosageInput = it },
                        label = { Text(strings.dosageHint) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reminder_dosage_input")
                    )
                    OutlinedTextField(
                        value = instructionsInput,
                        onValueChange = { instructionsInput = it },
                        label = { Text(strings.instructionsHint) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reminder_instructions_input")
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ScheduleSlot.entries.forEach { slot ->
                            FilterChip(
                                selected = selectedSlot == slot,
                                onClick = {
                                    selectedSlot = slot
                                    hourInput = slot.defaultHour
                                    minuteInput = slot.defaultMinute
                                },
                                label = { Text(slotLabel(slot)) }
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = hourInput.toString(),
                            onValueChange = { txt ->
                                hourInput = txt.toIntOrNull()?.coerceIn(0, 23) ?: 0
                            },
                            label = { Text("Hour (0-23)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = minuteInput.toString(),
                            onValueChange = { txt ->
                                minuteInput = txt.toIntOrNull()?.coerceIn(0, 59) ?: 0
                            },
                            label = { Text("Min (0-59)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val reminder = MedicineReminder(
                            id = editingReminder?.id.orEmpty(),
                            medicineId = selectedMed?.id ?: "custom_med",
                            medicineName = medNameInput.ifBlank { "Medicine" },
                            dosage = dosageInput.ifBlank { "As prescribed" },
                            scheduleSlot = selectedSlot.name,
                            timeHour = hourInput.toLong(),
                            timeMinute = minuteInput.toLong(),
                            instructions = instructionsInput,
                            active = true
                        )
                        onSaveReminder(reminder)
                        showDialog = false
                    },
                    modifier = Modifier.testTag("reminder_save_button")
                ) {
                    Text(strings.saveText)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(strings.cancelText)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    language: AppLanguage,
    favorites: List<FavoriteMedicine>,
    medicines: List<Medicine>,
    priceOffers: List<PriceOffer>,
    onOpenMedicineDetail: (String) -> Unit,
    onOpenPriceCompare: (String) -> Unit,
    onToggleFavorite: (Medicine, Boolean) -> Unit,
    onBack: () -> Unit
) {
    val strings = LocalStrings.current
    val medById = remember(medicines) { medicines.associateBy { it.id } }
    val lowestPriceByMedicine = remember(priceOffers) {
        priceOffers.groupBy { it.medicineId }
            .mapValues { (_, list) -> list.minOfOrNull { it.pricePerUnitInr } }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.favorites) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("favorites_back_button")) {
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (favorites.isEmpty()) {
                item {
                    EmptyStatePanel(
                        message = strings.noFavoritesYet,
                        icon = Icons.Filled.Bookmark
                    )
                }
            } else {
                items(favorites, key = { it.medicineId }) { fav ->
                    val med = medById[fav.medicineId]
                    if (med != null) {
                        MedicineSummaryCard(
                            medicine = med,
                            language = language,
                            isFavorite = true,
                            lowestPriceInr = lowestPriceByMedicine[med.id],
                            onClick = { onOpenMedicineDetail(med.id) },
                            onToggleFavorite = { onToggleFavorite(med, true) },
                            onCompareClick = { onOpenPriceCompare(med.id) }
                        )
                    } else {
                        OutlinedButton(
                            onClick = { onOpenMedicineDetail(fav.medicineId) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(fav.medicineName)
                        }
                    }
                }
            }
        }
    }
}

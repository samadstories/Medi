package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.AnalyticsEvent
import com.example.data.model.ApiConnection
import com.example.data.model.Category
import com.example.data.model.Manufacturer
import com.example.data.model.Medicine
import com.example.data.model.PriceOffer
import com.example.data.model.TranslationOverride
import com.example.data.remote.OpenFdaClient
import com.example.data.repository.CatalogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class AdminViewModel(
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    val apiConnections: StateFlow<List<ApiConnection>> = catalogRepository.observeApiConnections()
        .catch { e ->
            if (e is CancellationException) throw e
            emit(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val analyticsEvents: StateFlow<List<AnalyticsEvent>> = catalogRepository.observeAnalyticsEvents()
        .catch { e ->
            if (e is CancellationException) throw e
            emit(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _adminBanner = MutableStateFlow<String?>(null)
    val adminBanner: StateFlow<String?> = _adminBanner.asStateFlow()

    private val _busy = MutableStateFlow(false)
    val busy: StateFlow<Boolean> = _busy.asStateFlow()

    fun dismissBanner() {
        _adminBanner.value = null
    }

    fun seedVerifiedCatalog() {
        viewModelScope.launch {
            _busy.value = true
            catalogRepository.seedVerifiedCatalogToFirestore()
                .onSuccess { count ->
                    _adminBanner.value = "Synced $count verified catalog documents to Cloud Firestore."
                }
                .onFailure { err ->
                    _adminBanner.value = "Sync failed: ${err.message}"
                }
            _busy.value = false
        }
    }

    fun saveMedicine(medicine: Medicine) {
        viewModelScope.launch {
            _busy.value = true
            catalogRepository.saveMedicine(medicine)
                .onSuccess { _adminBanner.value = "Saved medicine: ${medicine.brandName}" }
                .onFailure { _adminBanner.value = "Failed: ${it.message}" }
            _busy.value = false
        }
    }

    fun deleteMedicine(medicineId: String) {
        viewModelScope.launch {
            catalogRepository.deleteMedicine(medicineId)
                .onSuccess { _adminBanner.value = "Deleted medicine $medicineId" }
                .onFailure { _adminBanner.value = "Delete failed: ${it.message}" }
        }
    }

    fun savePriceOffer(offer: PriceOffer) {
        viewModelScope.launch {
            _busy.value = true
            catalogRepository.savePriceOffer(offer)
                .onSuccess { _adminBanner.value = "Saved verified price: ${offer.brandName}" }
                .onFailure { _adminBanner.value = "Failed: ${it.message}" }
            _busy.value = false
        }
    }

    fun deletePriceOffer(offerId: String) {
        viewModelScope.launch {
            catalogRepository.deletePriceOffer(offerId)
                .onSuccess { _adminBanner.value = "Removed price entry $offerId" }
                .onFailure { _adminBanner.value = "Delete failed: ${it.message}" }
        }
    }

    fun saveCategory(category: Category) {
        viewModelScope.launch {
            catalogRepository.saveCategory(category)
                .onSuccess { _adminBanner.value = "Saved category: ${category.nameEn}" }
                .onFailure { _adminBanner.value = "Failed: ${it.message}" }
        }
    }

    fun saveManufacturer(manufacturer: Manufacturer) {
        viewModelScope.launch {
            catalogRepository.saveManufacturer(manufacturer)
                .onSuccess { _adminBanner.value = "Saved manufacturer: ${manufacturer.name}" }
                .onFailure { _adminBanner.value = "Failed: ${it.message}" }
        }
    }

    fun saveTranslation(overrideItem: TranslationOverride) {
        viewModelScope.launch {
            catalogRepository.saveTranslationOverride(overrideItem)
                .onSuccess { _adminBanner.value = "Updated translation key: ${overrideItem.key}" }
                .onFailure { _adminBanner.value = "Failed: ${it.message}" }
        }
    }

    fun saveAndTestApiConnection(conn: ApiConnection) {
        viewModelScope.launch {
            _busy.value = true
            val pingResult = OpenFdaClient.testHttpsEndpoint(conn.baseUrl)
            val statusMsg = pingResult.fold(
                onSuccess = { "Verified live: $it" },
                onFailure = { "Check warning: ${it.message}" }
            )
            val updated = conn.copy(statusMessage = statusMsg)
            catalogRepository.saveApiConnection(updated)
                .onSuccess { _adminBanner.value = "${conn.name} — $statusMsg" }
                .onFailure { _adminBanner.value = "${conn.name} — $statusMsg" }
            _busy.value = false
        }
    }

    class Factory(private val repo: CatalogRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AdminViewModel(repo) as T
        }
    }
}

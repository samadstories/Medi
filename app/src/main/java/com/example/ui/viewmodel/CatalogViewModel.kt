package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.Category
import com.example.data.model.Manufacturer
import com.example.data.model.Medicine
import com.example.data.model.PriceOffer
import com.example.data.model.TranslationOverride
import com.example.data.model.UiState
import com.example.data.remote.FdaMonographSnippet
import com.example.data.remote.OpenFdaClient
import com.example.data.repository.CatalogRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModel(
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    private val isAdminFlow = MutableStateFlow(false)

    fun setAdminMode(isAdmin: Boolean) {
        if (isAdminFlow.value != isAdmin) {
            isAdminFlow.value = isAdmin
        }
    }

    val medicinesState: StateFlow<UiState<List<Medicine>>> = isAdminFlow
        .flatMapLatest { admin ->
            catalogRepository.observeMedicines(admin)
                .map<List<Medicine>, UiState<List<Medicine>>> { UiState.Success(it) }
                .catch { e ->
                    if (e is CancellationException) throw e
                    emit(UiState.Error(e.message ?: "Unable to load verified medicines"))
                }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    val priceOffersState: StateFlow<UiState<List<PriceOffer>>> = isAdminFlow
        .flatMapLatest { admin ->
            catalogRepository.observePriceOffers(admin)
                .map<List<PriceOffer>, UiState<List<PriceOffer>>> { UiState.Success(it) }
                .catch { e ->
                    if (e is CancellationException) throw e
                    emit(UiState.Error(e.message ?: "Unable to load verified prices"))
                }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    val categoriesState: StateFlow<UiState<List<Category>>> = isAdminFlow
        .flatMapLatest { admin ->
            catalogRepository.observeCategories(admin)
                .map<List<Category>, UiState<List<Category>>> { UiState.Success(it) }
                .catch { e ->
                    if (e is CancellationException) throw e
                    emit(UiState.Error(e.message ?: "Unable to load categories"))
                }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    val manufacturersState: StateFlow<UiState<List<Manufacturer>>> = isAdminFlow
        .flatMapLatest { admin ->
            catalogRepository.observeManufacturers(admin)
                .map<List<Manufacturer>, UiState<List<Manufacturer>>> { UiState.Success(it) }
                .catch { e ->
                    if (e is CancellationException) throw e
                    emit(UiState.Error(e.message ?: "Unable to load manufacturers"))
                }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    val translationsState: StateFlow<List<TranslationOverride>> = isAdminFlow
        .flatMapLatest { admin ->
            catalogRepository.observeTranslations(admin)
                .catch { e ->
                    if (e is CancellationException) throw e
                    emit(emptyList())
                }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<String?>(null)
    val selectedCategoryId: StateFlow<String?> = _selectedCategoryId.asStateFlow()

    private val _fdaLiveState = MutableStateFlow<UiState<FdaMonographSnippet>?>(null)
    val fdaLiveState: StateFlow<UiState<FdaMonographSnippet>?> = _fdaLiveState.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(categoryId: String?) {
        _selectedCategoryId.value = categoryId
    }

    fun clearFdaLookup() {
        _fdaLiveState.value = null
    }

    fun lookupFdaLabel(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _fdaLiveState.value = UiState.Loading
            OpenFdaClient.fetchDrugLabel(query)
                .onSuccess { snippet -> _fdaLiveState.value = UiState.Success(snippet) }
                .onFailure { err ->
                    _fdaLiveState.value = UiState.Error(
                        err.message ?: "Could not reach official OpenFDA endpoint."
                    )
                }
        }
    }

    fun filterMedicines(
        medicines: List<Medicine>,
        categories: List<Category>,
        query: String,
        categoryId: String?
    ): List<Medicine> {
        val q = query.trim().lowercase()
        val catLookup = categories.associateBy { it.id }
        return medicines.filter { med ->
            val catMatch = categoryId == null || med.categoryId == categoryId
            if (!catMatch) return@filter false
            if (q.isBlank()) return@filter true
            val cat = catLookup[med.categoryId]
            val haystack = buildString {
                append(med.name.en).append(' ')
                append(med.name.hi).append(' ')
                append(med.name.bn).append(' ')
                append(med.brandName).append(' ')
                append(med.genericName.en).append(' ')
                append(med.genericName.hi).append(' ')
                append(med.genericName.bn).append(' ')
                append(med.saltComposition).append(' ')
                append(med.manufacturerDetails.en).append(' ')
                append(med.manufacturerDetails.hi).append(' ')
                append(med.manufacturerDetails.bn).append(' ')
                if (cat != null) {
                    append(cat.nameEn).append(' ')
                    append(cat.nameHi).append(' ')
                    append(cat.nameBn)
                }
            }.lowercase()
            haystack.contains(q)
        }
    }

    fun findBestMatchingMedicineFromScan(
        recognizedText: String,
        medicines: List<Medicine>
    ): Medicine? {
        val tokens = recognizedText.lowercase()
            .split(Regex("[^a-zA-Z0-9]+"))
            .filter { it.length >= 4 }
        if (tokens.isEmpty()) return null
        return medicines.maxByOrNull { med ->
            val text = "${med.name.en} ${med.brandName} ${med.genericName.en} ${med.saltComposition}".lowercase()
            tokens.count { token -> text.contains(token) }
        }?.takeIf { med ->
            val text = "${med.name.en} ${med.brandName} ${med.genericName.en} ${med.saltComposition}".lowercase()
            tokens.any { token -> text.contains(token) }
        }
    }

    class Factory(private val repo: CatalogRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatalogViewModel(repo) as T
        }
    }
}

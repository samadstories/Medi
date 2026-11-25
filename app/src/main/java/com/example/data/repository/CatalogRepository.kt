package com.example.data.repository

import com.example.data.model.AnalyticsEvent
import com.example.data.model.ApiConnection
import com.example.data.model.Category
import com.example.data.model.Manufacturer
import com.example.data.model.Medicine
import com.example.data.model.PriceOffer
import com.example.data.model.TranslationOverride
import com.example.data.seed.VerifiedReferenceCatalog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class CatalogRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private fun sanitizeId(raw: String): String {
        val cleaned = raw.trim().lowercase().replace(Regex("[^a-zA-Z0-9_\\-]"), "_")
        return cleaned.ifBlank { "item_${System.currentTimeMillis()}" }.take(120)
    }

    fun observeMedicines(isAdmin: Boolean): Flow<List<Medicine>> {
        val base = db.collection("medicines")
        val query = if (isAdmin) base else base.whereEqualTo("published", true)
        return query.snapshots()
            .map { snapshot ->
                val remote = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Medicine::class.java)
                }
                if (remote.isEmpty()) {
                    VerifiedReferenceCatalog.medicines
                } else {
                    val remoteIds = remote.map { it.id }.toSet()
                    val merged = remote + VerifiedReferenceCatalog.medicines.filter { it.id !in remoteIds }
                    (if (isAdmin) merged else merged.filter { it.published })
                        .sortedByDescending { it.popularScore }
                }
            }
            .catch { e ->
                if (e is CancellationException) throw e
                throw handleFirestoreError(e, OperationType.LIST, "medicines", auth)
            }
    }

    fun observePriceOffers(isAdmin: Boolean): Flow<List<PriceOffer>> {
        val base = db.collection("priceOffers")
        val query = if (isAdmin) base else base.whereEqualTo("published", true)
        return query.snapshots()
            .map { snapshot ->
                val remote = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(PriceOffer::class.java)
                }
                if (remote.isEmpty()) {
                    VerifiedReferenceCatalog.priceOffers
                } else {
                    val remoteIds = remote.map { it.id }.toSet()
                    val merged = remote + VerifiedReferenceCatalog.priceOffers.filter { it.id !in remoteIds }
                    (if (isAdmin) merged else merged.filter { it.published })
                        .sortedBy { it.pricePerUnitInr }
                }
            }
            .catch { e ->
                if (e is CancellationException) throw e
                throw handleFirestoreError(e, OperationType.LIST, "priceOffers", auth)
            }
    }

    fun observeCategories(isAdmin: Boolean): Flow<List<Category>> {
        val base = db.collection("categories")
        val query = if (isAdmin) base else base.whereEqualTo("published", true)
        return query.snapshots()
            .map { snapshot ->
                val remote = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Category::class.java)
                }
                if (remote.isEmpty()) {
                    VerifiedReferenceCatalog.categories
                } else {
                    val remoteIds = remote.map { it.id }.toSet()
                    val merged = remote + VerifiedReferenceCatalog.categories.filter { it.id !in remoteIds }
                    (if (isAdmin) merged else merged.filter { it.published })
                        .sortedBy { it.sortOrder }
                }
            }
            .catch { e ->
                if (e is CancellationException) throw e
                throw handleFirestoreError(e, OperationType.LIST, "categories", auth)
            }
    }

    fun observeManufacturers(isAdmin: Boolean): Flow<List<Manufacturer>> {
        val base = db.collection("manufacturers")
        val query = if (isAdmin) base else base.whereEqualTo("published", true)
        return query.snapshots()
            .map { snapshot ->
                val remote = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Manufacturer::class.java)
                }
                if (remote.isEmpty()) {
                    VerifiedReferenceCatalog.manufacturers
                } else {
                    val remoteIds = remote.map { it.id }.toSet()
                    remote + VerifiedReferenceCatalog.manufacturers.filter { it.id !in remoteIds }
                }
            }
            .catch { e ->
                if (e is CancellationException) throw e
                throw handleFirestoreError(e, OperationType.LIST, "manufacturers", auth)
            }
    }

    fun observeTranslations(isAdmin: Boolean): Flow<List<TranslationOverride>> {
        val base = db.collection("translations")
        val query = if (isAdmin) base else base.whereEqualTo("published", true)
        return query.snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { it.toObject(TranslationOverride::class.java) }
            }
            .catch { e ->
                if (e is CancellationException) throw e
                throw handleFirestoreError(e, OperationType.LIST, "translations", auth)
            }
    }

    fun observeApiConnections(): Flow<List<ApiConnection>> {
        return db.collection("apiConnections").snapshots()
            .map { snapshot ->
                val remote = snapshot.documents.mapNotNull { it.toObject(ApiConnection::class.java) }
                if (remote.isEmpty()) VerifiedReferenceCatalog.apiConnections else remote
            }
            .catch { e ->
                if (e is CancellationException) throw e
                throw handleFirestoreError(e, OperationType.LIST, "apiConnections", auth)
            }
    }

    fun observeAnalyticsEvents(): Flow<List<AnalyticsEvent>> {
        return db.collection("analyticsEvents").snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { doc ->
                    doc.toObject(AnalyticsEvent::class.java)?.let { event ->
                        val ts = doc.getTimestamp(
                            "createdAt",
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )
                        event.copy(createdAt = ts)
                    }
                }.sortedByDescending { it.createdAt?.seconds ?: 0L }
            }
            .catch { e ->
                if (e is CancellationException) throw e
                throw handleFirestoreError(e, OperationType.LIST, "analyticsEvents", auth)
            }
    }

    suspend fun saveMedicine(medicine: Medicine): Result<Unit> = runCatching {
        val docId = sanitizeId(medicine.id.ifBlank { medicine.brandName })
        val ref = db.collection("medicines").document(docId)
        val exists = ref.get().await().exists()
        if (exists) {
            ref.update(medicine.toWriteMap(isCreate = false)).await()
        } else {
            ref.set(medicine.toWriteMap(isCreate = true)).await()
        }
        Unit
    }.recoverCatching { e ->
        if (e is CancellationException) throw e
        throw handleFirestoreError(e, OperationType.WRITE, "medicines/${medicine.id}", auth)
    }

    suspend fun deleteMedicine(medicineId: String): Result<Unit> = runCatching {
        db.collection("medicines").document(sanitizeId(medicineId)).delete().await()
        Unit
    }.recoverCatching { e ->
        if (e is CancellationException) throw e
        throw handleFirestoreError(e, OperationType.DELETE, "medicines/$medicineId", auth)
    }

    suspend fun savePriceOffer(offer: PriceOffer): Result<Unit> = runCatching {
        val docId = sanitizeId(offer.id.ifBlank { "${offer.medicineId}_${offer.brandName}" })
        val ref = db.collection("priceOffers").document(docId)
        val exists = ref.get().await().exists()
        if (exists) {
            ref.update(offer.toWriteMap(isCreate = false)).await()
        } else {
            ref.set(offer.toWriteMap(isCreate = true)).await()
        }
        Unit
    }.recoverCatching { e ->
        if (e is CancellationException) throw e
        throw handleFirestoreError(e, OperationType.WRITE, "priceOffers/${offer.id}", auth)
    }

    suspend fun deletePriceOffer(offerId: String): Result<Unit> = runCatching {
        db.collection("priceOffers").document(sanitizeId(offerId)).delete().await()
        Unit
    }.recoverCatching { e ->
        if (e is CancellationException) throw e
        throw handleFirestoreError(e, OperationType.DELETE, "priceOffers/$offerId", auth)
    }

    suspend fun saveCategory(category: Category): Result<Unit> = runCatching {
        val docId = sanitizeId(category.id.ifBlank { category.nameEn })
        val ref = db.collection("categories").document(docId)
        val exists = ref.get().await().exists()
        if (exists) {
            ref.update(category.toWriteMap(isCreate = false)).await()
        } else {
            ref.set(category.toWriteMap(isCreate = true)).await()
        }
        Unit
    }.recoverCatching { e ->
        if (e is CancellationException) throw e
        throw handleFirestoreError(e, OperationType.WRITE, "categories/${category.id}", auth)
    }

    suspend fun saveManufacturer(manufacturer: Manufacturer): Result<Unit> = runCatching {
        val docId = sanitizeId(manufacturer.id.ifBlank { manufacturer.name })
        val ref = db.collection("manufacturers").document(docId)
        val exists = ref.get().await().exists()
        if (exists) {
            ref.update(manufacturer.toWriteMap(isCreate = false)).await()
        } else {
            ref.set(manufacturer.toWriteMap(isCreate = true)).await()
        }
        Unit
    }.recoverCatching { e ->
        if (e is CancellationException) throw e
        throw handleFirestoreError(e, OperationType.WRITE, "manufacturers/${manufacturer.id}", auth)
    }

    suspend fun saveTranslationOverride(item: TranslationOverride): Result<Unit> = runCatching {
        val docId = sanitizeId(item.id.ifBlank { item.key })
        val ref = db.collection("translations").document(docId)
        val exists = ref.get().await().exists()
        if (exists) {
            ref.update(item.toWriteMap(isCreate = false)).await()
        } else {
            ref.set(item.toWriteMap(isCreate = true)).await()
        }
        Unit
    }.recoverCatching { e ->
        if (e is CancellationException) throw e
        throw handleFirestoreError(e, OperationType.WRITE, "translations/${item.key}", auth)
    }

    suspend fun saveApiConnection(conn: ApiConnection): Result<Unit> = runCatching {
        val docId = sanitizeId(conn.id.ifBlank { conn.name })
        val ref = db.collection("apiConnections").document(docId)
        val exists = ref.get().await().exists()
        if (exists) {
            ref.update(conn.toWriteMap(isCreate = false)).await()
        } else {
            ref.set(conn.toWriteMap(isCreate = true)).await()
        }
        Unit
    }.recoverCatching { e ->
        if (e is CancellationException) throw e
        throw handleFirestoreError(e, OperationType.WRITE, "apiConnections/${conn.id}", auth)
    }

    suspend fun seedVerifiedCatalogToFirestore(): Result<Int> = runCatching {
        var count = 0
        for (cat in VerifiedReferenceCatalog.categories) {
            saveCategory(cat).getOrThrow()
            count++
        }
        for (mfr in VerifiedReferenceCatalog.manufacturers) {
            saveManufacturer(mfr).getOrThrow()
            count++
        }
        for (api in VerifiedReferenceCatalog.apiConnections) {
            saveApiConnection(api).getOrThrow()
            count++
        }
        for (med in VerifiedReferenceCatalog.medicines) {
            saveMedicine(med).getOrThrow()
            count++
        }
        for (offer in VerifiedReferenceCatalog.priceOffers) {
            savePriceOffer(offer).getOrThrow()
            count++
        }
        count
    }
}

package com.example.data.repository

import com.example.data.model.FavoriteMedicine
import com.example.data.model.Medicine
import com.example.data.model.MedicineReminder
import com.example.data.model.RecentlyViewed
import com.example.data.model.SearchHistoryEntry
import com.example.data.model.ThemeMode
import com.example.data.model.UserProfile
import com.example.i18n.AppLanguage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class UserDataRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val bootstrappedAdminEmail = "Samadconnecting@gmail.com"

    private fun requireUserId(): String =
        auth.currentUser?.uid ?: throw IllegalStateException("User must be signed in")

    private fun sanitizeId(raw: String): String {
        val cleaned = raw.trim().replace(Regex("[^a-zA-Z0-9_\\-]"), "_")
        return cleaned.ifBlank { "id_${System.currentTimeMillis()}" }.take(120)
    }

    fun observeIsAdmin(uid: String): Flow<Boolean> {
        val currentUser = auth.currentUser
        val emailAdmin = currentUser != null &&
            currentUser.isEmailVerified &&
            currentUser.email.equals(bootstrappedAdminEmail, ignoreCase = false)

        return db.collection("admins").document(uid).snapshots()
            .map { snapshot -> emailAdmin || snapshot.exists() }
            .catch { e ->
                if (e is CancellationException) throw e
                emit(emailAdmin)
            }
    }

    fun observeProfile(uid: String): Flow<UserProfile?> {
        return db.collection("users").document(uid).snapshots()
            .map { snapshot -> snapshot.toObject(UserProfile::class.java) }
            .catch { e ->
                if (e is CancellationException) throw e
                throw handleFirestoreError(e, OperationType.GET, "users/$uid", auth)
            }
    }

    suspend fun ensureUserProfile(
        language: AppLanguage,
        themeMode: ThemeMode,
        onboardingCompleted: Boolean
    ): Result<Unit> = runCatching {
        val user = auth.currentUser ?: throw IllegalStateException("Not signed in")
        val ref = db.collection("users").document(user.uid)
        val snap = ref.get().await()
        val displayName = user.displayName?.take(120).orEmpty().ifBlank { "MediGuide User" }
        val email = user.email?.take(200).orEmpty()
        if (!snap.exists()) {
            val data = mutableMapOf<String, Any>(
                "userId" to user.uid,
                "displayName" to displayName,
                "preferredLanguage" to language.code,
                "themeMode" to themeMode.name,
                "onboardingCompleted" to onboardingCompleted,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            if (email.isNotBlank()) data["email"] = email
            ref.set(data).await()
        } else {
            val updates = mutableMapOf<String, Any>(
                "displayName" to displayName,
                "updatedAt" to FieldValue.serverTimestamp()
            )
            if (email.isNotBlank()) updates["email"] = email
            ref.update(updates).await()
        }
        Unit
    }.recoverCatching { e ->
        if (e is CancellationException) throw e
        throw handleFirestoreError(e, OperationType.WRITE, "users/${auth.currentUser?.uid}", auth)
    }

    suspend fun updateProfilePreferences(
        language: AppLanguage,
        themeMode: ThemeMode,
        onboardingCompleted: Boolean
    ): Result<Unit> = runCatching {
        val uid = requireUserId()
        db.collection("users").document(uid).update(
            mapOf(
                "preferredLanguage" to language.code,
                "themeMode" to themeMode.name,
                "onboardingCompleted" to onboardingCompleted,
                "updatedAt" to FieldValue.serverTimestamp()
            )
        ).await()
        Unit
    }.recoverCatching { e ->
        if (e is CancellationException) throw e
        throw handleFirestoreError(e, OperationType.UPDATE, "users/${auth.currentUser?.uid}", auth)
    }

    fun observeFavorites(uid: String): Flow<List<FavoriteMedicine>> {
        return db.collection("users").document(uid).collection("favorites").snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { doc ->
                    doc.toObject(FavoriteMedicine::class.java)?.copy(
                        createdAt = doc.getTimestamp(
                            "createdAt",
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )
                    )
                }.sortedByDescending { it.createdAt?.seconds ?: 0L }
            }
            .catch { e ->
                if (e is CancellationException) throw e
                throw handleFirestoreError(e, OperationType.LIST, "users/$uid/favorites", auth)
            }
    }

    suspend fun toggleFavorite(medicine: Medicine, currentlyFavorite: Boolean): Result<Unit> = runCatching {
        val uid = requireUserId()
        val medId = sanitizeId(medicine.id)
        val ref = db.collection("users").document(uid).collection("favorites").document(medId)
        if (currentlyFavorite) {
            ref.delete().await()
        } else {
            val data = mapOf(
                "userId" to uid,
                "medicineId" to medId,
                "medicineName" to medicine.name.en.take(200),
                "genericName" to medicine.genericName.en.take(200),
                "categoryId" to medicine.categoryId.take(100),
                "createdAt" to FieldValue.serverTimestamp()
            )
            ref.set(data).await()
        }
        Unit
    }.recoverCatching { e ->
        if (e is CancellationException) throw e
        throw handleFirestoreError(e, OperationType.WRITE, "users/${auth.currentUser?.uid}/favorites", auth)
    }

    fun observeRecentlyViewed(uid: String): Flow<List<RecentlyViewed>> {
        return db.collection("users").document(uid).collection("recentlyViewed").snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { doc ->
                    doc.toObject(RecentlyViewed::class.java)?.copy(
                        viewedAt = doc.getTimestamp(
                            "viewedAt",
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )
                    )
                }.sortedByDescending { it.viewedAt?.seconds ?: 0L }.take(15)
            }
            .catch { e ->
                if (e is CancellationException) throw e
                throw handleFirestoreError(e, OperationType.LIST, "users/$uid/recentlyViewed", auth)
            }
    }

    suspend fun recordMedicineView(medicine: Medicine): Result<Unit> = runCatching {
        val uid = requireUserId()
        val medId = sanitizeId(medicine.id)
        val ref = db.collection("users").document(uid).collection("recentlyViewed").document(medId)
        ref.set(
            mapOf(
                "userId" to uid,
                "medicineId" to medId,
                "medicineName" to medicine.name.en.take(200),
                "viewedAt" to FieldValue.serverTimestamp()
            )
        ).await()
        logAnalyticsEvent("VIEW", medId)
        Unit
    }.recoverCatching { e ->
        if (e is CancellationException) throw e
        throw handleFirestoreError(e, OperationType.WRITE, "users/${auth.currentUser?.uid}/recentlyViewed", auth)
    }

    fun observeSearchHistory(uid: String): Flow<List<SearchHistoryEntry>> {
        return db.collection("users").document(uid).collection("searchHistory").snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { doc ->
                    doc.toObject(SearchHistoryEntry::class.java)?.copy(
                        searchedAt = doc.getTimestamp(
                            "searchedAt",
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )
                    )
                }.sortedByDescending { it.searchedAt?.seconds ?: 0L }.take(20)
            }
            .catch { e ->
                if (e is CancellationException) throw e
                throw handleFirestoreError(e, OperationType.LIST, "users/$uid/searchHistory", auth)
            }
    }

    suspend fun addSearchHistory(query: String, searchType: String = "TEXT"): Result<Unit> = runCatching {
        val trimmed = query.trim().take(200)
        if (trimmed.isBlank()) return@runCatching
        val uid = requireUserId()
        val historyId = "sh_${System.currentTimeMillis()}"
        db.collection("users").document(uid).collection("searchHistory").document(historyId)
            .set(
                mapOf(
                    "userId" to uid,
                    "query" to trimmed,
                    "searchType" to searchType.take(50),
                    "searchedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        val eventType = when (searchType.uppercase()) {
            "SCAN" -> "SCAN"
            "VOICE" -> "VOICE"
            else -> "SEARCH"
        }
        logAnalyticsEvent(eventType, trimmed.take(120))
        Unit
    }.recoverCatching { e ->
        if (e is CancellationException) throw e
        throw handleFirestoreError(e, OperationType.CREATE, "users/${auth.currentUser?.uid}/searchHistory", auth)
    }

    suspend fun clearSearchHistory(entries: List<SearchHistoryEntry>): Result<Unit> = runCatching {
        val uid = requireUserId()
        val batch = db.batch()
        entries.forEach { entry ->
            if (entry.id.isNotBlank()) {
                val ref = db.collection("users").document(uid).collection("searchHistory").document(entry.id)
                batch.delete(ref)
            }
        }
        batch.commit().await()
        Unit
    }.recoverCatching { e ->
        if (e is CancellationException) throw e
        throw handleFirestoreError(e, OperationType.DELETE, "users/${auth.currentUser?.uid}/searchHistory", auth)
    }

    fun observeReminders(uid: String): Flow<List<MedicineReminder>> {
        return db.collection("users").document(uid).collection("reminders").snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { doc ->
                    doc.toObject(MedicineReminder::class.java)
                }.sortedWith(compareBy({ it.timeHour }, { it.timeMinute }))
            }
            .catch { e ->
                if (e is CancellationException) throw e
                throw handleFirestoreError(e, OperationType.LIST, "users/$uid/reminders", auth)
            }
    }

    suspend fun saveReminder(reminder: MedicineReminder): Result<Unit> = runCatching {
        val uid = requireUserId()
        val reminderId = sanitizeId(reminder.id.ifBlank { "rem_${System.currentTimeMillis()}" })
        val ref = db.collection("users").document(uid).collection("reminders").document(reminderId)
        val exists = ref.get().await().exists()
        val medId = sanitizeId(reminder.medicineId.ifBlank { "custom_med" })
        if (!exists) {
            val map = mutableMapOf<String, Any>(
                "userId" to uid,
                "medicineId" to medId,
                "medicineName" to reminder.medicineName.trim().take(200).ifBlank { "Medicine" },
                "dosage" to reminder.dosage.trim().take(100).ifBlank { "As prescribed" },
                "scheduleSlot" to reminder.slot().name,
                "timeHour" to reminder.timeHour.coerceIn(0, 23),
                "timeMinute" to reminder.timeMinute.coerceIn(0, 59),
                "active" to reminder.active,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            if (reminder.instructions.isNotBlank()) {
                map["instructions"] = reminder.instructions.trim().take(300)
            }
            ref.set(map).await()
        } else {
            val updates = mutableMapOf<String, Any>(
                "medicineId" to medId,
                "medicineName" to reminder.medicineName.trim().take(200).ifBlank { "Medicine" },
                "dosage" to reminder.dosage.trim().take(100).ifBlank { "As prescribed" },
                "scheduleSlot" to reminder.slot().name,
                "timeHour" to reminder.timeHour.coerceIn(0, 23),
                "timeMinute" to reminder.timeMinute.coerceIn(0, 59),
                "active" to reminder.active,
                "updatedAt" to FieldValue.serverTimestamp()
            )
            if (reminder.instructions.isNotBlank()) {
                updates["instructions"] = reminder.instructions.trim().take(300)
            }
            ref.update(updates).await()
        }
        Unit
    }.recoverCatching { e ->
        if (e is CancellationException) throw e
        throw handleFirestoreError(e, OperationType.WRITE, "users/${auth.currentUser?.uid}/reminders", auth)
    }

    suspend fun deleteReminder(reminderId: String): Result<Unit> = runCatching {
        val uid = requireUserId()
        db.collection("users").document(uid).collection("reminders")
            .document(sanitizeId(reminderId)).delete().await()
        Unit
    }.recoverCatching { e ->
        if (e is CancellationException) throw e
        throw handleFirestoreError(e, OperationType.DELETE, "users/${auth.currentUser?.uid}/reminders/$reminderId", auth)
    }

    suspend fun logAnalyticsEvent(eventType: String, targetId: String) {
        val uid = auth.currentUser?.uid ?: return
        val allowed = setOf("VIEW", "SEARCH", "COMPARE", "SCAN", "VOICE")
        val normalized = if (eventType in allowed) eventType else "VIEW"
        runCatching {
            val eventId = "evt_${System.currentTimeMillis()}_${(100..999).random()}"
            db.collection("analyticsEvents").document(eventId).set(
                mapOf(
                    "userId" to uid,
                    "eventType" to normalized,
                    "targetId" to targetId.take(128),
                    "createdAt" to FieldValue.serverTimestamp()
                )
            ).await()
        }
    }
}

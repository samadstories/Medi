package com.example.data.model

import androidx.annotation.Keep
import com.example.i18n.AppLanguage
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class LocalizedText(
    val en: String = "",
    val hi: String = "",
    val bn: String = ""
) {
    fun get(language: AppLanguage): String = when (language) {
        AppLanguage.EN -> en.ifBlank { hi.ifBlank { bn } }
        AppLanguage.HI -> hi.ifBlank { en }
        AppLanguage.BN -> bn.ifBlank { en }
    }

    fun toMap(): Map<String, Any> = mapOf(
        "en" to en.ifBlank { "-" },
        "hi" to hi.ifBlank { en.ifBlank { "-" } },
        "bn" to bn.ifBlank { en.ifBlank { "-" } }
    )
}

@Keep
@IgnoreExtraProperties
data class MedicineMonograph(
    val usedFor: LocalizedText = LocalizedText(),
    val usesAndBenefits: LocalizedText = LocalizedText(),
    val howItWorks: LocalizedText = LocalizedText(),
    val commonSideEffects: LocalizedText = LocalizedText(),
    val seriousSideEffects: LocalizedText = LocalizedText(),
    val warnings: LocalizedText = LocalizedText(),
    val drugInteractions: LocalizedText = LocalizedText(),
    val contraindications: LocalizedText = LocalizedText(),
    val pregnancyWarnings: LocalizedText = LocalizedText(),
    val storageInstructions: LocalizedText = LocalizedText()
) {
    fun toMap(): Map<String, Any> = mapOf(
        "usedFor" to usedFor.toMap(),
        "usesAndBenefits" to usesAndBenefits.toMap(),
        "howItWorks" to howItWorks.toMap(),
        "commonSideEffects" to commonSideEffects.toMap(),
        "seriousSideEffects" to seriousSideEffects.toMap(),
        "warnings" to warnings.toMap(),
        "drugInteractions" to drugInteractions.toMap(),
        "contraindications" to contraindications.toMap(),
        "pregnancyWarnings" to pregnancyWarnings.toMap(),
        "storageInstructions" to storageInstructions.toMap()
    )
}

@Keep
@IgnoreExtraProperties
data class Medicine(
    @DocumentId val id: String = "",
    val name: LocalizedText = LocalizedText(),
    val brandName: String = "",
    val genericName: LocalizedText = LocalizedText(),
    val saltComposition: String = "",
    val categoryId: String = "",
    val manufacturerId: String = "",
    val manufacturerDetails: LocalizedText = LocalizedText(),
    val imageUrl: String = "",
    val monograph: MedicineMonograph = MedicineMonograph(),
    val prescriptionRequired: Boolean = false,
    val officialSources: String = "",
    val lastVerifiedDate: String = "",
    val openFdaGenericQuery: String = "",
    val popularScore: Long = 0L,
    val published: Boolean = true,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
) {
    fun toWriteMap(isCreate: Boolean): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "name" to name.toMap(),
            "brandName" to brandName,
            "genericName" to genericName.toMap(),
            "saltComposition" to saltComposition,
            "categoryId" to categoryId,
            "manufacturerId" to manufacturerId,
            "manufacturerDetails" to manufacturerDetails.toMap(),
            "monograph" to monograph.toMap(),
            "prescriptionRequired" to prescriptionRequired,
            "officialSources" to officialSources,
            "lastVerifiedDate" to lastVerifiedDate,
            "popularScore" to popularScore,
            "published" to published,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (imageUrl.isNotBlank()) map["imageUrl"] = imageUrl
        if (openFdaGenericQuery.isNotBlank()) map["openFdaGenericQuery"] = openFdaGenericQuery
        if (isCreate) map["createdAt"] = FieldValue.serverTimestamp()
        return map
    }
}

@Keep
@IgnoreExtraProperties
data class PriceOffer(
    @DocumentId val id: String = "",
    val medicineId: String = "",
    val saltComposition: String = "",
    val brandName: String = "",
    val genericName: String = "",
    val genericOption: Boolean = false,
    val packSize: String = "",
    val unitCount: Long = 10L,
    val mrpInr: Double = 0.0,
    val sellingPriceInr: Double = 0.0,
    val pricePerUnitInr: Double = 0.0,
    val discountPercent: Long = 0L,
    val manufacturerName: String = "",
    val availability: String = AvailabilityStatus.IN_STOCK.name,
    val sourceName: String = "",
    val lastUpdatedDate: String = "",
    val published: Boolean = true,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
) {
    fun availabilityStatus(): AvailabilityStatus = AvailabilityStatus.fromName(availability)

    fun toWriteMap(isCreate: Boolean): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "medicineId" to medicineId,
            "saltComposition" to saltComposition,
            "brandName" to brandName,
            "genericName" to genericName,
            "genericOption" to genericOption,
            "packSize" to packSize,
            "unitCount" to unitCount,
            "mrpInr" to mrpInr,
            "sellingPriceInr" to sellingPriceInr,
            "pricePerUnitInr" to pricePerUnitInr,
            "discountPercent" to discountPercent,
            "manufacturerName" to manufacturerName,
            "availability" to availabilityStatus().name,
            "sourceName" to sourceName,
            "lastUpdatedDate" to lastUpdatedDate,
            "published" to published,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (isCreate) map["createdAt"] = FieldValue.serverTimestamp()
        return map
    }
}

@Keep
@IgnoreExtraProperties
data class Category(
    @DocumentId val id: String = "",
    val nameEn: String = "",
    val nameHi: String = "",
    val nameBn: String = "",
    val iconKey: String = "medication",
    val sortOrder: Long = 0L,
    val published: Boolean = true,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
) {
    fun localizedName(language: AppLanguage): String = when (language) {
        AppLanguage.EN -> nameEn
        AppLanguage.HI -> nameHi.ifBlank { nameEn }
        AppLanguage.BN -> nameBn.ifBlank { nameEn }
    }

    fun toWriteMap(isCreate: Boolean): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "nameEn" to nameEn,
            "nameHi" to nameHi.ifBlank { nameEn },
            "nameBn" to nameBn.ifBlank { nameEn },
            "iconKey" to iconKey,
            "sortOrder" to sortOrder,
            "published" to published,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (isCreate) map["createdAt"] = FieldValue.serverTimestamp()
        return map
    }
}

@Keep
@IgnoreExtraProperties
data class Manufacturer(
    @DocumentId val id: String = "",
    val name: String = "",
    val country: String = "",
    val licenseInfo: String = "",
    val published: Boolean = true,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
) {
    fun toWriteMap(isCreate: Boolean): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "name" to name,
            "country" to country,
            "published" to published,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (licenseInfo.isNotBlank()) map["licenseInfo"] = licenseInfo
        if (isCreate) map["createdAt"] = FieldValue.serverTimestamp()
        return map
    }
}

@Keep
@IgnoreExtraProperties
data class TranslationOverride(
    @DocumentId val id: String = "",
    val key: String = "",
    val en: String = "",
    val hi: String = "",
    val bn: String = "",
    val published: Boolean = true,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
) {
    fun get(language: AppLanguage): String = when (language) {
        AppLanguage.EN -> en
        AppLanguage.HI -> hi.ifBlank { en }
        AppLanguage.BN -> bn.ifBlank { en }
    }

    fun toWriteMap(isCreate: Boolean): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "key" to key,
            "en" to en,
            "hi" to hi.ifBlank { en },
            "bn" to bn.ifBlank { en },
            "published" to published,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (isCreate) map["createdAt"] = FieldValue.serverTimestamp()
        return map
    }
}

@Keep
@IgnoreExtraProperties
data class ApiConnection(
    @DocumentId val id: String = "",
    val name: String = "",
    val baseUrl: String = "",
    val providerType: String = "",
    val active: Boolean = true,
    val statusMessage: String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
) {
    fun toWriteMap(isCreate: Boolean): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "name" to name,
            "baseUrl" to baseUrl,
            "providerType" to providerType,
            "active" to active,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (statusMessage.isNotBlank()) map["statusMessage"] = statusMessage
        if (isCreate) map["createdAt"] = FieldValue.serverTimestamp()
        return map
    }
}

@Keep
@IgnoreExtraProperties
data class UserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val preferredLanguage: String = AppLanguage.EN.code,
    val themeMode: String = ThemeMode.SYSTEM.name,
    val onboardingCompleted: Boolean = false,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class FavoriteMedicine(
    @DocumentId val id: String = "",
    val userId: String = "",
    val medicineId: String = "",
    val medicineName: String = "",
    val genericName: String = "",
    val categoryId: String = "",
    val createdAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class RecentlyViewed(
    @DocumentId val id: String = "",
    val userId: String = "",
    val medicineId: String = "",
    val medicineName: String = "",
    val viewedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class SearchHistoryEntry(
    @DocumentId val id: String = "",
    val userId: String = "",
    val query: String = "",
    val searchType: String = "TEXT",
    val searchedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class MedicineReminder(
    @DocumentId val id: String = "",
    val userId: String = "",
    val medicineId: String = "",
    val medicineName: String = "",
    val dosage: String = "",
    val scheduleSlot: String = ScheduleSlot.MORNING.name,
    val timeHour: Long = 8L,
    val timeMinute: Long = 0L,
    val instructions: String = "",
    val active: Boolean = true,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
) {
    fun slot(): ScheduleSlot = ScheduleSlot.fromName(scheduleSlot)
}

@Keep
@IgnoreExtraProperties
data class AnalyticsEvent(
    @DocumentId val id: String = "",
    val userId: String = "",
    val eventType: String = "VIEW",
    val targetId: String = "",
    val createdAt: Timestamp? = null
)

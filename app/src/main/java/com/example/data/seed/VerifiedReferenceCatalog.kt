package com.example.data.seed

import com.example.data.model.ApiConnection
import com.example.data.model.Category
import com.example.data.model.Manufacturer
import com.example.data.model.Medicine
import com.example.data.model.PriceOffer

object VerifiedReferenceCatalog {
    val categories: List<Category> get() = VerifiedCatalogMetaSeed.categories
    val manufacturers: List<Manufacturer> get() = VerifiedCatalogMetaSeed.manufacturers
    val apiConnections: List<ApiConnection> get() = VerifiedCatalogMetaSeed.apiConnections
    val medicines: List<Medicine> get() = VerifiedMedicinesSeed.coreMedicines + VerifiedExtraMedicinesSeed.extraMedicines
    val priceOffers: List<PriceOffer> get() = VerifiedPricesSeed.priceOffers
}

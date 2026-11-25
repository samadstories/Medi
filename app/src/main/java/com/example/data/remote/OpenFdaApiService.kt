package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.InetAddress
import java.net.URI
import java.util.concurrent.TimeUnit

data class OpenFdaLabelResponse(
    val results: List<OpenFdaDrugLabel> = emptyList()
)

data class OpenFdaDrugLabel(
    @Json(name = "effective_time") val effectiveTime: String? = null,
    @Json(name = "indications_and_usage") val indicationsAndUsage: List<String>? = null,
    @Json(name = "warnings") val warnings: List<String>? = null,
    @Json(name = "warnings_and_cautions") val warningsAndCautions: List<String>? = null,
    @Json(name = "adverse_reactions") val adverseReactions: List<String>? = null,
    @Json(name = "drug_interactions") val drugInteractions: List<String>? = null,
    @Json(name = "contraindications") val contraindications: List<String>? = null,
    @Json(name = "pregnancy") val pregnancy: List<String>? = null,
    @Json(name = "openfda") val openfda: OpenFdaMeta? = null
)

data class OpenFdaMeta(
    @Json(name = "brand_name") val brandName: List<String>? = null,
    @Json(name = "generic_name") val genericName: List<String>? = null,
    @Json(name = "manufacturer_name") val manufacturerName: List<String>? = null,
    @Json(name = "substance_name") val substanceName: List<String>? = null
)

interface OpenFdaRetrofitApi {
    @GET("drug/label.json")
    suspend fun searchDrugLabel(
        @Query("search") searchQuery: String,
        @Query("limit") limit: Int = 1
    ): OpenFdaLabelResponse
}

data class FdaMonographSnippet(
    val brandNames: String,
    val genericNames: String,
    val manufacturer: String,
    val indications: String,
    val warnings: String,
    val adverseReactions: String,
    val interactions: String,
    val contraindications: String,
    val effectiveDate: String
)

object OpenFdaClient {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    private val api: OpenFdaRetrofitApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.fda.gov/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(OpenFdaRetrofitApi::class.java)
    }

    suspend fun fetchDrugLabel(genericOrBrandQuery: String): Result<FdaMonographSnippet> =
        withContext(Dispatchers.IO) {
            runCatching {
                val sanitized = genericOrBrandQuery
                    .trim()
                    .replace(Regex("[^a-zA-Z0-9\\s\\-]"), " ")
                    .trim()
                require(sanitized.isNotBlank()) { "Search term required for OpenFDA lookup." }
                val queryExpr = "openfda.generic_name:\"$sanitized\" OR openfda.brand_name:\"$sanitized\""
                val response = api.searchDrugLabel(searchQuery = queryExpr, limit = 1)
                val first = response.results.firstOrNull()
                    ?: throw IllegalStateException("No FDA label entry matched \"$sanitized\".")
                val meta = first.openfda
                FdaMonographSnippet(
                    brandNames = meta?.brandName?.take(3)?.joinToString(", ").orEmpty().ifBlank { sanitized },
                    genericNames = meta?.genericName?.take(3)?.joinToString(", ").orEmpty().ifBlank { sanitized },
                    manufacturer = meta?.manufacturerName?.firstOrNull().orEmpty().ifBlank { "U.S. FDA Labeler" },
                    indications = first.indicationsAndUsage?.firstOrNull()?.take(550).orEmpty(),
                    warnings = (first.warnings ?: first.warningsAndCautions)?.firstOrNull()?.take(550).orEmpty(),
                    adverseReactions = first.adverseReactions?.firstOrNull()?.take(550).orEmpty(),
                    interactions = first.drugInteractions?.firstOrNull()?.take(450).orEmpty(),
                    contraindications = first.contraindications?.firstOrNull()?.take(450).orEmpty(),
                    effectiveDate = first.effectiveTime.orEmpty().ifBlank { "FDA DailyMed" }
                )
            }
        }

    suspend fun testHttpsEndpoint(rawUrl: String): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val uri = URI(rawUrl.trim())
            require(uri.scheme.equals("https", ignoreCase = true)) {
                "Only HTTPS endpoints are permitted."
            }
            val host = uri.host ?: throw IllegalArgumentException("Invalid host URL.")
            val addresses = InetAddress.getAllByName(host)
            require(addresses.none { it.isAnyLocalAddress || it.isLoopbackAddress || it.isSiteLocalAddress || it.isLinkLocalAddress }) {
                "Private or loopback addresses are not permitted."
            }
            val request = Request.Builder()
                .url(uri.toString())
                .head()
                .build()
            okHttpClient.newCall(request).execute().use { resp ->
                "HTTP ${resp.code} (${if (resp.isSuccessful || resp.code in 200..405) "Reachable" else "Status ${resp.code}"})"
            }
        }
    }
}

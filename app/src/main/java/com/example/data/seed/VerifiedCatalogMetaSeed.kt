package com.example.data.seed

import com.example.data.model.ApiConnection
import com.example.data.model.Category
import com.example.data.model.Manufacturer

object VerifiedCatalogMetaSeed {

    val categories: List<Category> = listOf(
        Category("fever_pain", "Fever & Pain Relief", "बुखार और दर्द निवारक", "জ্বর ও ব্যথা উপশম", "thermostat", 1, true),
        Category("antibiotics", "Antibiotics", "एंटीबायोटिक्स", "অ্যান্টিবায়োটিক", "biotech", 2, true),
        Category("diabetes", "Diabetes", "मधुमेह (डायबिटीज)", "ডায়াবেটিস", "bloodtype", 3, true),
        Category("blood_pressure", "Blood Pressure", "रक्तचाप (बीपी)", "রক্তচাপ", "monitor_heart", 4, true),
        Category("heart_care", "Heart Care", "हृदय देखभाल", "হৃদযন্ত্রের যত্ন", "favorite", 5, true),
        Category("vitamins", "Vitamins & Supplements", "विटामिन और सप्लीमेंट्स", "ভিটামিন ও সাপ্লিমেন্ট", "medication", 6, true),
        Category("skin_care", "Skin Care", "त्वचा देखभाल", "ত্বকের যত্ন", "spa", 7, true),
        Category("digestive", "Digestive Health", "पाचन स्वास्थ्य", "হজম ও পেটের স্বাস্থ্য", "healing", 8, true),
        Category("cough_cold", "Cough & Cold", "खांसी और जुकाम", "সর্দি ও কাশি", "air", 9, true),
        Category("allergy", "Allergy", "एलर्जी", "অ্যালার্জি", "coronavirus", 10, true),
        Category("eye_ear", "Eye & Ear Care", "आंख और कान की देखभाल", "চোখ ও কানের যত্ন", "visibility", 11, true),
        Category("womens_health", "Women's Health", "महिला स्वास्थ्य", "নারী স্বাস্থ্য", "pregnant_woman", 12, true),
        Category("childrens_medicines", "Children's Medicines", "बच्चों की दवाएं", "শিশুদের ওষুধ", "child_care", 13, true)
    )

    val manufacturers: List<Manufacturer> = listOf(
        Manufacturer("mfr_bppi", "Pharmaceuticals & Medical Devices Bureau of India (PMBJP Jan Aushadhi)", "India", "Govt. of India Dept. of Pharmaceuticals", true),
        Manufacturer("mfr_micro", "Micro Labs Ltd.", "India", "CDSCO Approved GMP Facility, Bengaluru", true),
        Manufacturer("mfr_cipla", "Cipla Ltd.", "India", "CDSCO & WHO-GMP Certified, Mumbai", true),
        Manufacturer("mfr_sun", "Sun Pharmaceutical Industries Ltd.", "India", "CDSCO & USFDA Inspected, Vadodara", true),
        Manufacturer("mfr_usv", "USV Private Ltd.", "India", "CDSCO Approved Facility, Mumbai", true),
        Manufacturer("mfr_gsk", "GlaxoSmithKline Pharmaceuticals Ltd.", "India / UK", "CDSCO Licensed Facility, Nashik", true),
        Manufacturer("mfr_sanofi", "Sanofi India Ltd.", "India / France", "CDSCO Approved Facility, Ankleshwar", true),
        Manufacturer("mfr_drreddys", "Dr. Reddy's Laboratories Ltd.", "India", "CDSCO & USFDA Certified, Hyderabad", true),
        Manufacturer("mfr_alkem", "Alkem Laboratories Ltd.", "India", "CDSCO Approved Facility, Daman", true),
        Manufacturer("mfr_abbott", "Abbott India Ltd.", "India / USA", "CDSCO Approved Facility, Goa", true),
        Manufacturer("mfr_glenmark", "Glenmark Pharmaceuticals Ltd.", "India", "CDSCO & WHO-GMP Certified, Nashik", true)
    )

    val apiConnections: List<ApiConnection> = listOf(
        ApiConnection(
            id = "openfda_drug_label",
            name = "U.S. FDA OpenFDA Drug Label API",
            baseUrl = "https://api.fda.gov/drug/label.json",
            providerType = "OPENFDA_REST",
            active = true,
            statusMessage = "Live Verified Monograph Endpoint"
        ),
        ApiConnection(
            id = "nppa_pharma_sahi_daam",
            name = "NPPA Pharma Sahi Daam (DPCO Ceiling Reference)",
            baseUrl = "https://nppaprice.gov.in/",
            providerType = "NPPA_REFERENCE",
            active = true,
            statusMessage = "Statutory Ceiling Price Reference Registry"
        ),
        ApiConnection(
            id = "cdsco_drug_directory",
            name = "CDSCO Approved Drug & NLEM 2022 Registry",
            baseUrl = "https://cdsco.gov.in/",
            providerType = "CDSCO_REFERENCE",
            active = true,
            statusMessage = "National List of Essential Medicines Reference"
        )
    )
}

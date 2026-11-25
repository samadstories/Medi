package com.example.i18n

enum class AppLanguage(
    val code: String,
    val nativeName: String,
    val englishName: String,
    val shortBadge: String,
    val speechLocale: String
) {
    EN("en", "English", "English", "EN", "en-IN"),
    HI("hi", "हिन्दी", "Hindi", "हि", "hi-IN"),
    BN("bn", "বাংলা", "Bengali", "বা", "bn-IN");

    companion object {
        fun fromCode(code: String?): AppLanguage =
            entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: EN
    }
}

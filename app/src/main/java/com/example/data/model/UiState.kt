package com.example.data.model

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

enum class ThemeMode {
    SYSTEM, LIGHT, DARK;

    companion object {
        fun fromName(raw: String?): ThemeMode =
            entries.firstOrNull { it.name.equals(raw, ignoreCase = true) } ?: SYSTEM
    }
}

enum class AvailabilityStatus {
    IN_STOCK, LIMITED, OUT_OF_STOCK;

    companion object {
        fun fromName(raw: String?): AvailabilityStatus =
            entries.firstOrNull { it.name.equals(raw, ignoreCase = true) } ?: IN_STOCK
    }
}

enum class ScheduleSlot(val defaultHour: Int, val defaultMinute: Int) {
    MORNING(8, 0),
    AFTERNOON(13, 30),
    EVENING(18, 30),
    NIGHT(21, 30),
    CUSTOM(9, 0);

    companion object {
        fun fromName(raw: String?): ScheduleSlot =
            entries.firstOrNull { it.name.equals(raw, ignoreCase = true) } ?: MORNING
    }
}

package com.weekchecker.util

import com.weekchecker.domain.model.WeekInfo

object Strings {
    fun get(key: String, locale: String = "en"): String {
        return when (locale) {
            "fr" -> frenchStrings[key] ?: englishStrings[key] ?: key
            else -> englishStrings[key] ?: key
        }
    }

    private val englishStrings = mapOf(
        "app_title" to "Week Checker",
        "current_week" to "Current Week",
        "even_week" to "Even Week",
        "odd_week" to "Odd Week",
        "refresh" to "Refresh",
        "last_updated" to "Last updated",
        "monday" to "Monday",
        "sunday" to "Sunday",
        "through" to "through",
        "loading" to "Loading...",
        "error_title" to "Something went wrong",
        "retry" to "Retry"
    )

    private val frenchStrings = mapOf(
        "app_title" to "Vérificateur de Semaine",
        "current_week" to "Semaine actuelle",
        "even_week" to "Semaine paire",
        "odd_week" to "Semaine impaire",
        "refresh" to "Actualiser",
        "last_updated" to "Dernière mise à jour",
        "monday" to "Lundi",
        "sunday" to "Dimanche",
        "through" to "à",
        "loading" to "Chargement...",
        "error_title" to "Quelque chose s'est mal passé",
        "retry" to "Réessayer"
    )
}

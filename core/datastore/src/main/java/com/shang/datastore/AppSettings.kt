package com.shang.datastore

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val language: Language = Language.ENGLISH,
    @Serializable(with = PersistentListLocationSerializer::class)
    val lastKnownLocation: PersistentList<Location> = persistentListOf(),
)

@Serializable
data class Location(val lat: Double, val long: Double)

enum class Language {
    ENGLISH,
    ARABIC,
    SPANISH,
}

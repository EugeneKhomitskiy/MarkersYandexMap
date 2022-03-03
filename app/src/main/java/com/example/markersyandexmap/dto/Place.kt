package com.example.markersyandexmap.dto

data class Place(
    val id: Int,
    val title: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        val empty = Place(
            id = 0,
            title = "",
            description = "",
            latitude = 0.0,
            longitude = 0.0
        )
    }
}
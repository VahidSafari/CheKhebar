package com.example.chekhebar.ui

data class PlaceDetailView(
    override val id: String,
    override val name: String,
    override var distance: Int,
    val phoneNumber: String,
    val rating: Double,
    val ratingHexColor: String
): PlaceView(id, name, distance)
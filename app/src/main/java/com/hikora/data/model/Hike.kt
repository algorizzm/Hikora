package com.hikora.data.model

data class Hike(
    val id: String = "",
    val title: String = "",
    val location: String = "",
    val difficulty: String = "",
    val distance: Double = 0.0,
    val guideId: String = ""
)
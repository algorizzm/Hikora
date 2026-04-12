package com.hikora.data.model

data class Booking(
    val id: String = "",
    val hikeId: String = "",
    val userId: String = "",
    val status: String = "pending" // pending, confirmed
)
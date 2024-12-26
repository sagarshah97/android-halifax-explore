package com.mobile_computing.halifaxexplore.business_directory

import java.io.Serializable

data class LocalBusiness(
    var id: String = "",
    val name: String = "",
    val description: String = "",
    val image: String = "",
    val opening_hour: String = "",
    val closing_hour: String = "",
    val rating: Double = 0.0,
    val address: String = "",
    val phone: String = "",
    val email: String = "",
    var reviews: List<Review> = emptyList()
)

data class Review(
    val user: String = "",
    val comment: String = "",
    val rating: Double = 0.0
) : Serializable

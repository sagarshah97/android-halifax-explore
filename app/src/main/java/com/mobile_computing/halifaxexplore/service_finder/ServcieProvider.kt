package com.mobile_computing.halifaxexplore.service_finder

// ServiceProvider.kt
data class ServiceProvider(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val location: String,
    val website: String
) {
    fun getDetailsAsString(): String {
        return "Name: $name\n" +
                "Email: $email\n" +
                "Phone Number: $phoneNumber\n" +
                "Location: $location\n" +
                "Website: $website"
    }
}

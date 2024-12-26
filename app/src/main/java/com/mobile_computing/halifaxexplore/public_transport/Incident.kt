package com.mobile_computing.halifaxexplore.public_transport

import com.google.firebase.Timestamp

data class Incident(
    var id: String = "",
    val title: String = "",
    val description: String = "",
    val timestamp: Timestamp? = null // Firebase Timestamp object
)

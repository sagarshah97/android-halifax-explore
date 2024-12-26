package com.mobile_computing.halifaxexplore.marketplace

data class MarketplaceItem(
    val imageUrl: String,
    val itemName: String,
    val itemDescription: String,
    val itemPrice: Double?,
    val sellerEmail: String
)
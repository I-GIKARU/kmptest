package com.kmptest.models

data class InventoryItem(
    val id: String,
    val name: String,
    val quantity: Int,
    val price: Double,
    val category: String
)

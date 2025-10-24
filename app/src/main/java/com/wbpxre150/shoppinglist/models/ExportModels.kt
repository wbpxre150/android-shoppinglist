package com.wbpxre150.shoppinglist.models

import com.google.gson.annotations.SerializedName

/**
 * Data model for exporting a shopping list to JSON format
 */
data class ExportShoppingList(
    @SerializedName("version")
    val version: Int = 1,

    @SerializedName("exportTimestamp")
    val exportTimestamp: Long = System.currentTimeMillis(),

    @SerializedName("listName")
    val listName: String,

    @SerializedName("items")
    val items: List<ExportShoppingItem>
)

/**
 * Data model for individual shopping items in export
 */
data class ExportShoppingItem(
    @SerializedName("name")
    val name: String,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("price")
    val price: Double = 0.0,

    @SerializedName("position")
    val position: Int = 0
)

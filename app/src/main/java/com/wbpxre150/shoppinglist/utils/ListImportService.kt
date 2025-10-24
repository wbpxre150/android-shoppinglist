package com.wbpxre150.shoppinglist.utils

import android.content.Context
import android.net.Uri
import com.wbpxre150.shoppinglist.ShoppingItem
import com.wbpxre150.shoppinglist.ShoppingList
import com.wbpxre150.shoppinglist.models.ExportShoppingList
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.InputStreamReader

/**
 * Service for importing shopping lists from JSON files
 */
class ListImportService(private val context: Context) {

    private val gson: Gson = Gson()

    /**
     * Result of an import operation
     */
    sealed class ImportResult {
        data class Success(val shoppingList: ShoppingList, val items: List<ShoppingItem>) : ImportResult()
        data class Error(val message: String) : ImportResult()
    }

    /**
     * Imports a shopping list from a .shoppinglist file URI
     * @param fileUri The URI of the file to import
     * @param existingListNames List of existing list names to avoid duplicates
     * @return ImportResult containing the parsed data or error
     */
    fun importList(fileUri: Uri, existingListNames: List<String>): ImportResult {
        try {
            // Read file content
            val inputStream = context.contentResolver.openInputStream(fileUri)
                ?: return ImportResult.Error("Could not open file")

            val jsonString = InputStreamReader(inputStream).use { reader ->
                reader.readText()
            }

            // Parse JSON
            val exportList = try {
                gson.fromJson(jsonString, ExportShoppingList::class.java)
            } catch (e: JsonSyntaxException) {
                return ImportResult.Error("Invalid file format")
            }

            // Validate data
            if (exportList == null) {
                return ImportResult.Error("Invalid file format")
            }

            if (exportList.listName.isBlank()) {
                return ImportResult.Error("List name is empty")
            }

            // Check version compatibility (currently only version 1)
            if (exportList.version > 1) {
                return ImportResult.Error("Unsupported file version. Please update the app.")
            }

            // Handle duplicate list names
            val finalListName = generateUniqueName(exportList.listName, existingListNames)

            // Create ShoppingList entity (without ID, will be auto-generated)
            val shoppingList = ShoppingList(
                name = finalListName,
                position = 0 // Will be updated by repository if needed
            )

            // Create ShoppingItem entities (without IDs and listId, will be set after insert)
            val items = exportList.items.map { exportItem ->
                ShoppingItem(
                    listId = 0, // Will be set after list insertion
                    name = exportItem.name,
                    quantity = exportItem.quantity.coerceAtLeast(1), // Ensure at least 1
                    price = exportItem.price.coerceAtLeast(0.0), // Ensure non-negative
                    position = exportItem.position,
                    isPurchased = false // Reset purchased status for new import
                )
            }

            return ImportResult.Success(shoppingList, items)

        } catch (e: Exception) {
            e.printStackTrace()
            return ImportResult.Error("Failed to import list: ${e.message}")
        }
    }

    /**
     * Generates a unique name by appending a number if the name already exists
     */
    private fun generateUniqueName(baseName: String, existingNames: List<String>): String {
        if (!existingNames.contains(baseName)) {
            return baseName
        }

        var counter = 1
        var newName: String

        do {
            newName = "$baseName ($counter)"
            counter++
        } while (existingNames.contains(newName))

        return newName
    }
}

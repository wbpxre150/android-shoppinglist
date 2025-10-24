package com.wbpxre150.shoppinglist.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.wbpxre150.shoppinglist.ShoppingItem
import com.wbpxre150.shoppinglist.ShoppingList
import com.wbpxre150.shoppinglist.models.ExportShoppingItem
import com.wbpxre150.shoppinglist.models.ExportShoppingList
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileWriter

/**
 * Service for exporting shopping lists to JSON files
 */
class ListExportService(private val context: Context) {

    companion object {
        const val MIME_TYPE = "application/x-shoppinglist"
    }

    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    /**
     * Exports a shopping list with its items to a .shoppinglist JSON file
     * @param shoppingList The shopping list to export
     * @param items The items in the shopping list
     * @return URI of the exported file, or null if export failed
     */
    fun exportList(shoppingList: ShoppingList, items: List<ShoppingItem>): Uri? {
        try {
            // Convert to export models
            val exportItems = items
                .sortedBy { it.position }
                .map { item ->
                    ExportShoppingItem(
                        name = item.name,
                        quantity = item.quantity,
                        price = item.price,
                        position = item.position
                    )
                }

            val exportList = ExportShoppingList(
                listName = shoppingList.name,
                items = exportItems
            )

            // Create JSON string
            val jsonString = gson.toJson(exportList)

            // Create file in cache directory
            val sharedListsDir = File(context.cacheDir, "shared_lists")
            if (!sharedListsDir.exists()) {
                sharedListsDir.mkdirs()
            }

            // Clean filename (remove invalid characters)
            val cleanFileName = shoppingList.name
                .replace(Regex("[^a-zA-Z0-9\\s-]"), "")
                .replace(Regex("\\s+"), "_")
                .take(50) // Limit length

            val fileName = "${cleanFileName}_${System.currentTimeMillis()}.shoppinglist"
            val file = File(sharedListsDir, fileName)

            // Write JSON to file
            FileWriter(file).use { writer ->
                writer.write(jsonString)
            }

            // Get URI using FileProvider
            return FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Cleans up old exported files from cache
     */
    fun cleanupOldExports() {
        try {
            val sharedListsDir = File(context.cacheDir, "shared_lists")
            if (sharedListsDir.exists()) {
                val now = System.currentTimeMillis()
                sharedListsDir.listFiles()?.forEach { file ->
                    // Delete files older than 1 hour
                    if (now - file.lastModified() > 3600000) {
                        file.delete()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

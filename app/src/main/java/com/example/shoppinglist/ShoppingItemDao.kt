package com.example.shoppinglist

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shoppingItem: ShoppingItem)

    @Update
    suspend fun update(shoppingItem: ShoppingItem)

    @Delete
    suspend fun delete(shoppingItem: ShoppingItem)

    @Query("SELECT * FROM shopping_items WHERE listId = :listId ORDER BY isPurchased ASC, position ASC, name ASC")
    fun getItemsForList(listId: Int): LiveData<List<ShoppingItem>>

    @Query("DELETE FROM shopping_items WHERE listId = :listId")
    suspend fun deleteAllItemsFromList(listId: Int)
    
    @Update
    suspend fun updateItems(items: List<ShoppingItem>)
}
package com.example.shoppinglist

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingListDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shoppingList: ShoppingList): Long

    @Update
    suspend fun update(shoppingList: ShoppingList)

    @Update
    suspend fun updateShoppingLists(shoppingLists: List<ShoppingList>)

    @Delete
    suspend fun delete(shoppingList: ShoppingList)

    @Query("SELECT * FROM shopping_lists ORDER BY position ASC, name ASC")
    fun getAllShoppingLists(): LiveData<List<ShoppingList>>

    @Query("SELECT * FROM shopping_lists WHERE id = :listId")
    fun getShoppingListById(listId: Int): LiveData<ShoppingList>
    
    @Query("SELECT * FROM shopping_lists WHERE id = :listId")
    suspend fun getShoppingListByIdSync(listId: Int): ShoppingList?

    // For getting the count of items in each list
    @Query("SELECT COUNT(id) FROM shopping_items WHERE listId = :listId")
    fun getItemCountForList(listId: Int): LiveData<Int>
    
    @Query("SELECT COUNT(id) FROM shopping_items WHERE listId = :listId AND isPurchased = 1")
    fun getPurchasedItemCountForList(listId: Int): LiveData<Int>
}
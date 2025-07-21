package com.example.shoppinglist

import androidx.lifecycle.LiveData

class ShoppingRepository(
    private val shoppingListDao: ShoppingListDao,
    private val shoppingItemDao: ShoppingItemDao
) {
    // Shopping Lists
    val allShoppingLists: LiveData<List<ShoppingList>> = shoppingListDao.getAllShoppingLists()

    suspend fun insertShoppingList(shoppingList: ShoppingList): Long {
        return shoppingListDao.insert(shoppingList)
    }

    suspend fun updateShoppingList(shoppingList: ShoppingList) {
        shoppingListDao.update(shoppingList)
    }

    suspend fun deleteShoppingList(shoppingList: ShoppingList) {
        shoppingListDao.delete(shoppingList)
    }

    fun getShoppingListById(id: Int): LiveData<ShoppingList> {
        return shoppingListDao.getShoppingListById(id)
    }

    fun getItemCountForList(listId: Int): LiveData<Int> {
        return shoppingListDao.getItemCountForList(listId)
    }

    fun getPurchasedItemCountForList(listId: Int): LiveData<Int> {
        return shoppingListDao.getPurchasedItemCountForList(listId)
    }

    // Shopping Items
    fun getItemsForList(listId: Int): LiveData<List<ShoppingItem>> {
        return shoppingItemDao.getItemsForList(listId)
    }

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItemDao.insert(shoppingItem)
    }

    suspend fun updateShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItemDao.update(shoppingItem)
    }

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItemDao.delete(shoppingItem)
    }

    suspend fun deleteAllItemsFromList(listId: Int) {
        shoppingItemDao.deleteAllItemsFromList(listId)
    }
    
    suspend fun updateShoppingItems(items: List<ShoppingItem>) {
        shoppingItemDao.updateItems(items)
    }
    
    suspend fun updateShoppingLists(shoppingLists: List<ShoppingList>) {
        shoppingListDao.updateShoppingLists(shoppingLists)
    }
}
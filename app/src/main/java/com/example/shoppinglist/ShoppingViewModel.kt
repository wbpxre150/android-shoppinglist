package com.example.shoppinglist

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ShoppingViewModel(private val repository: ShoppingRepository) : ViewModel() {

    val allShoppingLists: LiveData<List<ShoppingList>> = repository.allShoppingLists

    fun insert(shoppingList: ShoppingList) = viewModelScope.launch {
        repository.insertShoppingList(shoppingList)
    }

    fun update(shoppingList: ShoppingList) = viewModelScope.launch {
        repository.updateShoppingList(shoppingList)
    }

    fun delete(shoppingList: ShoppingList) = viewModelScope.launch {
        repository.deleteShoppingList(shoppingList)
    }

    fun getShoppingListById(id: Int): LiveData<ShoppingList> {
        return repository.getShoppingListById(id)
    }

    fun getItemCountForList(listId: Int): LiveData<Int> {
        return repository.getItemCountForList(listId)
    }

    fun getPurchasedItemCountForList(listId: Int): LiveData<Int> {
        return repository.getPurchasedItemCountForList(listId)
    }

    // Shopping Items
    fun getItemsForList(listId: Int): LiveData<List<ShoppingItem>> {
        return repository.getItemsForList(listId)
    }

    fun insertItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun updateItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.updateShoppingItem(shoppingItem)
    }

    fun deleteItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun deleteAllItemsFromList(listId: Int) = viewModelScope.launch {
        repository.deleteAllItemsFromList(listId)
    }
    
    fun updateItems(items: List<ShoppingItem>) = viewModelScope.launch {
        repository.updateShoppingItems(items)
    }
    
    fun updateShoppingLists(shoppingLists: List<ShoppingList>) = viewModelScope.launch {
        repository.updateShoppingLists(shoppingLists)
    }

    class ShoppingViewModelFactory(private val repository: ShoppingRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShoppingViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ShoppingViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
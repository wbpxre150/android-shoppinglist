package com.example.shoppinglist

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ShoppingViewModel(private val repository: ShoppingRepository) : ViewModel() {

    val allShoppingLists: LiveData<List<ShoppingList>> = repository.allShoppingLists
    
    private val _allShoppingListsWithCounts = MediatorLiveData<List<ShoppingListWithCount>>()
    val allShoppingListsWithCounts: LiveData<List<ShoppingListWithCount>> = _allShoppingListsWithCounts
    
    private val listCountsMap = mutableMapOf<Int, Pair<Int, Int>>() // listId -> (itemCount, purchasedCount)
    private val listObserversMap = mutableMapOf<Int, Pair<LiveData<Int>, LiveData<Int>>>() // listId -> (itemCountSource, purchasedCountSource)
    
    init {
        _allShoppingListsWithCounts.addSource(allShoppingLists) { lists ->
            updateConsolidatedLists(lists)
        }
    }

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
    
    private fun updateConsolidatedLists(lists: List<ShoppingList>?) {
        if (lists.isNullOrEmpty()) {
            // Clean up all observers when no lists exist
            cleanupAllObservers()
            _allShoppingListsWithCounts.value = emptyList()
            return
        }
        
        // Remove observers for lists that no longer exist
        val currentListIds = lists.map { it.id }.toSet()
        val obsoleteListIds = listCountsMap.keys - currentListIds
        obsoleteListIds.forEach { listId ->
            removeObserversForList(listId)
        }
        
        // Set up observers for new lists
        lists.forEach { list ->
            val listId = list.id
            if (!listCountsMap.containsKey(listId)) {
                addObserversForList(listId)
            }
        }
        
        // Emit current state using fresh data
        emitConsolidatedListsFromCurrentData()
    }
    
    private fun removeObserversForList(listId: Int) {
        // Remove from data maps
        listCountsMap.remove(listId)
        
        // Remove LiveData sources if they exist
        listObserversMap[listId]?.let { (itemCountSource, purchasedCountSource) ->
            _allShoppingListsWithCounts.removeSource(itemCountSource)
            _allShoppingListsWithCounts.removeSource(purchasedCountSource)
        }
        listObserversMap.remove(listId)
    }
    
    private fun addObserversForList(listId: Int) {
        // Validate listId
        if (listId <= 0) return
        
        // Prevent duplicate observers
        if (listObserversMap.containsKey(listId)) return
        
        // Initialize counts to 0
        listCountsMap[listId] = Pair(0, 0)
        
        // Create LiveData sources
        val itemCountSource = repository.getItemCountForList(listId)
        val purchasedCountSource = repository.getPurchasedItemCountForList(listId)
        
        // Store sources for cleanup
        listObserversMap[listId] = Pair(itemCountSource, purchasedCountSource)
        
        // Add observers that get fresh data with validation
        _allShoppingListsWithCounts.addSource(itemCountSource) { itemCount ->
            if (itemCount != null && listCountsMap.containsKey(listId)) {
                val currentCounts = listCountsMap[listId] ?: Pair(0, 0)
                listCountsMap[listId] = Pair(itemCount, currentCounts.second)
                emitConsolidatedListsFromCurrentData()
            }
        }
        
        _allShoppingListsWithCounts.addSource(purchasedCountSource) { purchasedCount ->
            if (purchasedCount != null && listCountsMap.containsKey(listId)) {
                val currentCounts = listCountsMap[listId] ?: Pair(0, 0)
                listCountsMap[listId] = Pair(currentCounts.first, purchasedCount)
                emitConsolidatedListsFromCurrentData()
            }
        }
    }
    
    private fun cleanupAllObservers() {
        listObserversMap.values.forEach { (itemCountSource, purchasedCountSource) ->
            _allShoppingListsWithCounts.removeSource(itemCountSource)
            _allShoppingListsWithCounts.removeSource(purchasedCountSource)
        }
        listObserversMap.clear()
        listCountsMap.clear()
    }
    
    private fun emitConsolidatedListsFromCurrentData() {
        // Get current lists from the primary source of truth
        val currentLists = allShoppingLists.value ?: emptyList()
        
        val listsWithCounts = currentLists.map { list ->
            val counts = listCountsMap[list.id] ?: Pair(0, 0)
            ShoppingListWithCount(
                shoppingList = list,
                itemCount = counts.first,
                purchasedCount = counts.second
            )
        }.sortedWith(compareBy({ it.shoppingList.position }, { it.shoppingList.name }))
        
        _allShoppingListsWithCounts.value = listsWithCounts
    }
    
    override fun onCleared() {
        super.onCleared()
        cleanupAllObservers()
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
package com.example.shoppinglist

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ShoppingApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { ShoppingDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { 
        ShoppingRepository(
            database.shoppingListDao(),
            database.shoppingItemDao()
        ) 
    }
}
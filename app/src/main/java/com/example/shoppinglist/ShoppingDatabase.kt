package com.example.shoppinglist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ShoppingList::class, ShoppingItem::class], version = 2, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun shoppingItemDao(): ShoppingItemDao

    companion object {
        @Volatile
        private var INSTANCE: ShoppingDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ShoppingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(ShoppingDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class ShoppingDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        // Add sample data if needed
                        // For example:
                        // val listDao = database.shoppingListDao()
                        // val itemDao = database.shoppingItemDao()
                        // val groceryList = ShoppingList(name = "Groceries", position = 0)
                        // val listId = listDao.insert(groceryList).toInt()
                        // itemDao.insert(ShoppingItem(listId = listId, name = "Milk", quantity = 1))
                        // itemDao.insert(ShoppingItem(listId = listId, name = "Eggs", quantity = 12))
                    }
                }
            }
        }
    }
}
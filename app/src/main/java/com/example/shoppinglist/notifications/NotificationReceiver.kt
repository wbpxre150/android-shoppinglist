package com.example.shoppinglist.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.ShoppingApplication
import com.example.shoppinglist.ShoppingListDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NotificationReceiver : BroadcastReceiver() {
    
    private val receiverScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    override fun onReceive(context: Context, intent: Intent) {
        val application = context.applicationContext as ShoppingApplication
        val repository = application.repository
        val notificationManager = ShoppingNotificationManager.getInstance(context)
        
        when (intent.action) {
            "SHOW_REMINDER" -> {
                val itemId = intent.getIntExtra(ShoppingNotificationManager.EXTRA_ITEM_ID, -1)
                val listId = intent.getIntExtra(ShoppingNotificationManager.EXTRA_LIST_ID, -1)
                
                if (itemId != -1 && listId != -1) {
                    receiverScope.launch {
                        try {
                            val item = repository.getItemById(itemId)
                            val shoppingList = repository.getShoppingListByIdSync(listId)
                            
                            if (item != null && shoppingList != null && !item.isPurchased) {
                                notificationManager.showNotification(item, shoppingList)
                            }
                        } catch (e: Exception) {
                            // Handle database errors gracefully
                        }
                    }
                }
            }
            
            ShoppingNotificationManager.ACTION_MARK_PURCHASED -> {
                val itemId = intent.getIntExtra(ShoppingNotificationManager.EXTRA_ITEM_ID, -1)
                val notificationId = intent.getIntExtra(ShoppingNotificationManager.EXTRA_NOTIFICATION_ID, -1)
                
                if (itemId != -1) {
                    receiverScope.launch {
                        try {
                            val item = repository.getItemById(itemId)
                            if (item != null && !item.isPurchased) {
                                val updatedItem = item.copy(isPurchased = true)
                                repository.updateItem(updatedItem)
                                
                                // Cancel any future notifications for this item
                                notificationManager.cancelNotification(itemId)
                            }
                        } catch (e: Exception) {
                            // Handle database errors gracefully
                        }
                    }
                    
                    // Dismiss the notification immediately
                    if (notificationId != -1) {
                        notificationManager.cancelNotification(notificationId)
                    }
                }
            }
            
            ShoppingNotificationManager.ACTION_SNOOZE -> {
                val itemId = intent.getIntExtra(ShoppingNotificationManager.EXTRA_ITEM_ID, -1)
                val listId = intent.getIntExtra(ShoppingNotificationManager.EXTRA_LIST_ID, -1)
                val notificationId = intent.getIntExtra(ShoppingNotificationManager.EXTRA_NOTIFICATION_ID, -1)
                
                if (itemId != -1 && listId != -1) {
                    receiverScope.launch {
                        try {
                            val item = repository.getItemById(itemId)
                            val shoppingList = repository.getShoppingListByIdSync(listId)
                            
                            if (item != null && shoppingList != null && !item.isPurchased) {
                                // Schedule notification for 30 minutes from now
                                val snoozeTime = System.currentTimeMillis() + 
                                    (ShoppingNotificationManager.SNOOZE_DURATION_MINUTES * 60 * 1000)
                                
                                val snoozedItem = item.copy(reminderDateTime = snoozeTime)
                                repository.updateItem(snoozedItem)
                                
                                // Cancel current notification and schedule new one
                                notificationManager.cancelNotification(itemId)
                                notificationManager.scheduleNotification(snoozedItem, shoppingList)
                            }
                        } catch (e: Exception) {
                            // Handle database errors gracefully
                        }
                    }
                    
                    // Dismiss the current notification
                    if (notificationId != -1) {
                        notificationManager.cancelNotification(notificationId)
                    }
                }
            }
            
            ShoppingNotificationManager.ACTION_VIEW_LIST -> {
                val listId = intent.getIntExtra(ShoppingNotificationManager.EXTRA_LIST_ID, -1)
                val notificationId = intent.getIntExtra(ShoppingNotificationManager.EXTRA_NOTIFICATION_ID, -1)
                
                if (listId != -1) {
                    // Open the shopping list detail activity
                    val viewIntent = Intent(context, ShoppingListDetailActivity::class.java).apply {
                        putExtra("SHOPPING_LIST_ID", listId)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    context.startActivity(viewIntent)
                    
                    // Dismiss the notification
                    if (notificationId != -1) {
                        notificationManager.cancelNotification(notificationId)
                    }
                }
            }
        }
    }
}
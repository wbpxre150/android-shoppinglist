package com.example.shoppinglist.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.shoppinglist.ShoppingApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    
    private val receiverScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val application = context.applicationContext as ShoppingApplication
            val repository = application.repository
            val notificationManager = ShoppingNotificationManager.getInstance(context)
            
            // Reschedule all future notifications after device reboot
            receiverScope.launch {
                try {
                    val itemsWithReminders = repository.getItemsWithFutureReminders()
                    notificationManager.rescheduleAllNotifications(itemsWithReminders)
                } catch (e: Exception) {
                    // Handle database errors gracefully
                }
            }
        }
    }
}
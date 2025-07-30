package com.example.shoppinglist.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.shoppinglist.MainActivity
import com.example.shoppinglist.R
import com.example.shoppinglist.ShoppingItem
import com.example.shoppinglist.ShoppingList
import com.example.shoppinglist.ShoppingListDetailActivity

class ShoppingNotificationManager private constructor(private val context: Context) {
    
    companion object {
        private const val CHANNEL_ID = "shopping_reminders"
        private const val CHANNEL_NAME = "Shopping Reminders"
        private const val CHANNEL_DESCRIPTION = "Notifications for shopping list item reminders"
        
        const val ACTION_MARK_PURCHASED = "com.example.shoppinglist.ACTION_MARK_PURCHASED"
        const val ACTION_SNOOZE = "com.example.shoppinglist.ACTION_SNOOZE"
        const val ACTION_VIEW_LIST = "com.example.shoppinglist.ACTION_VIEW_LIST"
        
        const val EXTRA_ITEM_ID = "extra_item_id"
        const val EXTRA_LIST_ID = "extra_list_id"
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
        
        const val SNOOZE_DURATION_MINUTES = 30
        
        @Volatile
        private var INSTANCE: ShoppingNotificationManager? = null
        
        fun getInstance(context: Context): ShoppingNotificationManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ShoppingNotificationManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    private val notificationManager = NotificationManagerCompat.from(context)
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    
    init {
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
            }
            
            val systemNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            systemNotificationManager.createNotificationChannel(channel)
        }
    }
    
    fun scheduleNotification(item: ShoppingItem, shoppingList: ShoppingList) {
        item.reminderDateTime?.let { reminderTime ->
            if (reminderTime > System.currentTimeMillis()) {
                val intent = Intent(context, NotificationReceiver::class.java).apply {
                    action = "SHOW_REMINDER"
                    putExtra(EXTRA_ITEM_ID, item.id)
                    putExtra(EXTRA_LIST_ID, item.listId)
                }
                
                val pendingIntent = getPendingIntent(item.id, intent)
                
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            reminderTime,
                            pendingIntent
                        )
                    } else {
                        alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            reminderTime,
                            pendingIntent
                        )
                    }
                } catch (e: SecurityException) {
                    // Fallback to inexact alarm if exact alarm permission is denied
                    alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent)
                }
            }
        }
    }
    
    fun cancelNotification(itemId: Int) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = getPendingIntent(itemId, intent)
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
        
        // Also dismiss any currently shown notification
        notificationManager.cancel(itemId)
    }
    
    @SuppressLint("MissingPermission")
    fun showNotification(item: ShoppingItem, shoppingList: ShoppingList) {
        if (!areNotificationsEnabled()) {
            return
        }
        
        val mainActivityIntent = Intent(context, MainActivity::class.java)
        val mainPendingIntent = PendingIntent.getActivity(
            context, 0, mainActivityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val markPurchasedIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_MARK_PURCHASED
            putExtra(EXTRA_ITEM_ID, item.id)
            putExtra(EXTRA_NOTIFICATION_ID, item.id)
        }
        val markPurchasedPendingIntent = PendingIntent.getBroadcast(
            context, item.id * 1000 + 1, markPurchasedIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val snoozeIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_SNOOZE
            putExtra(EXTRA_ITEM_ID, item.id)
            putExtra(EXTRA_LIST_ID, item.listId)
            putExtra(EXTRA_NOTIFICATION_ID, item.id)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context, item.id * 1000 + 2, snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val viewListIntent = Intent(context, ShoppingListDetailActivity::class.java).apply {
            putExtra(ShoppingListDetailActivity.EXTRA_LIST_ID, item.listId)
            putExtra(ShoppingListDetailActivity.EXTRA_LIST_NAME, shoppingList.name)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val viewListPendingIntent = PendingIntent.getActivity(
            context, item.id * 1000 + 3, viewListIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val quantityText = if (item.quantity > 1) " (Qty: ${item.quantity})" else ""
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_agenda)
            .setContentTitle("Shopping Reminder")
            .setContentText("${item.name}$quantityText - ${shoppingList.name}")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Don't forget to buy ${item.name}$quantityText from your ${shoppingList.name} list!"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(mainPendingIntent)
            .setAutoCancel(true)
            .addAction(
                android.R.drawable.ic_menu_save,
                "Mark Purchased",
                markPurchasedPendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_recent_history,
                "Snooze 30m",
                snoozePendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_view,
                "View List",
                viewListPendingIntent
            )
            .build()
        
        notificationManager.notify(item.id, notification)
    }
    
    fun rescheduleAllNotifications(itemsWithReminders: List<Pair<ShoppingItem, ShoppingList>>) {
        val currentTime = System.currentTimeMillis()
        itemsWithReminders.forEach { (item, shoppingList) ->
            item.reminderDateTime?.let { reminderTime ->
                if (reminderTime > currentTime) {
                    scheduleNotification(item, shoppingList)
                }
            }
        }
    }
    
    private fun getPendingIntent(itemId: Int, intent: Intent): PendingIntent {
        return PendingIntent.getBroadcast(
            context, itemId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    
    private fun areNotificationsEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationManager.areNotificationsEnabled()
        } else {
            true
        }
    }
    
    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            areNotificationsEnabled()
        } else {
            true
        }
    }
    
    fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }
}
package com.example.shoppinglist

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import java.util.Calendar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.databinding.ActivityShoppingListDetailBinding
import com.example.shoppinglist.notifications.ShoppingNotificationManager

class ShoppingListDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingListDetailBinding
    private lateinit var shoppingViewModel: ShoppingViewModel
    private lateinit var adapter: ShoppingItemAdapter
    private lateinit var notificationManager: ShoppingNotificationManager
    private var listId: Int = -1
    private var listName: String = ""
    private var isAddItemExpanded = true
    private var totalPriceView: TextView? = null
    
    companion object {
        const val EXTRA_LIST_ID = "extra_list_id"
        const val EXTRA_LIST_NAME = "extra_list_name"
        const val KEY_ADD_ITEM_EXPANDED = "key_add_item_expanded"
        private const val REQUEST_NOTIFICATION_PERMISSION = 1001
        private const val REQUEST_EXACT_ALARM_PERMISSION = 1002
    }
    
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle back button click
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get list info from intent
        listId = intent.getIntExtra(EXTRA_LIST_ID, -1)
        listName = intent.getStringExtra(EXTRA_LIST_NAME) ?: ""
        
        // Restore state
        savedInstanceState?.let {
            isAddItemExpanded = it.getBoolean(KEY_ADD_ITEM_EXPANDED, true)
        }
        
        
        // Set up action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = listName
        supportActionBar?.setDisplayShowTitleEnabled(true)
        
        // Add custom total price view to action bar
        val customView = layoutInflater.inflate(R.layout.actionbar_total_price, null)
        totalPriceView = customView.findViewById(R.id.textViewTotalPrice)
        
        // Set layout parameters to only take needed width on the right
        val layoutParams = androidx.appcompat.app.ActionBar.LayoutParams(
            androidx.appcompat.app.ActionBar.LayoutParams.WRAP_CONTENT,
            androidx.appcompat.app.ActionBar.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = Gravity.END or Gravity.CENTER_VERTICAL
        
        supportActionBar?.setCustomView(customView, layoutParams)
        supportActionBar?.setDisplayShowCustomEnabled(true)

        // Set up RecyclerView
        adapter = ShoppingItemAdapter(
            onItemCheckedChanged = { item, isChecked ->
                val updatedItem = item.copy(isPurchased = isChecked)
                // Cancel notification if item is marked as purchased
                if (isChecked) {
                    notificationManager.cancelNotification(item.id)
                }
                shoppingViewModel.updateItem(updatedItem)
            },
            onDeleteClicked = { item ->
                // Cancel any pending notifications for this item
                notificationManager.cancelNotification(item.id)
                shoppingViewModel.deleteItem(item)
                Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show()
            },
            onItemClicked = { item ->
                showEditItemDialog(item)
            },
            onStartDrag = { viewHolder ->
                itemTouchHelper.startDrag(viewHolder)
            },
            onPriceClicked = { item ->
                showPriceDialog(item)
            }
        )

        binding.recyclerViewItems.adapter = adapter
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(this)
        
        // Set up ItemTouchHelper for drag & drop
        itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                
                // Move item in adapter
                adapter.moveItem(fromPosition, toPosition)
                
                return true
            }
            
            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                
                when (actionState) {
                    ItemTouchHelper.ACTION_STATE_DRAG -> {
                        // Store original background and add highlight border when drag starts
                        viewHolder?.itemView?.let { itemView ->
                            itemView.tag = itemView.background
                            itemView.setBackgroundResource(R.drawable.item_drag_highlight)
                        }
                    }
                }
            }
            
            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                
                // Restore original background when drag ends
                val originalBackground = viewHolder.itemView.tag as? android.graphics.drawable.Drawable
                if (originalBackground != null) {
                    viewHolder.itemView.background = originalBackground
                    viewHolder.itemView.tag = null
                } else {
                    // Fallback: remove any background to let CardView show through
                    viewHolder.itemView.background = null
                }
                
                // Update positions in database once the drag is complete
                updateItemPositions()
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Not used for drag & drop
            }
        })
        
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewItems)

        // Get ViewModel
        val factory = ShoppingViewModel.ShoppingViewModelFactory((application as ShoppingApplication).repository)
        shoppingViewModel = ViewModelProvider(this, factory)[ShoppingViewModel::class.java]
        
        // Initialize notification manager
        notificationManager = ShoppingNotificationManager.getInstance(this)

        // Observe items for this list
        shoppingViewModel.getItemsForList(listId).observe(this) { items ->
            if (items.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.recyclerViewItems.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.recyclerViewItems.visibility = View.VISIBLE
                
                // Force layout recalculation after visibility change
                binding.recyclerViewItems.post {
                    binding.recyclerViewItems.requestLayout()
                }
            }
            
            adapter.submitList(items)
        }
        
        // Observe total price for this list
        shoppingViewModel.getTotalPriceForList(listId).observe(this) { totalPrice ->
            val price = totalPrice ?: 0.0
            totalPriceView?.let { priceView ->
                if (price > 0.0) {
                    priceView.text = String.format("$%.2f", price)
                    priceView.visibility = View.VISIBLE
                } else {
                    priceView.visibility = View.GONE
                }
            }
        }

        // Add item button handler
        binding.buttonAddItem.setOnClickListener {
            val itemName = binding.editTextItemName.text?.toString()?.trim() ?: ""
            val quantityStr = binding.editTextItemQuantity.text?.toString()?.trim() ?: ""
            val quantity = if (quantityStr.isEmpty()) 1 else quantityStr.toInt()

            if (TextUtils.isEmpty(itemName)) {
                Toast.makeText(this, R.string.empty_item_not_saved, Toast.LENGTH_LONG).show()
            } else {
                // Get position for new item (at the end of the list)
                val position = adapter.currentList.size
                
                val newItem = ShoppingItem(
                    listId = listId,
                    name = itemName,
                    quantity = quantity,
                    position = position,
                    price = 0.0
                )
                shoppingViewModel.insertItem(newItem)
                
                // Clear input fields
                binding.editTextItemName.text?.clear()
                binding.editTextItemQuantity.text?.clear()
                binding.editTextItemName.requestFocus()
            }
        }

        
        // Delete list button handler
        binding.fabDeleteList.setOnClickListener {
            showDeleteConfirmationDialog()
        }
        
        // Set up minimize/maximize functionality
        setupAddItemToggle()
        updateAddItemUI()
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_ADD_ITEM_EXPANDED, isAddItemExpanded)
    }
    
    private fun setupAddItemToggle() {
        // Minimize button click
        binding.buttonMinimize.setOnClickListener {
            toggleAddItemState()
        }
        
        // Card click to expand when minimized
        binding.cardAddItem.setOnClickListener {
            if (!isAddItemExpanded) {
                expandAddItem()
            }
        }
    }
    
    private fun toggleAddItemState() {
        if (isAddItemExpanded) {
            minimizeAddItem()
        } else {
            expandAddItem()
        }
    }
    
    private fun expandAddItem() {
        isAddItemExpanded = true
        updateAddItemUI()
        
        // Focus on name field and show keyboard
        binding.editTextItemName.requestFocus()
        binding.editTextItemName.postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.editTextItemName, InputMethodManager.SHOW_IMPLICIT)
        }, 100)
    }
    
    private fun minimizeAddItem() {
        isAddItemExpanded = false
        
        // Hide keyboard
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editTextItemName.windowToken, 0)
        
        updateAddItemUI()
    }
    
    private fun updateAddItemUI() {
        if (isAddItemExpanded) {
            // Show expanded state with animation
            binding.layoutFormContent.visibility = View.VISIBLE
            binding.layoutFormContent.alpha = 0f
            binding.layoutFormContent.animate()
                .alpha(1f)
                .setDuration(200)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
            
            binding.textViewAddItemTitle.text = "Add New Item"
            binding.buttonMinimize.setIconResource(R.drawable.ic_minimize_24)
            
            // Reset header margin to normal
            val headerLayoutParams = binding.layoutHeader.layoutParams as android.view.ViewGroup.MarginLayoutParams
            headerLayoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.spacing_l)
            binding.layoutHeader.layoutParams = headerLayoutParams
            
            // Enable card click interception when expanded
            binding.cardAddItem.isFocusable = false
            binding.cardAddItem.isClickable = false
        } else {
            // Show minimized state with animation
            binding.layoutFormContent.animate()
                .alpha(0f)
                .setDuration(150)
                .withEndAction {
                    binding.layoutFormContent.visibility = View.GONE
                }
                .start()
            
            binding.textViewAddItemTitle.text = "Add New Item"
            binding.buttonMinimize.setIconResource(R.drawable.ic_expand_more_24)
            
            // Reduce header margin for compact minimized state
            val headerLayoutParams = binding.layoutHeader.layoutParams as android.view.ViewGroup.MarginLayoutParams
            headerLayoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.spacing_xs)
            binding.layoutHeader.layoutParams = headerLayoutParams
            
            // Enable card click when minimized
            binding.cardAddItem.isFocusable = true
            binding.cardAddItem.isClickable = true
        }
    }
    
    private lateinit var itemTouchHelper: ItemTouchHelper
    
    private fun updateItemPositions() {
        val currentItems = adapter.currentList.toMutableList()
        // Update positions based on current adapter order
        val updatedItems = currentItems.mapIndexed { index, item ->
            item.copy(position = index)
        }
        // Save to database
        shoppingViewModel.updateItems(updatedItems)
    }
    
    private fun showEditItemDialog(item: ShoppingItem) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_item, null)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
        val editTextQuantity = dialogView.findViewById<EditText>(R.id.editTextQuantity)
        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)
        val buttonReminder = dialogView.findViewById<Button>(R.id.buttonReminder)
        val buttonSave = dialogView.findViewById<Button>(R.id.buttonSave)
        
        // Set current values
        editTextName.setText(item.name)
        editTextQuantity.setText(item.quantity.toString())
        
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.edit_item)
            .setView(dialogView)
            .create()
            
        // Set up custom button listeners
        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        buttonReminder.setOnClickListener {
            showSetReminderDialog(item, dialog, editTextName, editTextQuantity)
        }
        
        buttonSave.setOnClickListener {
            val newName = editTextName.text.toString().trim()
            val quantityStr = editTextQuantity.text.toString().trim()
            val newQuantity = if (quantityStr.isEmpty()) 1 else quantityStr.toInt()
            
            if (newName.isNotEmpty()) {
                // Create updated item
                val updatedItem = item.copy(
                    name = newName,
                    quantity = newQuantity
                )
                
                // Update in database
                shoppingViewModel.updateItem(updatedItem)
                Toast.makeText(this, R.string.item_updated, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, R.string.item_name_empty, Toast.LENGTH_SHORT).show()
            }
        }
            
        dialog.show()
        
        // Show keyboard automatically for dialog
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        editTextName.requestFocus()
    }
    
    private fun showPriceDialog(item: ShoppingItem) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_price_input, null)
        val editTextPrice = dialogView.findViewById<EditText>(R.id.editTextPrice)
        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)
        val buttonSave = dialogView.findViewById<Button>(R.id.buttonSave)
        
        // Set current price if exists
        if (item.price > 0.0) {
            editTextPrice.setText(String.format("%.2f", item.price))
        }
        
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
            
        // Set up custom button listeners
        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        buttonSave.setOnClickListener {
            val priceStr = editTextPrice.text.toString().trim()
            val price = if (priceStr.isEmpty()) 0.0 else {
                try {
                    priceStr.toDouble()
                } catch (e: NumberFormatException) {
                    0.0
                }
            }
            
            if (price >= 0.0) {
                // Create updated item with new price
                val updatedItem = item.copy(price = price)
                
                // Update in database
                shoppingViewModel.updateItem(updatedItem)
                Toast.makeText(this, "Price updated", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show()
            }
        }
            
        dialog.show()
        
        // Show keyboard automatically for dialog
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        editTextPrice.requestFocus()
        editTextPrice.postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editTextPrice, InputMethodManager.SHOW_IMPLICIT)
        }, 100)
    }

    private fun showSetReminderDialog(item: ShoppingItem, parentDialog: AlertDialog, editTextName: EditText, editTextQuantity: EditText) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_set_reminder, null)
        val dayPicker = dialogView.findViewById<NumberPicker>(R.id.dayPicker)
        val monthPicker = dialogView.findViewById<NumberPicker>(R.id.monthPicker)
        val yearPicker = dialogView.findViewById<NumberPicker>(R.id.yearPicker)
        val timePicker = dialogView.findViewById<TimePicker>(R.id.timePicker)
        val buttonCancelReminder = dialogView.findViewById<Button>(R.id.buttonCancelReminder)
        val buttonSetReminder = dialogView.findViewById<Button>(R.id.buttonSetReminder)
        
        // Set current date and time, or use existing reminder if available and not past
        val calendar = Calendar.getInstance()
        val currentTime = System.currentTimeMillis()
        
        if (item.reminderDateTime != null && item.reminderDateTime > currentTime) {
            // Use existing reminder if it's in the future
            calendar.timeInMillis = item.reminderDateTime
        }
        // Otherwise, calendar already has current date/time
        
        // Setup year picker with range from current year to current year + 10
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        yearPicker.minValue = currentYear
        yearPicker.maxValue = currentYear + 10
        yearPicker.value = calendar.get(Calendar.YEAR)
        
        // Setup month picker with abbreviated month names
        val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                           "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        monthPicker.minValue = 0
        monthPicker.maxValue = 11
        monthPicker.displayedValues = months
        monthPicker.value = calendar.get(Calendar.MONTH)
        
        // Function to update day picker based on selected year and month
        val updateDayPicker = {
            val selectedYear = yearPicker.value
            val selectedMonth = monthPicker.value
            val daysInMonth = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth)
            }.getActualMaximum(Calendar.DAY_OF_MONTH)
            
            val currentDay = dayPicker.value
            dayPicker.minValue = 1
            dayPicker.maxValue = daysInMonth
            
            // Adjust day if it's no longer valid for the new month
            if (currentDay > daysInMonth) {
                dayPicker.value = daysInMonth
            }
        }
        
        // Setup day picker for initial month/year
        updateDayPicker()
        dayPicker.value = calendar.get(Calendar.DAY_OF_MONTH)
        
        // Update day picker when year or month changes
        yearPicker.setOnValueChangedListener { _, _, _ ->
            updateDayPicker()
        }
        
        monthPicker.setOnValueChangedListener { _, _, _ ->
            updateDayPicker()
        }
        
        timePicker.hour = calendar.get(Calendar.HOUR_OF_DAY)
        timePicker.minute = calendar.get(Calendar.MINUTE)
        
        val reminderDialog = AlertDialog.Builder(this)
            .setTitle(R.string.set_reminder_title)
            .setView(dialogView)
            .create()
        
        buttonCancelReminder.setOnClickListener {
            reminderDialog.dismiss()
        }
        
        buttonSetReminder.setOnClickListener {
            // Get selected date and time
            val selectedYear = yearPicker.value
            val selectedMonth = monthPicker.value
            val selectedDay = dayPicker.value
            
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(
                selectedYear,
                selectedMonth,
                selectedDay,
                timePicker.hour,
                timePicker.minute,
                0
            )
            
            val reminderDateTime = selectedCalendar.timeInMillis
            val currentTime = System.currentTimeMillis()
            
            // Check if reminder is in the past
            if (reminderDateTime <= currentTime) {
                Toast.makeText(this, R.string.reminder_past_error, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Get updated item data from parent dialog
            val newName = editTextName.text.toString().trim()
            val quantityStr = editTextQuantity.text.toString().trim()
            val newQuantity = if (quantityStr.isEmpty()) 1 else quantityStr.toInt()
            
            if (newName.isNotEmpty()) {
                // Create updated item with reminder
                val updatedItem = item.copy(
                    name = newName,
                    quantity = newQuantity,
                    reminderDateTime = reminderDateTime
                )
                
                // Update in database
                shoppingViewModel.updateItem(updatedItem)
                
                // Check permissions and schedule notification
                if (checkAndRequestNotificationPermissions()) {
                    val shoppingList = ShoppingList(id = listId, name = listName)
                    notificationManager.scheduleNotification(updatedItem, shoppingList)
                    Toast.makeText(this, R.string.reminder_set, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Reminder saved, but permissions needed for notifications", Toast.LENGTH_LONG).show()
                }
                
                // Close both dialogs
                reminderDialog.dismiss()
                parentDialog.dismiss()
            } else {
                Toast.makeText(this, R.string.item_name_empty, Toast.LENGTH_SHORT).show()
            }
        }
        
        reminderDialog.show()
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Shopping List")
            .setMessage("Are you sure you want to delete this shopping list and all its items?")
            .setPositiveButton("Delete") { _, _ ->
                // Get the full ShoppingList object before deleting using one-time observation
                val shoppingListLiveData = shoppingViewModel.getShoppingListById(listId)
                val observer = object : androidx.lifecycle.Observer<ShoppingList?> {
                    override fun onChanged(value: ShoppingList?) {
                        if (value != null) {
                            // Remove observer immediately to prevent memory leaks
                            shoppingListLiveData.removeObserver(this)
                            shoppingViewModel.delete(value)
                            finish()
                        }
                    }
                }
                shoppingListLiveData.observe(this, observer)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun checkAndRequestNotificationPermissions(): Boolean {
        // Check notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) 
                != PackageManager.PERMISSION_GRANTED) {
                
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                    // Show explanation dialog
                    showPermissionExplanationDialog()
                } else {
                    // Request permission directly
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        REQUEST_NOTIFICATION_PERMISSION
                    )
                }
                return false
            }
        }
        
        // Check exact alarm permission for Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!notificationManager.canScheduleExactAlarms()) {
                showExactAlarmPermissionDialog()
                return false
            }
        }
        
        return true
    }
    
    private fun showPermissionExplanationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Notification Permission Required")
            .setMessage("This app needs notification permission to remind you about your shopping items. Please grant the permission to enable reminders.")
            .setPositiveButton("Grant Permission") { _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showExactAlarmPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exact Alarm Permission Required")
            .setMessage("This app needs permission to schedule exact alarms for precise reminders. Please enable this in Settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                        data = Uri.parse("package:$packageName")
                    }
                    startActivity(intent)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        when (requestCode) {
            REQUEST_NOTIFICATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Notification permission denied. Reminders may not work.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
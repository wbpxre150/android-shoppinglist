package com.example.shoppinglist

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.databinding.ActivityShoppingListDetailBinding

class ShoppingListDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingListDetailBinding
    private lateinit var shoppingViewModel: ShoppingViewModel
    private lateinit var adapter: ShoppingItemAdapter
    private var listId: Int = -1
    private var listName: String = ""
    private var isAddItemMinimized: Boolean = false
    private lateinit var sharedPreferences: SharedPreferences
    
    companion object {
        const val EXTRA_LIST_ID = "extra_list_id"
        const val EXTRA_LIST_NAME = "extra_list_name"
        private const val PREF_ADD_ITEM_MINIMIZED = "add_item_minimized"
        private const val ANIMATION_DURATION = 300L
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
        
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("shopping_list_prefs", Context.MODE_PRIVATE)
        
        // Restore minimized state
        isAddItemMinimized = sharedPreferences.getBoolean(PREF_ADD_ITEM_MINIMIZED, false)
        
        // Set up action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = listName
        
        // Initialize add item UI state
        updateAddItemVisibility(animate = false)

        // Set up RecyclerView
        adapter = ShoppingItemAdapter(
            onItemCheckedChanged = { item, isChecked ->
                val updatedItem = item.copy(isPurchased = isChecked)
                shoppingViewModel.updateItem(updatedItem)
            },
            onDeleteClicked = { item ->
                shoppingViewModel.deleteItem(item)
                Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show()
            },
            onItemClicked = { item ->
                showEditItemDialog(item)
            },
            onStartDrag = { viewHolder ->
                itemTouchHelper.startDrag(viewHolder)
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
                    position = position
                )
                shoppingViewModel.insertItem(newItem)
                
                // Clear input fields
                binding.editTextItemName.text?.clear()
                binding.editTextItemQuantity.text?.clear()
                binding.editTextItemName.requestFocus()
            }
        }

        // Minimize button handler
        binding.buttonMinimize.setOnClickListener {
            minimizeAddItem()
        }
        
        // Minimized card click handler
        binding.cardAddItemMinimized.setOnClickListener {
            expandAddItem()
        }
        
        // Delete list button handler
        binding.fabDeleteList.setOnClickListener {
            showDeleteConfirmationDialog()
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

    private fun minimizeAddItem() {
        if (isAddItemMinimized) return
        
        isAddItemMinimized = true
        saveMinimizedState()
        hideKeyboard()
        clearInputFields()
        updateAddItemVisibility(animate = true)
    }
    
    private fun expandAddItem() {
        if (!isAddItemMinimized) return
        
        isAddItemMinimized = false
        saveMinimizedState()
        updateAddItemVisibility(animate = true)
        
        // Focus and show keyboard after animation
        binding.root.postDelayed({
            binding.editTextItemName.requestFocus()
            showKeyboard(binding.editTextItemName)
        }, ANIMATION_DURATION / 2)
    }
    
    private fun updateAddItemVisibility(animate: Boolean) {
        if (animate) {
            animateCardTransition()
        } else {
            binding.cardAddItem.visibility = if (isAddItemMinimized) View.GONE else View.VISIBLE
            binding.cardAddItemMinimized.visibility = if (isAddItemMinimized) View.VISIBLE else View.GONE
        }
    }
    
    private fun animateCardTransition() {
        val expandedCard = binding.cardAddItem
        val minimizedCard = binding.cardAddItemMinimized
        
        if (isAddItemMinimized) {
            // Animate from expanded to minimized
            val fadeOutAnimator = ObjectAnimator.ofFloat(expandedCard, "alpha", 1f, 0f)
            val fadeInAnimator = ObjectAnimator.ofFloat(minimizedCard, "alpha", 0f, 1f)
            
            fadeOutAnimator.duration = ANIMATION_DURATION / 2
            fadeInAnimator.duration = ANIMATION_DURATION / 2
            fadeInAnimator.startDelay = ANIMATION_DURATION / 2
            
            fadeOutAnimator.interpolator = AccelerateDecelerateInterpolator()
            fadeInAnimator.interpolator = AccelerateDecelerateInterpolator()
            
            fadeOutAnimator.addUpdateListener { animator ->
                if (animator.animatedFraction == 1f) {
                    expandedCard.visibility = View.GONE
                    minimizedCard.visibility = View.VISIBLE
                    minimizedCard.alpha = 0f
                }
            }
            
            fadeOutAnimator.start()
            fadeInAnimator.start()
        } else {
            // Animate from minimized to expanded
            val fadeOutAnimator = ObjectAnimator.ofFloat(minimizedCard, "alpha", 1f, 0f)
            val fadeInAnimator = ObjectAnimator.ofFloat(expandedCard, "alpha", 0f, 1f)
            
            fadeOutAnimator.duration = ANIMATION_DURATION / 2
            fadeInAnimator.duration = ANIMATION_DURATION / 2
            fadeInAnimator.startDelay = ANIMATION_DURATION / 2
            
            fadeOutAnimator.interpolator = AccelerateDecelerateInterpolator()
            fadeInAnimator.interpolator = AccelerateDecelerateInterpolator()
            
            fadeOutAnimator.addUpdateListener { animator ->
                if (animator.animatedFraction == 1f) {
                    minimizedCard.visibility = View.GONE
                    expandedCard.visibility = View.VISIBLE
                    expandedCard.alpha = 0f
                }
            }
            
            fadeOutAnimator.start()
            fadeInAnimator.start()
        }
    }
    
    private fun showKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
    
    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = currentFocus
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
            currentFocus.clearFocus()
        }
    }
    
    private fun clearInputFields() {
        binding.editTextItemName.text?.clear()
        binding.editTextItemQuantity.setText("1")
    }
    
    private fun saveMinimizedState() {
        sharedPreferences.edit()
            .putBoolean(PREF_ADD_ITEM_MINIMIZED, isAddItemMinimized)
            .apply()
    }
}
package com.example.shoppinglist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var shoppingViewModel: ShoppingViewModel
    private lateinit var adapter: ShoppingListAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper

    private val newShoppingListLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getStringExtra(NewShoppingListActivity.EXTRA_REPLY)?.let { listName ->
                val currentListCount = shoppingViewModel.allShoppingLists.value?.size ?: 0
                val shoppingList = ShoppingList(name = listName, position = currentListCount)
                shoppingViewModel.insert(shoppingList)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ShoppingListAdapter(
            onListClicked = { shoppingList ->
                val intent = Intent(this, ShoppingListDetailActivity::class.java).apply {
                    putExtra(ShoppingListDetailActivity.EXTRA_LIST_ID, shoppingList.id)
                    putExtra(ShoppingListDetailActivity.EXTRA_LIST_NAME, shoppingList.name)
                }
                startActivity(intent)
            },
            onStartDrag = { viewHolder ->
                itemTouchHelper.startDrag(viewHolder)
            }
        )

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        
        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                adapter.moveItem(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Not used
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
                
                updateListPositions()
            }
        }
        
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        // Get a new or existing ViewModel from the ViewModelProvider
        val factory = ShoppingViewModel.ShoppingViewModelFactory((application as ShoppingApplication).repository)
        shoppingViewModel = ViewModelProvider(this, factory)[ShoppingViewModel::class.java]

        // Create a composite list with item counts
        shoppingViewModel.allShoppingLists.observe(this) { lists ->
            val listWithCounts = mutableListOf<ShoppingListWithCount>()
            
            if (lists.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                adapter.submitList(listWithCounts)
            } else {
                binding.emptyView.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                
                // We'll submit the list once we have all item counts
                var listsProcessed = 0
                
                for (list in lists) {
                    val listId = list.id
                    var itemCount = 0
                    var purchasedCount = 0
                    
                    shoppingViewModel.getItemCountForList(listId).observe(this) { count ->
                        itemCount = count
                        updateListWithCount(list, itemCount, purchasedCount, lists.size, listWithCounts)
                        listsProcessed++
                    }
                    
                    shoppingViewModel.getPurchasedItemCountForList(listId).observe(this) { count ->
                        purchasedCount = count
                        updateListWithCount(list, itemCount, purchasedCount, lists.size, listWithCounts)
                        listsProcessed++
                    }
                }
            }
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this, NewShoppingListActivity::class.java)
            newShoppingListLauncher.launch(intent)
        }
    }
    
    private fun updateListPositions() {
        val currentItems = adapter.currentList
        val updatedLists = mutableListOf<ShoppingList>()
        
        for (i in currentItems.indices) {
            val item = currentItems[i]
            val updatedList = item.shoppingList.copy(position = i)
            updatedLists.add(updatedList)
        }
        
        if (updatedLists.isNotEmpty()) {
            shoppingViewModel.updateShoppingLists(updatedLists)
        }
    }
    
    private fun updateListWithCount(
        list: ShoppingList,
        itemCount: Int,
        purchasedCount: Int,
        totalLists: Int,
        listWithCounts: MutableList<ShoppingListWithCount>
    ) {
        // Look for existing entry
        val existingIndex = listWithCounts.indexOfFirst { it.shoppingList.id == list.id }
        
        if (existingIndex >= 0) {
            // Update existing entry
            val existing = listWithCounts[existingIndex]
            listWithCounts[existingIndex] = existing.copy(itemCount = itemCount, purchasedCount = purchasedCount)
        } else {
            // Add new entry
            listWithCounts.add(ShoppingListWithCount(list, itemCount, purchasedCount))
        }
        
        // Sort by position, then name for consistency
        listWithCounts.sortWith(compareBy({ it.shoppingList.position }, { it.shoppingList.name }))
        
        // Submit the list when we have processed all lists
        adapter.submitList(listWithCounts.toList())
    }
}
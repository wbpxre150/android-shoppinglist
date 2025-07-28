package com.example.shoppinglist

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

class ShoppingItemAdapter(
    private val onItemCheckedChanged: (ShoppingItem, Boolean) -> Unit,
    private val onDeleteClicked: (ShoppingItem) -> Unit,
    private val onItemClicked: (ShoppingItem) -> Unit,
    private val onStartDrag: (RecyclerView.ViewHolder) -> Unit,
    private val onPriceClicked: (ShoppingItem) -> Unit
) : ListAdapter<ShoppingItem, ShoppingItemAdapter.ShoppingItemViewHolder>(ShoppingItemsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        return ShoppingItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(
            current,
            onItemCheckedChanged = { isChecked -> onItemCheckedChanged(current, isChecked) },
            onDeleteClicked = { onDeleteClicked(current) },
            onItemClicked = { onItemClicked(current) },
            onStartDrag = { onStartDrag(holder) },
            onPriceClicked = { onPriceClicked(current) }
        )
    }
    
    fun moveItem(fromPosition: Int, toPosition: Int) {
        val currentList = currentList.toMutableList()
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(currentList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(currentList, i, i - 1)
            }
        }
        // Use submitList to handle the update - no need for notifyItemMoved
        submitList(currentList)
    }
    
    fun getItemAt(position: Int): ShoppingItem {
        return getItem(position)
    }

    class ShoppingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemNameView: TextView = itemView.findViewById(R.id.textViewItemName)
        private val itemQuantityView: TextView = itemView.findViewById(R.id.textViewItemQuantity)
        private val quantityDisplayView: TextView = itemView.findViewById(R.id.textViewQuantityDisplay)
        private val checkBoxPurchased: CheckBox = itemView.findViewById(R.id.checkBoxPurchased)
        private val buttonDelete: ImageButton = itemView.findViewById(R.id.imageButtonDelete)
        private val buttonPrice: ImageButton = itemView.findViewById(R.id.imageButtonPrice)
        private val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)

        fun bind(
            shoppingItem: ShoppingItem,
            onItemCheckedChanged: (Boolean) -> Unit,
            onDeleteClicked: () -> Unit,
            onItemClicked: () -> Unit,
            onStartDrag: () -> Unit,
            onPriceClicked: () -> Unit
        ) {
            itemNameView.text = shoppingItem.name
            itemQuantityView.text = "Quantity: ${shoppingItem.quantity}"
            
            // Show quantity badge if quantity > 1
            if (shoppingItem.quantity > 1) {
                quantityDisplayView.text = shoppingItem.quantity.toString()
                quantityDisplayView.visibility = View.VISIBLE
            } else {
                quantityDisplayView.visibility = View.GONE
            }
            
            // Set the checked state without triggering the listener
            checkBoxPurchased.setOnCheckedChangeListener(null)
            checkBoxPurchased.isChecked = shoppingItem.isPurchased
            
            // Apply strike-through if purchased
            if (shoppingItem.isPurchased) {
                itemNameView.paintFlags = itemNameView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                itemNameView.paintFlags = itemNameView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            
            // Set up listeners
            checkBoxPurchased.setOnCheckedChangeListener { _, isChecked ->
                onItemCheckedChanged(isChecked)
            }
            
            buttonDelete.setOnClickListener {
                onDeleteClicked()
            }
            
            buttonPrice.setOnClickListener {
                onPriceClicked()
            }
            
            // Display price if set
            if (shoppingItem.price > 0.0) {
                textViewPrice.text = String.format("$%.2f", shoppingItem.price)
                textViewPrice.visibility = View.VISIBLE
            } else {
                textViewPrice.visibility = View.GONE
            }
            
            // Set click listener for editing
            itemView.setOnClickListener {
                onItemClicked()
            }
            
            // Set long click listener for drag & drop
            itemView.setOnLongClickListener {
                onStartDrag()
                true
            }
        }

        companion object {
            fun create(parent: ViewGroup): ShoppingItemViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_shopping_item, parent, false)
                return ShoppingItemViewHolder(view)
            }
        }
    }

    class ShoppingItemsComparator : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem == newItem
        }
    }
}
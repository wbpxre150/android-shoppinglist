package com.example.shoppinglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

class ShoppingListAdapter(
    private val onListClicked: (ShoppingList) -> Unit,
    private val onStartDrag: (RecyclerView.ViewHolder) -> Unit
) : ListAdapter<ShoppingListWithCount, ShoppingListAdapter.ShoppingListViewHolder>(ShoppingListsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        return ShoppingListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, onStartDrag)
        holder.itemView.setOnClickListener { onListClicked(current.shoppingList) }
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
        submitList(currentList)
    }

    class ShoppingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val listNameView: TextView = itemView.findViewById(R.id.textViewListName)
        private val itemCountView: TextView = itemView.findViewById(R.id.textViewItemCount)

        fun bind(shoppingListWithCount: ShoppingListWithCount, onStartDrag: (RecyclerView.ViewHolder) -> Unit) {
            listNameView.text = shoppingListWithCount.shoppingList.name
            val itemCountText = if (shoppingListWithCount.itemCount > 0) {
                "${shoppingListWithCount.purchasedCount}/${shoppingListWithCount.itemCount} items"
            } else {
                "Empty list"
            }
            itemCountView.text = itemCountText
            
            itemView.setOnLongClickListener {
                onStartDrag(this)
                true
            }
        }

        companion object {
            fun create(parent: ViewGroup): ShoppingListViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_shopping_list, parent, false)
                return ShoppingListViewHolder(view)
            }
        }
    }

    class ShoppingListsComparator : DiffUtil.ItemCallback<ShoppingListWithCount>() {
        override fun areItemsTheSame(oldItem: ShoppingListWithCount, newItem: ShoppingListWithCount): Boolean {
            return oldItem.shoppingList.id == newItem.shoppingList.id
        }

        override fun areContentsTheSame(oldItem: ShoppingListWithCount, newItem: ShoppingListWithCount): Boolean {
            return oldItem == newItem
        }
    }
}

data class ShoppingListWithCount(
    val shoppingList: ShoppingList,
    val itemCount: Int = 0,
    val purchasedCount: Int = 0
)
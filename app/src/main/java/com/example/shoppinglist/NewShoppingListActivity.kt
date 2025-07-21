package com.example.shoppinglist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppinglist.databinding.ActivityNewShoppingListBinding

class NewShoppingListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewShoppingListBinding
    
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
        binding = ActivityNewShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup action bar with back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Create New Shopping List"

        // Show keyboard automatically
        binding.editListName.postDelayed({
            binding.editListName.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.editListName, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

        binding.buttonSave.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(binding.editListName.text)) {
                Toast.makeText(this, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
            } else {
                val listName = binding.editListName.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, listName)
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.shoppinglist.REPLY"
    }
}
package com.wbpxre150.shoppinglist

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.wbpxre150.shoppinglist.databinding.ActivityImportListBinding
import com.wbpxre150.shoppinglist.utils.ListImportService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImportListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImportListBinding
    private var isImportInProgress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImportListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the file URI from the intent - handle both ACTION_VIEW and ACTION_SEND
        val fileUri = when (intent.action) {
            Intent.ACTION_VIEW -> intent.data
            Intent.ACTION_SEND -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                }
            }
            else -> null
        }

        if (fileUri == null) {
            showError(getString(R.string.import_error))
            finish()
            return
        }

        // Import the list
        importList(fileUri)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // If a new intent arrives while activity is already running (due to singleTop mode),
        // we ignore it to prevent duplicate imports
        if (isImportInProgress) {
            Toast.makeText(this, "Import already in progress", Toast.LENGTH_SHORT).show()
            return
        }

        // Update the current intent
        setIntent(intent)
    }

    private fun importList(fileUri: Uri) {
        // Prevent multiple simultaneous imports
        if (isImportInProgress) {
            return
        }

        isImportInProgress = true

        lifecycleScope.launch {
            try {
                binding.textViewStatus.text = getString(R.string.importing_list)

                val result = withContext(Dispatchers.IO) {
                    val repository = (application as ShoppingApplication).repository
                    val importService = ListImportService(this@ImportListActivity)

                    // Get existing list names to avoid duplicates
                    val existingNames = repository.getAllListNames()

                    // Import the list
                    val importResult = importService.importList(fileUri, existingNames)

                    when (importResult) {
                        is ListImportService.ImportResult.Success -> {
                            // Insert into database
                            val (list, items) = importResult
                            val newListId = repository.importList(list, items)

                            // Fetch the list details to get the name
                            val importedList = repository.getShoppingListByIdSync(newListId.toInt())

                            if (importedList != null) {
                                Triple(true, newListId.toInt(), importedList.name)
                            } else {
                                Triple(false, 0, "List not found after import")
                            }
                        }
                        is ListImportService.ImportResult.Error -> {
                            Triple(false, 0, importResult.message)
                        }
                    }
                }

                // Handle result
                val success = result.first as Boolean
                val listId = result.second as Int
                val listNameOrError = result.third as String

                if (success) {
                    Toast.makeText(this@ImportListActivity, R.string.import_success, Toast.LENGTH_SHORT).show()

                    // Navigate to the imported list with complete data
                    val intent = Intent(this@ImportListActivity, ShoppingListDetailActivity::class.java).apply {
                        putExtra(ShoppingListDetailActivity.EXTRA_LIST_ID, listId)
                        putExtra(ShoppingListDetailActivity.EXTRA_LIST_NAME, listNameOrError)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    showError(listNameOrError)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                showError(getString(R.string.import_error))
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        // Navigate back to main activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

package com.example.shoppinglist;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0014J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u0010H\u0002J\u0010\u0010\u0018\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u0019H\u0002J\b\u0010\u001a\u001a\u00020\u0010H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/example/shoppinglist/ShoppingListDetailActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "adapter", "Lcom/example/shoppinglist/ShoppingItemAdapter;", "binding", "Lcom/example/shoppinglist/databinding/ActivityShoppingListDetailBinding;", "itemTouchHelper", "Landroidx/recyclerview/widget/ItemTouchHelper;", "listId", "", "listName", "", "shoppingViewModel", "Lcom/example/shoppinglist/ShoppingViewModel;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "showDeleteConfirmationDialog", "showEditItemDialog", "Lcom/example/shoppinglist/ShoppingItem;", "updateItemPositions", "Companion", "app_release"})
public final class ShoppingListDetailActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.example.shoppinglist.databinding.ActivityShoppingListDetailBinding binding;
    private com.example.shoppinglist.ShoppingViewModel shoppingViewModel;
    private com.example.shoppinglist.ShoppingItemAdapter adapter;
    private int listId = -1;
    @org.jetbrains.annotations.NotNull
    private java.lang.String listName = "";
    private androidx.recyclerview.widget.ItemTouchHelper itemTouchHelper;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_LIST_ID = "extra_list_id";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_LIST_NAME = "extra_list_name";
    @org.jetbrains.annotations.NotNull
    public static final com.example.shoppinglist.ShoppingListDetailActivity.Companion Companion = null;
    
    public ShoppingListDetailActivity() {
        super();
    }
    
    @java.lang.Override
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull
    android.view.MenuItem item) {
        return false;
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void updateItemPositions() {
    }
    
    private final void showEditItemDialog(com.example.shoppinglist.ShoppingItem item) {
    }
    
    private final void showDeleteConfirmationDialog() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/example/shoppinglist/ShoppingListDetailActivity$Companion;", "", "()V", "EXTRA_LIST_ID", "", "EXTRA_LIST_NAME", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
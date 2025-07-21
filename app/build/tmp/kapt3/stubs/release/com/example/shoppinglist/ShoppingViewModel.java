package com.example.shoppinglist;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\u0018\u00002\u00020\u0001:\u0001!B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bJ\u000e\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u0013J\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00100\u00062\u0006\u0010\u000f\u001a\u00020\u0010J\u001a\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00070\u00062\u0006\u0010\u000f\u001a\u00020\u0010J\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00100\u00062\u0006\u0010\u000f\u001a\u00020\u0010J\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\b0\u00062\u0006\u0010\u0018\u001a\u00020\u0010J\u000e\u0010\u0019\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bJ\u000e\u0010\u001a\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u001b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bJ\u000e\u0010\u001c\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u0013J\u0014\u0010\u001d\u001a\u00020\f2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00130\u0007J\u0014\u0010\u001f\u001a\u00020\f2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\b0\u0007R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/example/shoppinglist/ShoppingViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/example/shoppinglist/ShoppingRepository;", "(Lcom/example/shoppinglist/ShoppingRepository;)V", "allShoppingLists", "Landroidx/lifecycle/LiveData;", "", "Lcom/example/shoppinglist/ShoppingList;", "getAllShoppingLists", "()Landroidx/lifecycle/LiveData;", "delete", "Lkotlinx/coroutines/Job;", "shoppingList", "deleteAllItemsFromList", "listId", "", "deleteItem", "shoppingItem", "Lcom/example/shoppinglist/ShoppingItem;", "getItemCountForList", "getItemsForList", "getPurchasedItemCountForList", "getShoppingListById", "id", "insert", "insertItem", "update", "updateItem", "updateItems", "items", "updateShoppingLists", "shoppingLists", "ShoppingViewModelFactory", "app_release"})
public final class ShoppingViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.example.shoppinglist.ShoppingRepository repository = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.example.shoppinglist.ShoppingList>> allShoppingLists = null;
    
    public ShoppingViewModel(@org.jetbrains.annotations.NotNull
    com.example.shoppinglist.ShoppingRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.example.shoppinglist.ShoppingList>> getAllShoppingLists() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job insert(@org.jetbrains.annotations.NotNull
    com.example.shoppinglist.ShoppingList shoppingList) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job update(@org.jetbrains.annotations.NotNull
    com.example.shoppinglist.ShoppingList shoppingList) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job delete(@org.jetbrains.annotations.NotNull
    com.example.shoppinglist.ShoppingList shoppingList) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<com.example.shoppinglist.ShoppingList> getShoppingListById(int id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.lang.Integer> getItemCountForList(int listId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.lang.Integer> getPurchasedItemCountForList(int listId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.example.shoppinglist.ShoppingItem>> getItemsForList(int listId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job insertItem(@org.jetbrains.annotations.NotNull
    com.example.shoppinglist.ShoppingItem shoppingItem) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job updateItem(@org.jetbrains.annotations.NotNull
    com.example.shoppinglist.ShoppingItem shoppingItem) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job deleteItem(@org.jetbrains.annotations.NotNull
    com.example.shoppinglist.ShoppingItem shoppingItem) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job deleteAllItemsFromList(int listId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job updateItems(@org.jetbrains.annotations.NotNull
    java.util.List<com.example.shoppinglist.ShoppingItem> items) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job updateShoppingLists(@org.jetbrains.annotations.NotNull
    java.util.List<com.example.shoppinglist.ShoppingList> shoppingLists) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J%\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0000\u0010\u0006*\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\u00060\tH\u0016\u00a2\u0006\u0002\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/example/shoppinglist/ShoppingViewModel$ShoppingViewModelFactory;", "Landroidx/lifecycle/ViewModelProvider$Factory;", "repository", "Lcom/example/shoppinglist/ShoppingRepository;", "(Lcom/example/shoppinglist/ShoppingRepository;)V", "create", "T", "Landroidx/lifecycle/ViewModel;", "modelClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)Landroidx/lifecycle/ViewModel;", "app_release"})
    public static final class ShoppingViewModelFactory implements androidx.lifecycle.ViewModelProvider.Factory {
        @org.jetbrains.annotations.NotNull
        private final com.example.shoppinglist.ShoppingRepository repository = null;
        
        public ShoppingViewModelFactory(@org.jetbrains.annotations.NotNull
        com.example.shoppinglist.ShoppingRepository repository) {
            super();
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public <T extends androidx.lifecycle.ViewModel>T create(@org.jetbrains.annotations.NotNull
        java.lang.Class<T> modelClass) {
            return null;
        }
    }
}
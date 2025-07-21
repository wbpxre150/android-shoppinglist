package com.example.shoppinglist;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0002\u0016\u0017B-\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00070\u0005\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rJ\u0018\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\rH\u0016J\u0018\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\rH\u0016R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/example/shoppinglist/ShoppingListAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/example/shoppinglist/ShoppingListWithCount;", "Lcom/example/shoppinglist/ShoppingListAdapter$ShoppingListViewHolder;", "onListClicked", "Lkotlin/Function1;", "Lcom/example/shoppinglist/ShoppingList;", "", "onStartDrag", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "(Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "moveItem", "fromPosition", "", "toPosition", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "ShoppingListViewHolder", "ShoppingListsComparator", "app_debug"})
public final class ShoppingListAdapter extends androidx.recyclerview.widget.ListAdapter<com.example.shoppinglist.ShoppingListWithCount, com.example.shoppinglist.ShoppingListAdapter.ShoppingListViewHolder> {
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<com.example.shoppinglist.ShoppingList, kotlin.Unit> onListClicked = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<androidx.recyclerview.widget.RecyclerView.ViewHolder, kotlin.Unit> onStartDrag = null;
    
    public ShoppingListAdapter(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.example.shoppinglist.ShoppingList, kotlin.Unit> onListClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super androidx.recyclerview.widget.RecyclerView.ViewHolder, kotlin.Unit> onStartDrag) {
        super(null);
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.example.shoppinglist.ShoppingListAdapter.ShoppingListViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.example.shoppinglist.ShoppingListAdapter.ShoppingListViewHolder holder, int position) {
    }
    
    public final void moveItem(int fromPosition, int toPosition) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\"\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\t0\rR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/example/shoppinglist/ShoppingListAdapter$ShoppingListViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Landroid/view/View;)V", "itemCountView", "Landroid/widget/TextView;", "listNameView", "bind", "", "shoppingListWithCount", "Lcom/example/shoppinglist/ShoppingListWithCount;", "onStartDrag", "Lkotlin/Function1;", "Companion", "app_debug"})
    public static final class ShoppingListViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView listNameView = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView itemCountView = null;
        @org.jetbrains.annotations.NotNull
        public static final com.example.shoppinglist.ShoppingListAdapter.ShoppingListViewHolder.Companion Companion = null;
        
        public ShoppingListViewHolder(@org.jetbrains.annotations.NotNull
        android.view.View itemView) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        com.example.shoppinglist.ShoppingListWithCount shoppingListWithCount, @org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function1<? super androidx.recyclerview.widget.RecyclerView.ViewHolder, kotlin.Unit> onStartDrag) {
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/example/shoppinglist/ShoppingListAdapter$ShoppingListViewHolder$Companion;", "", "()V", "create", "Lcom/example/shoppinglist/ShoppingListAdapter$ShoppingListViewHolder;", "parent", "Landroid/view/ViewGroup;", "app_debug"})
        public static final class Companion {
            
            private Companion() {
                super();
            }
            
            @org.jetbrains.annotations.NotNull
            public final com.example.shoppinglist.ShoppingListAdapter.ShoppingListViewHolder create(@org.jetbrains.annotations.NotNull
            android.view.ViewGroup parent) {
                return null;
            }
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lcom/example/shoppinglist/ShoppingListAdapter$ShoppingListsComparator;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/example/shoppinglist/ShoppingListWithCount;", "()V", "areContentsTheSame", "", "oldItem", "newItem", "areItemsTheSame", "app_debug"})
    public static final class ShoppingListsComparator extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.example.shoppinglist.ShoppingListWithCount> {
        
        public ShoppingListsComparator() {
            super();
        }
        
        @java.lang.Override
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull
        com.example.shoppinglist.ShoppingListWithCount oldItem, @org.jetbrains.annotations.NotNull
        com.example.shoppinglist.ShoppingListWithCount newItem) {
            return false;
        }
        
        @java.lang.Override
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull
        com.example.shoppinglist.ShoppingListWithCount oldItem, @org.jetbrains.annotations.NotNull
        com.example.shoppinglist.ShoppingListWithCount newItem) {
            return false;
        }
    }
}
package com.example.shoppinglist;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0002\u001a\u001bB[\u0012\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00070\t\u0012\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00070\t\u0012\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\u0002\u0010\rJ\u000e\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u0010J\u0016\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0010J\u0018\u0010\u0014\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0018\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0010H\u0016R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00070\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00070\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00070\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/example/shoppinglist/ShoppingItemAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/example/shoppinglist/ShoppingItem;", "Lcom/example/shoppinglist/ShoppingItemAdapter$ShoppingItemViewHolder;", "onItemCheckedChanged", "Lkotlin/Function2;", "", "", "onDeleteClicked", "Lkotlin/Function1;", "onItemClicked", "onStartDrag", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "(Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "getItemAt", "position", "", "moveItem", "fromPosition", "toPosition", "onBindViewHolder", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "ShoppingItemViewHolder", "ShoppingItemsComparator", "app_debug"})
public final class ShoppingItemAdapter extends androidx.recyclerview.widget.ListAdapter<com.example.shoppinglist.ShoppingItem, com.example.shoppinglist.ShoppingItemAdapter.ShoppingItemViewHolder> {
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function2<com.example.shoppinglist.ShoppingItem, java.lang.Boolean, kotlin.Unit> onItemCheckedChanged = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<com.example.shoppinglist.ShoppingItem, kotlin.Unit> onDeleteClicked = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<com.example.shoppinglist.ShoppingItem, kotlin.Unit> onItemClicked = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<androidx.recyclerview.widget.RecyclerView.ViewHolder, kotlin.Unit> onStartDrag = null;
    
    public ShoppingItemAdapter(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super com.example.shoppinglist.ShoppingItem, ? super java.lang.Boolean, kotlin.Unit> onItemCheckedChanged, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.example.shoppinglist.ShoppingItem, kotlin.Unit> onDeleteClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.example.shoppinglist.ShoppingItem, kotlin.Unit> onItemClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super androidx.recyclerview.widget.RecyclerView.ViewHolder, kotlin.Unit> onStartDrag) {
        super(null);
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.example.shoppinglist.ShoppingItemAdapter.ShoppingItemViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.example.shoppinglist.ShoppingItemAdapter.ShoppingItemViewHolder holder, int position) {
    }
    
    public final void moveItem(int fromPosition, int toPosition) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.example.shoppinglist.ShoppingItem getItemAt(int position) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004JL\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\r0\u00112\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\r0\u00142\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\r0\u00142\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\r0\u0014R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/example/shoppinglist/ShoppingItemAdapter$ShoppingItemViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Landroid/view/View;)V", "buttonDelete", "Landroid/widget/ImageButton;", "checkBoxPurchased", "Landroid/widget/CheckBox;", "itemNameView", "Landroid/widget/TextView;", "itemQuantityView", "bind", "", "shoppingItem", "Lcom/example/shoppinglist/ShoppingItem;", "onItemCheckedChanged", "Lkotlin/Function1;", "", "onDeleteClicked", "Lkotlin/Function0;", "onItemClicked", "onStartDrag", "Companion", "app_debug"})
    public static final class ShoppingItemViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView itemNameView = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView itemQuantityView = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.CheckBox checkBoxPurchased = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.ImageButton buttonDelete = null;
        @org.jetbrains.annotations.NotNull
        public static final com.example.shoppinglist.ShoppingItemAdapter.ShoppingItemViewHolder.Companion Companion = null;
        
        public ShoppingItemViewHolder(@org.jetbrains.annotations.NotNull
        android.view.View itemView) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        com.example.shoppinglist.ShoppingItem shoppingItem, @org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onItemCheckedChanged, @org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function0<kotlin.Unit> onDeleteClicked, @org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function0<kotlin.Unit> onItemClicked, @org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function0<kotlin.Unit> onStartDrag) {
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/example/shoppinglist/ShoppingItemAdapter$ShoppingItemViewHolder$Companion;", "", "()V", "create", "Lcom/example/shoppinglist/ShoppingItemAdapter$ShoppingItemViewHolder;", "parent", "Landroid/view/ViewGroup;", "app_debug"})
        public static final class Companion {
            
            private Companion() {
                super();
            }
            
            @org.jetbrains.annotations.NotNull
            public final com.example.shoppinglist.ShoppingItemAdapter.ShoppingItemViewHolder create(@org.jetbrains.annotations.NotNull
            android.view.ViewGroup parent) {
                return null;
            }
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lcom/example/shoppinglist/ShoppingItemAdapter$ShoppingItemsComparator;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/example/shoppinglist/ShoppingItem;", "()V", "areContentsTheSame", "", "oldItem", "newItem", "areItemsTheSame", "app_debug"})
    public static final class ShoppingItemsComparator extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.example.shoppinglist.ShoppingItem> {
        
        public ShoppingItemsComparator() {
            super();
        }
        
        @java.lang.Override
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull
        com.example.shoppinglist.ShoppingItem oldItem, @org.jetbrains.annotations.NotNull
        com.example.shoppinglist.ShoppingItem newItem) {
            return false;
        }
        
        @java.lang.Override
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull
        com.example.shoppinglist.ShoppingItem oldItem, @org.jetbrains.annotations.NotNull
        com.example.shoppinglist.ShoppingItem newItem) {
            return false;
        }
    }
}
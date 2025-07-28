# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands
- Build project: `./gradlew build`
- Run tests: `./gradlew test`
- Run single test: `./gradlew test --tests "com.example.shoppinglist.TestClassName.testMethodName"`
- Lint checks: `./gradlew lint`
- Install debug build: `./gradlew installDebug`

## Code Style Guidelines
- Kotlin standard conventions with 4-space indentation
- MVVM architecture with Repository pattern
- Room for database operations, use LiveData for reactive UI
- Coroutines for background operations in ViewModels via viewModelScope
- Classes use PascalCase, functions/variables use camelCase
- Entity classes as data classes with Room annotations
- DAOs use interfaces with Room annotations
- Organize imports alphabetically, remove unused imports
- Use view binding instead of findViewById
- Error handling via try/catch or Result wrappers where appropriate

## Architecture Overview
This is an Android shopping list app built with MVVM + Repository pattern:

### Core Components
- **ShoppingApplication**: Application class providing dependency injection via lazy properties
- **ShoppingDatabase**: Room database with singleton pattern, includes callback for sample data initialization
- **Repository Layer**: ShoppingRepository acts as single source of truth, coordinates between DAOs
- **ViewModel Layer**: ShoppingViewModel handles UI-related data using LiveData and coroutines
- **Entity Layer**: ShoppingList (parent) and ShoppingItem (child) with foreign key relationship

### Data Model
- Two main entities: ShoppingList and ShoppingItem
- ShoppingItem has foreign key relationship to ShoppingList with CASCADE delete
- Items have quantity, purchase status (isPurchased), position for ordering, price, and optional reminders
- All database operations use suspend functions with coroutines
- Database version 3 with migration support for adding reminderDateTime column

### UI Structure
- MainActivity: Main list of shopping lists with drag-and-drop reordering
- NewShoppingListActivity: Create new shopping lists
- ShoppingListDetailActivity: View/edit items within a list with pricing features
- RecyclerView adapters for both lists and items with ItemTouchHelper support
- View binding enabled for all activities
- Custom dialogs for price input, item editing, and reminder setting

### Dependencies
- Room 2.6.0 for database persistence
- Lifecycle components with LiveData/ViewModel
- Kotlin Coroutines for async operations
- Material Design components for UI
- Target SDK 34, minimum SDK 24

### Key Architectural Patterns
- **Dependency Injection**: Lazy initialization in ShoppingApplication.kt:11-16 provides repository instance
- **Database Singleton**: ShoppingDatabase.getDatabase() uses double-checked locking pattern for thread safety
- **LiveData Composition**: ShoppingViewModel uses MediatorLiveData to combine multiple data sources for shopping list counts
- **Coroutine Scopes**: applicationScope in Application class, viewModelScope in ViewModel for lifecycle-aware operations
- **Foreign Key Cascade**: ShoppingItem.kt:11-16 ensures items are automatically deleted when parent list is removed
- **Observer Pattern**: Complex observer management in ShoppingViewModel.kt:102-147 for dynamic list updates with cleanup handling
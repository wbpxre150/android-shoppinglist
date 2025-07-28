# Shopping List App

A modern Android shopping list application built with Kotlin featuring MVVM architecture, Material Design 3, comprehensive notification system, and Room database for local persistence.

## Features

### Core Functionality
- **Multiple Shopping Lists**: Create and manage multiple shopping lists for different occasions (groceries, household items, etc.)
- **Advanced Item Management**: Add, edit, and delete items with drag-and-drop reordering within lists
- **Purchase Tracking**: Mark items as purchased/unpurchased with visual indicators and purchase overlay
- **Smart Quantity System**: Set specific quantities with visual quantity badges (displayed only when > 1)
- **Pricing Support**: Add prices to items with real-time total calculation displayed in ActionBar
- **Item Reminders**: Set date/time reminders for shopping items with comprehensive notification system
- **Persistent Storage**: All data stored locally using Room database with migration support

### User Interface
- **Material Design 3**: Modern UI with comprehensive color system and dark theme support
- **Responsive Design**: Compact layout optimized for better information density
- **Interactive Elements**: Smooth animations, collapsible add-item form, and drag-and-drop support
- **Smart UI Components**: RecyclerView lists with ItemTouchHelper for reordering
- **View Binding**: Type-safe view references throughout the app
- **Accessibility**: Proper touch targets and screen reader support

### Notification System
- **Smart Reminders**: Set date/time reminders for individual shopping items
- **Rich Notifications**: Interactive notifications with item details and quantity information
- **Quick Actions**: Mark items as purchased, snooze for 30 minutes, or view full list directly from notifications
- **Persistent Scheduling**: Notifications survive app restarts and device reboots via BootReceiver
- **Modern Permissions**: Seamless Android 13+ notification permission handling with user-friendly dialogs
- **Exact Alarm Support**: Precise timing using AlarmManager with fallback to inexact alarms

### Architecture & Technical Features
- **MVVM Pattern**: Separation of concerns with ViewModel and LiveData, including MediatorLiveData for race condition prevention
- **Repository Pattern**: Single source of truth for data operations with notification-related extensions
- **Room Database**: Local SQLite database with entity relationships and schema migrations (v1→v3)
- **Kotlin Coroutines**: Asynchronous operations with proper scope management (viewModelScope, applicationScope)
- **Dependency Injection**: Lazy property injection via Application class singleton pattern
- **Foreign Key Relationships**: Cascade delete for data integrity and proper cleanup
- **Robust Error Handling**: Comprehensive exception handling and graceful degradation

## Technical Specifications

- **Target SDK**: 34 (Android 14)
- **Minimum SDK**: 24 (Android 7.0)
- **Language**: Kotlin
- **Database**: Room 2.6.0 with schema migrations
- **Architecture**: MVVM + Repository Pattern
- **UI**: View Binding + Material Design 3 Components
- **Permissions**: POST_NOTIFICATIONS, SCHEDULE_EXACT_ALARM, USE_EXACT_ALARM, WAKE_LOCK, RECEIVE_BOOT_COMPLETED
- **Threading**: Kotlin Coroutines with structured concurrency

## Build Instructions

### Prerequisites
- Android Studio
- Kotlin support
- Android SDK 24+

### Building the Project
```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Install debug build on connected device
./gradlew installDebug

# Run lint checks
./gradlew lint
```

### Running Tests
```bash
# Run all tests
./gradlew test

# Run specific test
./gradlew test --tests "com.example.shoppinglist.TestClassName.testMethodName"
```

## Project Structure

```
app/src/main/java/com/example/shoppinglist/
├── MainActivity.kt                    # Main shopping lists activity
├── NewShoppingListActivity.kt         # Create new shopping list
├── ShoppingListDetailActivity.kt      # Shopping list item management
├── ShoppingApplication.kt             # Application class with DI
├── ShoppingDatabase.kt                # Room database with migrations
├── ShoppingItem.kt                    # Item entity with price and reminders
├── ShoppingItemAdapter.kt             # RecyclerView adapter for items
├── ShoppingItemDao.kt                 # Item data access with notification queries
├── ShoppingList.kt                    # Shopping list entity
├── ShoppingListAdapter.kt             # RecyclerView adapter for lists
├── ShoppingListDao.kt                 # List data access
├── ShoppingRepository.kt              # Repository with notification support
├── ShoppingViewModel.kt               # MVVM ViewModel with MediatorLiveData
└── notifications/
    ├── NotificationManager.kt         # Centralized notification management
    ├── NotificationReceiver.kt        # Alarm handling and notification actions
    └── BootReceiver.kt               # Notification persistence across reboots
```

## Database Schema (Version 3)

### ShoppingList Entity
- `id` (Primary Key) - Unique list identifier
- `name` - List name
- `position` - List ordering for drag-and-drop
- `createdAt` - Creation timestamp

### ShoppingItem Entity  
- `id` (Primary Key) - Unique item identifier
- `listId` (Foreign Key → ShoppingList) - Parent list with CASCADE delete
- `name` - Item name
- `quantity` - Item quantity (default: 1)
- `isPurchased` - Purchase status (default: false)
- `position` - Item ordering within list (default: 0)
- `price` - Item price (default: 0.0)
- `reminderDateTime` - Optional reminder timestamp (added in v3)

## Development History

This application has evolved through several major iterations:

### v1.0 - Initial Release (4245423)
- Core MVVM architecture with Room database
- Basic shopping list and item management
- Material Design UI foundation
- Kotlin coroutines integration

### v2.0 - Material Design 3 & Dark Theme (e6be009)
- Upgraded to Material Design 3 with comprehensive color system
- Added dark theme support with automatic light/dark mode switching
- Implemented compact layout for better information density
- Enhanced visual hierarchy and accessibility standards

### v2.1 - LiveData Race Condition Fixes (2f7e2f6)
- Fixed critical bugs causing lists to disappear after creation
- Resolved deleted lists reappearing due to stale observer data
- Implemented MediatorLiveData approach for consolidated data management
- Added proper observer cleanup and memory leak prevention

### v2.2 - Enhanced UI & Pricing Features (d14722c)
- Added collapsible add-item form with smooth animations
- Implemented real-time total price calculation in ActionBar
- Created smart quantity badges (shown only when > 1)
- Added instant keyboard display for price input
- Enhanced drag-and-drop functionality with visual feedback

### v3.0 - Comprehensive Notification System (c43b2f5)
- Full notification infrastructure with Android permissions
- Smart reminder system with date/time picker integration
- Rich interactive notifications with action buttons (Mark Purchased, Snooze, View List)
- Boot persistence ensuring notifications survive device restarts
- Modern Android 13+ permission handling with user-friendly dialogs
- AlarmManager integration with exact timing and fallback support

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request
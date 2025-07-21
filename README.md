# Shopping List App

An Android shopping list application built with Kotlin using MVVM architecture and Room database for local persistence.

## Features

### Core Functionality
- **Multiple Shopping Lists**: Create and manage multiple shopping lists for different occasions (groceries, household items, etc.)
- **Item Management**: Add, edit, and delete items within each shopping list
- **Purchase Tracking**: Mark items as purchased/unpurchased with visual indicators
- **Quantity Support**: Set specific quantities for each item
- **Item Ordering**: Custom positioning of items within lists
- **Persistent Storage**: All data stored locally using Room database

### User Interface
- **Modern Material Design**: Clean, intuitive interface following Android design guidelines
- **RecyclerView Lists**: Smooth scrolling lists for both shopping lists and items
- **View Binding**: Type-safe view references throughout the app
- **Responsive Layout**: Optimized for various Android screen sizes

### Architecture & Technical Features
- **MVVM Pattern**: Separation of concerns with ViewModel and LiveData
- **Repository Pattern**: Single source of truth for data operations
- **Room Database**: Local SQLite database with entity relationships
- **Kotlin Coroutines**: Asynchronous operations for smooth UI performance
- **Dependency Injection**: Lazy property injection via Application class
- **Foreign Key Relationships**: Cascade delete for data integrity

## Technical Specifications

- **Target SDK**: 34 (Android 14)
- **Minimum SDK**: 24 (Android 7.0)
- **Language**: Kotlin
- **Database**: Room 2.6.0
- **Architecture**: MVVM + Repository Pattern
- **UI**: View Binding + Material Design Components

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
├── data/
│   ├── entities/        # Room entities (ShoppingList, ShoppingItem)
│   ├── dao/            # Data Access Objects
│   └── database/       # Room database configuration
├── repository/         # Repository layer for data management
├── ui/
│   ├── activities/     # Main activities
│   ├── adapters/       # RecyclerView adapters
│   └── viewmodels/     # MVVM ViewModels
└── ShoppingApplication.kt  # Application class with DI
```

## Database Schema

### ShoppingList Entity
- `id` (Primary Key)
- `name` - List name
- `createdAt` - Creation timestamp

### ShoppingItem Entity
- `id` (Primary Key)
- `shoppingListId` (Foreign Key → ShoppingList)
- `name` - Item name
- `quantity` - Item quantity
- `isPurchased` - Purchase status
- `position` - Item ordering

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request
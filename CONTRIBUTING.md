# Contributing to Frame Notes

Thank you for your interest in contributing to Frame Notes! This document provides guidelines and instructions for contributing.

## Getting Started

### Prerequisites
- Android Studio (Giraffe or newer)
- JDK 17+
- Android SDK (API 26+)
- Kotlin 2.0+

### Setup
1. Fork the repository on GitHub
2. Clone your fork:
   ```bash
   git clone https://github.com/<your-username>/Frame-Notes.git
   ```
3. Open the project in Android Studio
4. Wait for Gradle sync to complete
5. Run the app on an emulator or device (minSdk 26)

### Building
```bash
./gradlew assembleDebug
```

### Running Tests
```bash
./gradlew test                    # Unit tests
./gradlew connectedAndroidTest    # Instrumented tests
```

## How to Contribute

### Reporting Bugs
- Open an issue on GitHub
- Include steps to reproduce, expected vs actual behavior, and device/Android version

### Suggesting Features
- Open an issue with the "enhancement" label
- Describe the use case and how it benefits users

### Submitting Code
1. Create a feature branch from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```
2. Make your changes following the code style
3. Write or update tests as needed
4. Ensure all tests pass
5. Commit with clear, descriptive messages
6. Push and open a Pull Request

## Code Style
- Follow Kotlin coding conventions
- Use Compose for all UI
- Keep ViewModels thin — business logic goes in the data layer
- All file access must use content URIs (no file paths)
- Cache EXIF data in Room — never re-read files on every launch
- Use `Dispatchers.IO` for all I/O operations

## Architecture
- **MVVM** with a single `PhotoRepository` pattern
- **Room** is the source of truth for the photo list
- **Hilt** for dependency injection
- **Paging 3** for large library scrolling
- **WorkManager** for background indexing

## Pull Request Guidelines
- Keep PRs focused on a single change
- Include a clear description of what changed and why
- Add screenshots for UI changes
- Update tests for new functionality
- Reference related issues

## License
By contributing, you agree that your contributions will be licensed under the same license as the project.

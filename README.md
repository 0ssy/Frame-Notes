# Frame Notes

An Android app that indexes your photo library, learns how you shoot, and lets you annotate and clean up your photos. Most EXIF apps show you data for one photo — this one shows you patterns across your library.

## Features

- Index a whole folder in the background with incremental re-scans
- Paged library with filtering by camera, focal length range, and tags
- Sort by date, focal length, or ISO
- Detailed EXIF view with graceful handling of missing data
- Notes and tags per photo with search
- Insights dashboard: focal length distribution, ISO histogram, and time-of-day patterns
- Privacy export: strip GPS/identity metadata and share a clean copy
- CSV export of library metadata
- Material 3 theming with light/dark mode support

## Architecture

- MVVM with single `PhotoRepository` pattern via Room as source of truth
- Room + Paging 3 for smooth scrolling with large libraries
- Hilt for dependency injection
- WorkManager for background folder indexing
- Coil for image loading
- Navigation Compose
- Custom Canvas charts (no external chart library)

## Tech Stack

- Kotlin + Jetpack Compose
- Room (with KSP)
- Paging 3
- WorkManager + Hilt
- ExifInterface
- Coil
- Material 3

## Design Decisions

- **EXIF cached in Room**: Filtering/sorting happen via SQL queries instead of re-reading files on every launch
- **SAF folder access**: Uses `OpenDocumentTree` with `takePersistableUriPermission()` so indexing survives restarts
- **Timezone handling**: EXIF dates have no timezone; stored as wall-clock time as if UTC and displayed accordingly
- **Content URIs**: All file access uses content URIs via `DocumentsContract` and `contentResolver.openInputStream()` (no file paths)

## Build & Run

1. Clone the repository
2. Open in Android Studio (Giraffe or newer recommended)
3. Build with `./gradlew assembleDebug`
4. Run on device/emulator (minSdk 26)

## Usage

1. Launch the app
2. Tap "Choose photo folder" and grant persistent access to a folder containing images
3. WorkManager will index photos in the background (incremental re-scans on subsequent launches)
4. Browse the library, filter by camera/tags, view details, add notes and tags
5. Check Insights to see your shooting patterns
6. Use Privacy Export to create and share a copy with GPS removed

## Screenshots

Screenshots are captured from the running app (minSdk 26). Place them in `docs/screenshots/`:

- `docs/screenshots/library.png` — Library grid view with filter chips
- `docs/screenshots/detail.png` — Detail view with grouped EXIF metadata
- `docs/screenshots/insights.png` — Insights dashboard with Canvas charts
- `docs/screenshots/privacy.png` — Privacy export before/after preview

![Library](docs/screenshots/library.png)
![Detail](docs/screenshots/detail.png)
![Insights](docs/screenshots/insights.png)
![Privacy](docs/screenshots/privacy.png)

## Tests

- Unit tests: `ExifParser` (shutter speed, focal length, date parsing, camera formatting)
- Instrumented tests: DAO queries with in-memory Room DB
- Compose UI tests: basic rendering

Run tests with:
```bash
./gradlew test
./gradlew connectedAndroidTest
```

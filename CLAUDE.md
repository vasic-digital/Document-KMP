# Document-KMP

## Project Overview

Kotlin Multiplatform document model library. Package: `digital.vasic.document`.

## Build Commands

```bash
./gradlew desktopTest    # Run tests
./gradlew build          # Build all targets
```

## Architecture

- `Document.kt` - Serializable data class with 18 format constants, change tracking, format detection
- `FormatDetector.kt` - Extension and content-based format detection (regex patterns)
- `Document.{platform}.kt` - expect/actual implementations for file operations

## Key Patterns

- Document equality uses path + title + format (ignores content, author, timestamps)
- Format constants are plain strings (no external dependency)
- FormatDetector uses extension map + regex content patterns
- `@Transient` on modTime/touchTime excludes them from serialization

## Dependencies

- kotlinx-serialization (JSON serialization of Document)
- kotlinx-coroutines (test dependency only)

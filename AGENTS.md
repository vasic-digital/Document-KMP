# Document-KMP Agent Guidelines

## Testing

Tests in `src/commonTest/`. Run with `./gradlew desktopTest`.

Test files:
- `DocumentTest.kt` - Core CRUD, change tracking, format detection, file operations
- `DocumentAdvancedTest.kt` - Edge cases, equality, copy, destructuring, all format constants
- `DocumentFormatTest.kt` - Format detection by extension and content, all fields
- `DocumentStressTest.kt` - Concurrent creation, serialization, idempotency
- `FormatDetectorTest.kt` - Extension mapping, content patterns, edge cases

## Rules

- Never remove or disable tests
- All changes must pass existing tests
- Format constants must remain plain strings (no external dependencies)
- Document equality is based on path + title + format only

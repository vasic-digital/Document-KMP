# Document-KMP

Kotlin Multiplatform document model with format detection, change tracking, and serialization.

## Features

- **Document** - Serializable data class with path, title, extension, format, content, author, tags
- **FormatDetector** - Extension-based and content-based format detection for 18 formats
- **Change Tracking** - Modification time and touch time tracking with `hasChanged()`/`resetChangeTracking()`
- **Platform File Ops** - expect/actual for `fileExists()`, `getFileModTime()`, `getFileSize()`, `createDocument()`

## Platforms

- Android
- Desktop (JVM)
- iOS (x64, arm64, simulator)
- Web (Wasm/JS)

## Installation

Add as a git submodule:

```bash
git submodule add git@github.com:vasic-digital/Document-KMP.git
```

Then in your `settings.gradle.kts`:

```kotlin
includeBuild("Document-KMP")
```

## Usage

```kotlin
// Create a document
val doc = Document(
    path = "/path/to/README.md",
    title = "README",
    extension = "md"
)

// Detect format
doc.detectFormatByExtension() // sets format to "markdown"

// Content-based detection
doc.detectFormatByContent("\\documentclass{article}") // detects LaTeX

// Change tracking
doc.touch()
if (doc.hasChanged()) { /* reload */ }

// Serialization
val json = Json.encodeToString(doc)
val restored = Json.decodeFromString<Document>(json)

// Platform file operations
val exists = doc.fileExists()
val modTime = doc.getFileModTime()
val size = doc.getFileSize()

// Factory
val fromPath = createDocument("/path/to/file.md")
```

## Supported Formats

markdown, plaintext, todotxt, csv, latex, orgmode, wikitext, asciidoc,
restructuredtext, taskpaper, textile, creole, tiddlywiki, jupyter,
rmarkdown, keyvalue, binary

## License

Apache-2.0

# Document-KMP User Guide

## Getting Started

Document-KMP provides a platform-agnostic document model for Kotlin Multiplatform projects.

## Creating Documents

```kotlin
val doc = Document(
    path = "/path/to/file.md",
    title = "file",
    extension = "md"
)
```

## Format Detection

```kotlin
// By extension
doc.detectFormatByExtension() // "md" -> FORMAT_MARKDOWN

// By content
doc.detectFormatByContent("\\documentclass{article}") // -> FORMAT_LATEX
```

## Change Tracking

```kotlin
doc.touch() // mark as recently accessed
doc.hasChanged() // check if file was modified externally
doc.resetChangeTracking() // reset tracking state
```

## Serialization

```kotlin
val json = Json.encodeToString(doc)
val restored = Json.decodeFromString<Document>(json)
```

## Platform File Operations

```kotlin
doc.fileExists() // check if file exists
doc.getFileModTime() // last modification time
doc.getFileSize() // file size in bytes
createDocument("/path/to/file.md") // factory from path
```

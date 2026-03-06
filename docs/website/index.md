# Document-KMP

Cross-platform document model for Kotlin Multiplatform applications.

## Quick Start

```kotlin
val doc = Document(path = "/path/to/README.md", title = "README", extension = "md")
doc.detectFormatByExtension() // format = "markdown"
```

## Key Features

- 18 format constants with extension and content detection
- @Serializable with JSON support
- Change tracking with file modification monitoring
- Platform-specific file operations via expect/actual

## Links

- [User Guide](../user-guide.md)
- [API Reference](../api-reference.md)
- [Architecture](../architecture.md)
- [GitHub](https://github.com/vasic-digital/Document-KMP)

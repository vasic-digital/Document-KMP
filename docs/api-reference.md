# Document-KMP API Reference

## Document

### Properties
- `id: String` - Unique identifier (default "")
- `path: String` - Absolute file path
- `title: String` - Filename without extension
- `extension: String` - File extension
- `content: String` - Document content (default "")
- `author: String?` - Author name
- `tags: List<String>` - Labels/tags
- `format: String` - Format identifier (default FORMAT_UNKNOWN)
- `modTime: Long` - Last modification time (@Transient)
- `touchTime: Long` - Last access time (@Transient)
- `filename: String` - Computed title.extension

### Methods
- `hasChanged(): Boolean` - Check if file modified since last tracking
- `resetChangeTracking()` - Reset mod/touch times to -1
- `touch()` - Update touchTime to current time
- `detectFormatByExtension()` - Set format from extension
- `detectFormatByContent(content): Boolean` - Set format from content analysis

### Format Constants
FORMAT_UNKNOWN, FORMAT_PLAINTEXT, FORMAT_MARKDOWN, FORMAT_TODOTXT, FORMAT_CSV,
FORMAT_WIKITEXT, FORMAT_KEYVALUE, FORMAT_ASCIIDOC, FORMAT_ORGMODE, FORMAT_LATEX,
FORMAT_RESTRUCTUREDTEXT, FORMAT_TASKPAPER, FORMAT_TEXTILE, FORMAT_CREOLE,
FORMAT_TIDDLYWIKI, FORMAT_JUPYTER, FORMAT_RMARKDOWN, FORMAT_BINARY

## FormatDetector

- `detectByExtension(extension): String` - Map extension to format ID
- `detectByContent(content): String?` - Detect format from content (null if none)
- `getAllExtensions(): List<String>` - All registered extensions (with dot prefix)

## Platform Functions (expect/actual)

- `currentTimeMillis(): Long` - Current system time
- `Document.getFileModTime(): Long` - File modification time
- `Document.getFileSize(): Long` - File size in bytes
- `Document.fileExists(): Boolean` - File existence check
- `createDocument(path): Document?` - Factory from file path

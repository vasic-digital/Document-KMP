# Document-KMP Architecture

## Overview

Platform-agnostic document model with format detection and serialization for Kotlin Multiplatform.

## Dependencies

- kotlinx-serialization (Document is @Serializable)
- kotlinx-coroutines (test dependency)

## Design Decisions

1. Format constants are plain strings, not backed by external types — zero dependencies on format registries
2. FormatDetector is a separate object from Document — can be extended or replaced
3. Document equality uses path + title + format, ignoring content/author/timestamps
4. modTime and touchTime are @Transient — excluded from serialization
5. Platform file operations use expect/actual — Desktop/Android use java.io.File, iOS uses NSFileManager, Wasm uses localStorage

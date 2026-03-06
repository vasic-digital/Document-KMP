# Document-KMP Video Course Outline

## Module 1: Introduction
- What is Document-KMP
- Document data class overview
- Platform support

## Module 2: Document Model
- Creating documents
- Properties and defaults
- Filename computation
- Equality and hashing

## Module 3: Format Detection
- FormatDetector extension mapping
- Content-based detection with regex
- Adding custom formats

## Module 4: Change Tracking
- modTime and touchTime
- hasChanged() logic
- resetChangeTracking() and touch()

## Module 5: Serialization
- kotlinx-serialization integration
- @Transient fields
- JSON round-trip

## Module 6: Platform Integration
- expect/actual pattern
- Desktop: java.io.File
- iOS: NSFileManager
- Wasm: localStorage

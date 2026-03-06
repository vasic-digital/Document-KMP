package digital.vasic.document

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Platform-agnostic document model.
 *
 * Represents a text document with associated metadata, format detection,
 * and change tracking. Platform-specific file operations are delegated
 * to expect/actual functions.
 */
@Serializable
data class Document(
    val id: String = "",
    val path: String,
    val title: String,
    val extension: String,
    val content: String = "",
    val author: String? = null,
    val tags: List<String> = emptyList(),
    var format: String = FORMAT_UNKNOWN,
    @Transient var modTime: Long = -1,
    @Transient var touchTime: Long = -1
) {

    companion object {
        const val FORMAT_UNKNOWN = "unknown"
        const val FORMAT_PLAINTEXT = "plaintext"
        const val FORMAT_MARKDOWN = "markdown"
        const val FORMAT_TODOTXT = "todotxt"
        const val FORMAT_CSV = "csv"
        const val FORMAT_WIKITEXT = "wikitext"
        const val FORMAT_KEYVALUE = "keyvalue"
        const val FORMAT_ASCIIDOC = "asciidoc"
        const val FORMAT_ORGMODE = "orgmode"
        const val FORMAT_LATEX = "latex"
        const val FORMAT_RESTRUCTUREDTEXT = "restructuredtext"
        const val FORMAT_TASKPAPER = "taskpaper"
        const val FORMAT_TEXTILE = "textile"
        const val FORMAT_CREOLE = "creole"
        const val FORMAT_TIDDLYWIKI = "tiddlywiki"
        const val FORMAT_JUPYTER = "jupyter"
        const val FORMAT_RMARKDOWN = "rmarkdown"
        const val FORMAT_BINARY = "binary"
    }

    val filename: String
        get() = if (extension.isNotEmpty()) "$title.$extension" else title

    fun hasChanged(): Boolean {
        return modTime < 0 || touchTime < 0 || getFileModTime() > modTime
    }

    fun resetChangeTracking() {
        modTime = -1
        touchTime = -1
    }

    fun touch() {
        touchTime = currentTimeMillis()
    }

    fun detectFormatByExtension() {
        format = FormatDetector.detectByExtension(extension)
    }

    fun detectFormatByContent(content: String): Boolean {
        val detected = FormatDetector.detectByContent(content)
        if (detected != null) {
            format = detected
            return true
        }
        return false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as Document
        if (path != other.path) return false
        if (title != other.title) return false
        if (format != other.format) return false
        return true
    }

    override fun hashCode(): Int {
        var result = path.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + format.hashCode()
        return result
    }
}

expect fun currentTimeMillis(): Long
expect fun Document.getFileModTime(): Long
expect fun Document.getFileSize(): Long
expect fun Document.fileExists(): Boolean
expect fun createDocument(path: String): Document?

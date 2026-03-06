package digital.vasic.document

import java.io.File

actual fun currentTimeMillis(): Long = System.currentTimeMillis()

actual fun Document.getFileModTime(): Long {
    return try { File(path).lastModified() } catch (e: Exception) { -1L }
}

actual fun Document.getFileSize(): Long {
    return try { File(path).length() } catch (e: Exception) { 0L }
}

actual fun Document.fileExists(): Boolean {
    return try { File(path).exists() } catch (e: Exception) { false }
}

actual fun createDocument(path: String): Document? {
    return try {
        val file = File(path)
        if (!file.exists()) return null
        val extension = file.extension
        val title = file.nameWithoutExtension
        val format = FormatDetector.detectByExtension(extension)
        Document(path = file.absolutePath, title = title, extension = extension, format = format)
    } catch (e: Exception) { null }
}

package digital.vasic.document

import kotlinx.browser.window

actual fun currentTimeMillis(): Long = (window.performance.now() * 1000).toLong()

actual fun Document.getFileModTime(): Long {
    return try {
        window.localStorage.getItem("modTime_$path")?.toLongOrNull() ?: currentTimeMillis()
    } catch (e: Exception) { currentTimeMillis() }
}

actual fun Document.getFileSize(): Long {
    return try {
        window.localStorage.getItem("size_$path")?.toLongOrNull() ?: 0L
    } catch (e: Exception) { 0L }
}

actual fun Document.fileExists(): Boolean {
    return try { window.localStorage.getItem("content_$path") != null } catch (e: Exception) { false }
}

actual fun createDocument(path: String): Document? {
    return try {
        window.localStorage.getItem("content_$path") ?: return null
        val name = path.substringAfterLast('/', path)
        val extension = if (name.contains('.')) name.substringAfterLast('.', "") else ""
        val title = if (extension.isNotEmpty()) name.substringBeforeLast(".$extension") else name
        Document(path = path, title = title, extension = extension, modTime = currentTimeMillis(), touchTime = currentTimeMillis()).apply { detectFormatByExtension() }
    } catch (e: Exception) { null }
}

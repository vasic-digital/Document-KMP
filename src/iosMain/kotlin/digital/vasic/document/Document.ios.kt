package digital.vasic.document

import platform.Foundation.*

actual fun currentTimeMillis(): Long = (NSDate.date().timeIntervalSince1970 * 1000).toLong()

actual fun Document.getFileModTime(): Long {
    return try {
        val attrs = NSFileManager.defaultManager.attributesOfItemAtPath(path, null)
        val modDate = attrs?.objectForKey(NSFileModificationDate) as? NSDate
        modDate?.timeIntervalSince1970?.times(1000)?.toLong() ?: -1L
    } catch (e: Exception) { -1L }
}

actual fun Document.getFileSize(): Long {
    return try {
        val attrs = NSFileManager.defaultManager.attributesOfItemAtPath(path, null)
        val fileSize = attrs?.objectForKey(NSFileSize) as? NSNumber
        fileSize?.longValue ?: -1L
    } catch (e: Exception) { -1L }
}

actual fun Document.fileExists(): Boolean {
    return try { NSFileManager.defaultManager.fileExistsAtPath(path) } catch (e: Exception) { false }
}

actual fun createDocument(path: String): Document? {
    return try {
        if (!NSFileManager.defaultManager.fileExistsAtPath(path)) return null
        val url = NSURL.fileURLWithPath(path)
        val fileName = url.lastPathComponent ?: ""
        val extension = if (fileName.contains(".")) fileName.substringAfterLast(".", "") else ""
        val title = if (extension.isNotEmpty()) fileName.substringBeforeLast(".$extension") else fileName
        Document(path = path, title = title, extension = extension).apply { detectFormatByExtension() }
    } catch (e: Exception) { null }
}

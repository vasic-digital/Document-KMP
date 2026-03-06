package digital.vasic.document

import kotlin.test.*

class DocumentTest {

    @Test
    fun testDocumentCreation() {
        val doc = Document(path = "/test/document.md", title = "document", extension = "md", format = Document.FORMAT_MARKDOWN)
        assertEquals("/test/document.md", doc.path)
        assertEquals("document", doc.title)
        assertEquals("md", doc.extension)
        assertEquals(Document.FORMAT_MARKDOWN, doc.format)
    }

    @Test
    fun testFilename() {
        val doc = Document(path = "/test/document.md", title = "document", extension = "md")
        assertEquals("document.md", doc.filename)
    }

    @Test
    fun testFilenameWithoutExtension() {
        val doc = Document(path = "/test/document", title = "document", extension = "")
        assertEquals("document", doc.filename)
    }

    @Test
    fun testEquality() {
        val doc1 = Document(path = "/test/document.md", title = "document", extension = "md", format = Document.FORMAT_MARKDOWN)
        val doc2 = Document(path = "/test/document.md", title = "document", extension = "md", format = Document.FORMAT_MARKDOWN)
        assertEquals(doc1, doc2)
    }

    @Test
    fun testInequality() {
        val doc1 = Document(path = "/test/document1.md", title = "document1", extension = "md")
        val doc2 = Document(path = "/test/document2.md", title = "document2", extension = "md")
        assertFalse(doc1 == doc2)
    }

    @Test
    fun testFormatConstants() {
        assertEquals("markdown", Document.FORMAT_MARKDOWN)
        assertEquals("plaintext", Document.FORMAT_PLAINTEXT)
        assertEquals("todotxt", Document.FORMAT_TODOTXT)
        assertEquals("latex", Document.FORMAT_LATEX)
        assertEquals("unknown", Document.FORMAT_UNKNOWN)
    }

    @Test
    fun testResetChangeTracking() {
        val doc = Document(path = "/test/document.md", title = "document", extension = "md")
        doc.modTime = 123456L
        doc.touchTime = 789012L
        doc.resetChangeTracking()
        assertEquals(-1L, doc.modTime)
        assertEquals(-1L, doc.touchTime)
    }

    @Test
    fun testTouch() {
        val doc = Document(path = "/test/document.md", title = "document", extension = "md")
        val before = currentTimeMillis()
        doc.touch()
        val after = currentTimeMillis()
        assertTrue(doc.touchTime >= before)
        assertTrue(doc.touchTime <= after)
    }

    @Test
    fun testDetectFormatByExtension() {
        val doc = Document(path = "/test/document.md", title = "document", extension = "md", format = Document.FORMAT_UNKNOWN)
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_MARKDOWN, doc.format)
    }

    @Test
    fun testDetectFormatByExtensionTex() {
        val doc = Document(path = "/test/paper.tex", title = "paper", extension = "tex", format = Document.FORMAT_UNKNOWN)
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_LATEX, doc.format)
    }

    @Test
    fun testDetectFormatByContent() {
        val doc = Document(path = "/test/document.txt", title = "document", extension = "txt", format = Document.FORMAT_UNKNOWN)
        val detected = doc.detectFormatByContent("# Heading 1\n## Heading 2\nThis is markdown content")
        assertTrue(detected)
        assertEquals(Document.FORMAT_MARKDOWN, doc.format)
    }

    @Test
    fun testDetectFormatByContentNoMatch() {
        val doc = Document(path = "/test/document.txt", title = "document", extension = "txt", format = Document.FORMAT_UNKNOWN)
        val detected = doc.detectFormatByContent("Just some plain text")
        assertFalse(detected)
        assertEquals(Document.FORMAT_UNKNOWN, doc.format)
    }

    @Test
    fun testCurrentTimeMillis() {
        val time1 = currentTimeMillis()
        val time2 = currentTimeMillis()
        assertTrue(time2 >= time1)
        assertTrue(time1 > 1577836800000L)
    }

    @Test
    fun testDocumentFileOperations() {
        val doc = Document(path = "/definitely/does/not/exist/anywhere/file.txt", title = "file", extension = "txt")
        assertFalse(doc.fileExists())
        assertTrue(doc.getFileModTime() <= 0L)
        assertTrue(doc.getFileSize() >= 0L)
    }

    @Test
    fun testCreateDocument() {
        val doc = createDocument("/nonexistent/file.txt")
        assertNull(doc)
    }

    @Test
    fun testHasChanged() {
        val doc = Document(path = "/test/document.md", title = "document", extension = "md")
        assertTrue(doc.hasChanged())
        doc.modTime = currentTimeMillis()
        doc.touchTime = currentTimeMillis()
        assertFalse(doc.hasChanged())
        doc.resetChangeTracking()
        assertTrue(doc.hasChanged())
    }
}

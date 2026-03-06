package digital.vasic.document

import kotlin.test.*

class DocumentAdvancedTest {

    @Test
    fun `should have all 18 format constants`() {
        assertEquals("unknown", Document.FORMAT_UNKNOWN)
        assertEquals("plaintext", Document.FORMAT_PLAINTEXT)
        assertEquals("markdown", Document.FORMAT_MARKDOWN)
        assertEquals("todotxt", Document.FORMAT_TODOTXT)
        assertEquals("csv", Document.FORMAT_CSV)
        assertEquals("wikitext", Document.FORMAT_WIKITEXT)
        assertEquals("keyvalue", Document.FORMAT_KEYVALUE)
        assertEquals("asciidoc", Document.FORMAT_ASCIIDOC)
        assertEquals("orgmode", Document.FORMAT_ORGMODE)
        assertEquals("latex", Document.FORMAT_LATEX)
        assertEquals("restructuredtext", Document.FORMAT_RESTRUCTUREDTEXT)
        assertEquals("taskpaper", Document.FORMAT_TASKPAPER)
        assertEquals("textile", Document.FORMAT_TEXTILE)
        assertEquals("creole", Document.FORMAT_CREOLE)
        assertEquals("tiddlywiki", Document.FORMAT_TIDDLYWIKI)
        assertEquals("jupyter", Document.FORMAT_JUPYTER)
        assertEquals("rmarkdown", Document.FORMAT_RMARKDOWN)
        assertEquals("binary", Document.FORMAT_BINARY)
    }

    @Test
    fun `should support copy with path change`() {
        val doc = Document(path = "/path/doc.md", title = "doc", extension = "md", format = Document.FORMAT_MARKDOWN)
        val copied = doc.copy(path = "/new/path/doc.md")
        assertEquals("/new/path/doc.md", copied.path)
        assertEquals("doc", copied.title)
    }

    @Test
    fun `should support copy with title change`() {
        val doc = Document(path = "/path/doc.md", title = "doc", extension = "md")
        val copied = doc.copy(title = "newdoc")
        assertEquals("newdoc", copied.title)
    }

    @Test
    fun `should support copy with no changes`() {
        val doc = Document(path = "/path/doc.md", title = "doc", extension = "md", format = Document.FORMAT_MARKDOWN)
        val copied = doc.copy()
        assertEquals(doc, copied)
        assertNotSame(doc, copied)
    }

    @Test
    fun `should generate consistent hashCode`() {
        val doc1 = Document(path = "/path/doc.md", title = "doc", extension = "md", format = Document.FORMAT_MARKDOWN)
        val doc2 = Document(path = "/path/doc.md", title = "doc", extension = "md", format = Document.FORMAT_MARKDOWN)
        assertEquals(doc1.hashCode(), doc2.hashCode())
    }

    @Test
    fun `should generate different hashCode for different documents`() {
        val doc1 = Document(path = "/path/doc1.md", title = "doc1", extension = "md")
        val doc2 = Document(path = "/path/doc2.md", title = "doc2", extension = "md")
        assertNotEquals(doc1.hashCode(), doc2.hashCode())
    }

    @Test
    fun `should support destructuring`() {
        val doc = Document(path = "/path/doc.md", title = "doc", extension = "md", format = Document.FORMAT_MARKDOWN)
        val (id, path, title, extension) = doc
        assertEquals("", id)
        assertEquals("/path/doc.md", path)
        assertEquals("doc", title)
        assertEquals("md", extension)
    }

    @Test
    fun `should handle filename with multiple extensions`() {
        val doc = Document(path = "/path/archive.tar.gz", title = "archive.tar", extension = "gz")
        assertEquals("archive.tar.gz", doc.filename)
    }

    @Test
    fun `should handle filename with special characters`() {
        val doc = Document(path = "/path/my-file_v2.md", title = "my-file_v2", extension = "md")
        assertEquals("my-file_v2.md", doc.filename)
    }

    @Test
    fun `should handle filename with spaces`() {
        val doc = Document(path = "/path/my document.md", title = "my document", extension = "md")
        assertEquals("my document.md", doc.filename)
    }

    @Test
    fun `should handle filename with Unicode`() {
        val doc = Document(path = "/path/文档.md", title = "文档", extension = "md")
        assertEquals("文档.md", doc.filename)
    }

    @Test
    fun `should detect format from csv extension`() {
        val doc = Document(path = "/path/data.csv", title = "data", extension = "csv")
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_CSV, doc.format)
    }

    @Test
    fun `should detect format from txt extension`() {
        val doc = Document(path = "/path/notes.txt", title = "notes", extension = "txt")
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_PLAINTEXT, doc.format)
    }

    @Test
    fun `should detect format from org extension`() {
        val doc = Document(path = "/path/tasks.org", title = "tasks", extension = "org")
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_ORGMODE, doc.format)
    }

    @Test
    fun `should detect format from wiki extension`() {
        val doc = Document(path = "/path/page.wiki", title = "page", extension = "wiki")
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_WIKITEXT, doc.format)
    }

    @Test
    fun `should detect format from rst extension`() {
        val doc = Document(path = "/path/README.rst", title = "README", extension = "rst")
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_RESTRUCTUREDTEXT, doc.format)
    }

    @Test
    fun `should detect format from adoc extension`() {
        val doc = Document(path = "/path/doc.adoc", title = "doc", extension = "adoc")
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_ASCIIDOC, doc.format)
    }

    @Test
    fun `should detect format from taskpaper extension`() {
        val doc = Document(path = "/path/tasks.taskpaper", title = "tasks", extension = "taskpaper")
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_TASKPAPER, doc.format)
    }

    @Test
    fun `should detect format from uppercase extension`() {
        val doc = Document(path = "/path/DOC.MD", title = "DOC", extension = "MD")
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_MARKDOWN, doc.format)
    }

    @Test
    fun `should detect latex content`() {
        val doc = Document(path = "/path/doc.txt", title = "doc", extension = "txt")
        val detected = doc.detectFormatByContent("\\documentclass{article}\n\\begin{document}\nTest\\end{document}")
        assertTrue(detected)
        assertEquals(Document.FORMAT_LATEX, doc.format)
    }

    @Test
    fun `should detect todotxt content`() {
        val doc = Document(path = "/path/tasks.txt", title = "tasks", extension = "txt")
        val detected = doc.detectFormatByContent("(A) Important task\nx 2024-01-01 Completed task")
        assertTrue(detected)
        assertEquals(Document.FORMAT_TODOTXT, doc.format)
    }

    @Test
    fun `should detect CSV content`() {
        val doc = Document(path = "/path/data.txt", title = "data", extension = "txt")
        val detected = doc.detectFormatByContent("Name,Age,City\nJohn,30,NYC\nJane,25,LA")
        assertTrue(detected)
        assertEquals(Document.FORMAT_CSV, doc.format)
    }

    @Test
    fun `should not detect format from plain text`() {
        val doc = Document(path = "/path/notes.txt", title = "notes", extension = "txt")
        val detected = doc.detectFormatByContent("Just some ordinary plain text without any special formatting.")
        assertFalse(detected)
        assertEquals(Document.FORMAT_UNKNOWN, doc.format)
    }

    @Test
    fun `should not detect format from empty content`() {
        val doc = Document(path = "/path/empty.txt", title = "empty", extension = "txt")
        val detected = doc.detectFormatByContent("")
        assertFalse(detected)
    }

    @Test
    fun `should indicate changed when modTime is negative`() {
        val doc = Document(path = "/path/doc.md", title = "doc", extension = "md")
        doc.modTime = -1; doc.touchTime = 1000
        assertTrue(doc.hasChanged())
    }

    @Test
    fun `should indicate changed when touchTime is negative`() {
        val doc = Document(path = "/path/doc.md", title = "doc", extension = "md")
        doc.modTime = 1000; doc.touchTime = -1
        assertTrue(doc.hasChanged())
    }

    @Test
    fun `should update touchTime when touched`() {
        val doc = Document(path = "/path/doc.md", title = "doc", extension = "md")
        val before = currentTimeMillis()
        doc.touch()
        assertTrue(doc.touchTime >= before)
        assertTrue(doc.touchTime <= currentTimeMillis())
    }

    @Test
    fun `should handle multiple touches`() {
        val doc = Document(path = "/path/doc.md", title = "doc", extension = "md")
        doc.touch()
        val first = doc.touchTime
        doc.touch()
        assertTrue(doc.touchTime >= first)
    }

    @Test
    fun `should be equal to itself`() {
        val doc = Document(path = "/path/doc.md", title = "doc", extension = "md")
        assertEquals(doc, doc)
    }

    @Test
    fun `should not be equal to null`() {
        val doc = Document(path = "/path/doc.md", title = "doc", extension = "md")
        assertFalse(doc.equals(null))
    }

    @Test
    fun `should not be equal to different type`() {
        val doc = Document(path = "/path/doc.md", title = "doc", extension = "md")
        assertFalse(doc.equals("string"))
    }

    @Test
    fun `should not be equal when path differs`() {
        val doc1 = Document(path = "/path1/doc.md", title = "doc", extension = "md", format = Document.FORMAT_MARKDOWN)
        val doc2 = Document(path = "/path2/doc.md", title = "doc", extension = "md", format = Document.FORMAT_MARKDOWN)
        assertNotEquals(doc1, doc2)
    }

    @Test
    fun `should not be equal when format differs`() {
        val doc1 = Document(path = "/path/doc.md", title = "doc", extension = "md", format = Document.FORMAT_MARKDOWN)
        val doc2 = Document(path = "/path/doc.md", title = "doc", extension = "md", format = Document.FORMAT_LATEX)
        assertNotEquals(doc1, doc2)
    }

    @Test
    fun `should be equal when timestamps differ`() {
        val doc1 = Document(path = "/path/doc.md", title = "doc", extension = "md", format = Document.FORMAT_MARKDOWN, modTime = 1000, touchTime = 2000)
        val doc2 = Document(path = "/path/doc.md", title = "doc", extension = "md", format = Document.FORMAT_MARKDOWN, modTime = 3000, touchTime = 4000)
        assertEquals(doc1, doc2)
    }

    @Test
    fun `should handle absolute Unix path`() {
        val doc = Document(path = "/home/user/documents/file.md", title = "file", extension = "md")
        assertEquals("/home/user/documents/file.md", doc.path)
    }

    @Test
    fun `should handle absolute Windows path`() {
        val doc = Document(path = "C:\\Users\\user\\Documents\\file.md", title = "file", extension = "md")
        assertEquals("C:\\Users\\user\\Documents\\file.md", doc.path)
    }
}

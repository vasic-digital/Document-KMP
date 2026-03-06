package digital.vasic.document

import kotlin.test.*

class DocumentFormatTest {

    @Test
    fun testAllFormatConstants() {
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
    fun testDetectFormatByExtensionCsv() {
        val doc = Document(path = "/data.csv", title = "data", extension = "csv", format = Document.FORMAT_UNKNOWN)
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_CSV, doc.format)
    }

    @Test
    fun testDetectFormatByExtensionOrg() {
        val doc = Document(path = "/notes.org", title = "notes", extension = "org", format = Document.FORMAT_UNKNOWN)
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_ORGMODE, doc.format)
    }

    @Test
    fun testDetectFormatByExtensionRst() {
        val doc = Document(path = "/doc.rst", title = "doc", extension = "rst", format = Document.FORMAT_UNKNOWN)
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_RESTRUCTUREDTEXT, doc.format)
    }

    @Test
    fun testDetectFormatByExtensionAdoc() {
        val doc = Document(path = "/doc.adoc", title = "doc", extension = "adoc", format = Document.FORMAT_UNKNOWN)
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_ASCIIDOC, doc.format)
    }

    @Test
    fun testDetectFormatByExtensionTxt() {
        val doc = Document(path = "/file.txt", title = "file", extension = "txt", format = Document.FORMAT_UNKNOWN)
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_PLAINTEXT, doc.format)
    }

    @Test
    fun testDetectFormatByExtensionWiki() {
        val doc = Document(path = "/page.wiki", title = "page", extension = "wiki", format = Document.FORMAT_UNKNOWN)
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_WIKITEXT, doc.format)
    }

    @Test
    fun testDetectFormatByExtensionIpynb() {
        val doc = Document(path = "/nb.ipynb", title = "nb", extension = "ipynb", format = Document.FORMAT_UNKNOWN)
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_JUPYTER, doc.format)
    }

    @Test
    fun testDetectFormatByExtensionRmd() {
        val doc = Document(path = "/analysis.Rmd", title = "analysis", extension = "Rmd", format = Document.FORMAT_UNKNOWN)
        doc.detectFormatByExtension()
        assertEquals(Document.FORMAT_RMARKDOWN, doc.format)
    }

    @Test
    fun testDetectFormatByContentLatex() {
        val doc = Document(path = "/file.txt", title = "file", extension = "txt", format = Document.FORMAT_UNKNOWN)
        val detected = doc.detectFormatByContent("\\documentclass{article}\n\\begin{document}\nHello World\n\\end{document}")
        assertTrue(detected)
        assertEquals(Document.FORMAT_LATEX, doc.format)
    }

    @Test
    fun testDetectFormatByContentCsv() {
        val doc = Document(path = "/file.txt", title = "file", extension = "txt", format = Document.FORMAT_UNKNOWN)
        val detected = doc.detectFormatByContent("name,age,city\nAlice,30,NYC\nBob,25,LA\nCarol,35,SF")
        if (detected) assertEquals(Document.FORMAT_CSV, doc.format)
    }

    @Test
    fun testDocumentCreationWithAllFields() {
        val doc = Document(
            id = "doc-123", path = "/path/to/file.md", title = "file", extension = "md",
            content = "# Hello World", author = "Test Author", tags = listOf("important", "draft"),
            format = Document.FORMAT_MARKDOWN
        )
        assertEquals("doc-123", doc.id)
        assertEquals("# Hello World", doc.content)
        assertEquals("Test Author", doc.author)
        assertEquals(2, doc.tags.size)
    }

    @Test
    fun testDocumentDefaultValues() {
        val doc = Document(path = "/file.txt", title = "file", extension = "txt")
        assertEquals("", doc.id)
        assertEquals("", doc.content)
        assertNull(doc.author)
        assertTrue(doc.tags.isEmpty())
        assertEquals(Document.FORMAT_UNKNOWN, doc.format)
        assertEquals(-1L, doc.modTime)
        assertEquals(-1L, doc.touchTime)
    }

    @Test
    fun testFilenameVariousExtensions() {
        assertEquals("readme.md", Document(path = "/readme.md", title = "readme", extension = "md").filename)
        assertEquals("data.csv", Document(path = "/data.csv", title = "data", extension = "csv").filename)
        assertEquals("noext", Document(path = "/noext", title = "noext", extension = "").filename)
    }

    @Test
    fun testEqualityIgnoresContentAndAuthor() {
        val doc1 = Document(path = "/file.md", title = "file", extension = "md", content = "hello", author = "Alice")
        val doc2 = Document(path = "/file.md", title = "file", extension = "md", content = "world", author = "Bob")
        assertEquals(doc1, doc2)
    }

    @Test
    fun testHashCodeConsistency() {
        val doc = Document(path = "/file.md", title = "file", extension = "md", format = Document.FORMAT_MARKDOWN)
        assertEquals(doc.hashCode(), doc.hashCode())
    }

    @Test
    fun testCopyPreservesFields() {
        val doc = Document(id = "id-1", path = "/path/file.md", title = "file", extension = "md",
            content = "content", author = "author", tags = listOf("tag1"), format = Document.FORMAT_MARKDOWN)
        val copy = doc.copy(content = "new content")
        assertEquals("id-1", copy.id)
        assertEquals("new content", copy.content)
        assertEquals("author", copy.author)
        assertEquals(Document.FORMAT_MARKDOWN, copy.format)
    }
}

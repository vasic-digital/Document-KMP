package digital.vasic.document

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.*

class DocumentStressTest {

    @Test
    fun `create many documents concurrently`() = runTest {
        val documents = (1..1000).map { i ->
            async {
                Document(path = "/path/to/doc$i.md", title = "doc$i", extension = "md", format = Document.FORMAT_MARKDOWN)
            }
        }.awaitAll()
        assertEquals(1000, documents.size)
        assertTrue(documents.all { it.format == Document.FORMAT_MARKDOWN })
    }

    @Test
    fun `create documents with all format types`() = runTest {
        val formats = listOf(
            Document.FORMAT_PLAINTEXT to "txt", Document.FORMAT_MARKDOWN to "md",
            Document.FORMAT_TODOTXT to "txt", Document.FORMAT_CSV to "csv",
            Document.FORMAT_LATEX to "tex", Document.FORMAT_ASCIIDOC to "adoc",
            Document.FORMAT_ORGMODE to "org", Document.FORMAT_WIKITEXT to "wiki",
            Document.FORMAT_RESTRUCTUREDTEXT to "rst", Document.FORMAT_TASKPAPER to "taskpaper",
            Document.FORMAT_TEXTILE to "textile", Document.FORMAT_CREOLE to "creole",
            Document.FORMAT_TIDDLYWIKI to "tid", Document.FORMAT_JUPYTER to "ipynb",
            Document.FORMAT_RMARKDOWN to "rmd", Document.FORMAT_KEYVALUE to "ini",
            Document.FORMAT_BINARY to "bin"
        )
        formats.forEach { (format, extension) ->
            val doc = Document(path = "/path/to/file.$extension", title = "file", extension = extension, format = format)
            assertEquals(format, doc.format)
        }
    }

    @Test
    fun `detect format by extension for all supported extensions`() = runTest {
        val documents = FormatDetector.getAllExtensions().map { ext ->
            async {
                val doc = Document(path = "/path/to/file$ext", title = "file", extension = ext.removePrefix("."), format = Document.FORMAT_UNKNOWN)
                doc.detectFormatByExtension()
                doc
            }
        }.awaitAll()
        assertTrue(documents.all { it.format != Document.FORMAT_UNKNOWN })
    }

    @Test
    fun `detect format by content concurrently`() = runTest {
        val contentSamples = listOf(
            "# Markdown Heading\n\nParagraph with **bold** text.",
            "(A) Todo task @context +project",
            "a,b,c\n1,2,3",
            "\\documentclass{article}\n\\begin{document}",
            "* Org heading\n** Sub heading",
            "== Wiki Heading =="
        )
        val results = contentSamples.map { content ->
            async {
                val doc = Document(path = "/path/to/file.txt", title = "file", extension = "txt", format = Document.FORMAT_UNKNOWN)
                doc.detectFormatByContent(content)
                doc
            }
        }.awaitAll()
        assertEquals(contentSamples.size, results.size)
    }

    @Test
    fun `format detection is idempotent`() = runTest {
        val doc = Document(path = "/path/to/file.md", title = "file", extension = "md", format = Document.FORMAT_UNKNOWN)
        repeat(100) { doc.detectFormatByExtension() }
        assertEquals(Document.FORMAT_MARKDOWN, doc.format)
    }

    @Test
    fun `filename property handles various extensions`() = runTest {
        val testCases = listOf(
            Triple("doc", "md", "doc.md"), Triple("doc", "", "doc"),
            Triple("doc.backup", "md", "doc.backup.md"), Triple("", "md", ".md")
        )
        testCases.forEach { (title, ext, expected) ->
            val doc = Document(path = "/path/to/$expected", title = title, extension = ext)
            assertEquals(expected, doc.filename)
        }
    }

    @Test
    fun `change tracking reset is consistent`() = runTest {
        val doc = Document(path = "/path/to/file.md", title = "file", extension = "md", modTime = 1000L, touchTime = 2000L)
        repeat(100) {
            doc.resetChangeTracking()
            assertEquals(-1L, doc.modTime)
            assertEquals(-1L, doc.touchTime)
        }
    }

    @Test
    fun `touch updates time correctly`() = runTest {
        val doc = Document(path = "/path/to/file.md", title = "file", extension = "md")
        val times = (1..100).map { doc.touch(); doc.touchTime }
        times.zipWithNext().forEach { (prev, curr) -> assertTrue(curr >= prev) }
    }

    @Test
    fun `serialize and deserialize many documents`() = runTest {
        val json = Json { ignoreUnknownKeys = true }
        val documents = (1..500).map { i ->
            Document(path = "/path/to/doc$i.md", title = "doc$i", extension = "md", format = Document.FORMAT_MARKDOWN)
        }
        val serialized = documents.map { json.encodeToString(Document.serializer(), it) }
        val deserialized = serialized.map { json.decodeFromString(Document.serializer(), it) }
        assertEquals(documents.size, deserialized.size)
        documents.zip(deserialized).forEach { (original, restored) ->
            assertEquals(original.path, restored.path)
            assertEquals(original.title, restored.title)
            assertEquals(original.format, restored.format)
        }
    }

    @Test
    fun `serialization handles special characters`() = runTest {
        val json = Json { ignoreUnknownKeys = true }
        val specialDocs = listOf(
            Document(path = "/path/with spaces/file.md", title = "file with spaces", extension = "md"),
            Document(path = "/path/unicode/\u00e9\u00e8\u00ea/file.md", title = "\u00e9\u00e8\u00ea", extension = "md")
        )
        specialDocs.forEach { doc ->
            val serialized = json.encodeToString(Document.serializer(), doc)
            val restored = json.decodeFromString(Document.serializer(), serialized)
            assertEquals(doc.path, restored.path)
            assertEquals(doc.title, restored.title)
        }
    }

    @Test
    fun `equals and hashCode are consistent`() = runTest {
        val documents = (1..1000).map { i ->
            Document(path = "/path/to/doc${i % 100}.md", title = "doc${i % 100}", extension = "md", format = Document.FORMAT_MARKDOWN)
        }
        val grouped = documents.groupBy { it }
        grouped.forEach { (_, equalDocs) ->
            assertTrue(equalDocs.all { it.hashCode() == equalDocs.first().hashCode() })
        }
    }

    @Test
    fun `document handles empty path`() = runTest {
        val doc = Document(path = "", title = "", extension = "")
        assertEquals("", doc.path)
        assertEquals("", doc.filename)
    }

    @Test
    fun `document handles very long paths`() = runTest {
        val longPath = "/" + "a".repeat(10000) + "/file.md"
        val doc = Document(path = longPath, title = "file", extension = "md")
        assertEquals(longPath, doc.path)
    }

    @Test
    fun `document handles unicode paths`() = runTest {
        listOf("/documents/\u6587\u6863/file.md", "/\u0434\u043e\u043a\u0443\u043c\u0435\u043d\u0442\u044b/file.md").forEach { path ->
            val doc = Document(path = path, title = "file", extension = "md")
            assertEquals(path, doc.path)
        }
    }
}

package digital.vasic.document

import kotlin.test.*

class FormatDetectorTest {

    @Test
    fun `should detect markdown by extension`() {
        assertEquals(Document.FORMAT_MARKDOWN, FormatDetector.detectByExtension("md"))
        assertEquals(Document.FORMAT_MARKDOWN, FormatDetector.detectByExtension("markdown"))
        assertEquals(Document.FORMAT_MARKDOWN, FormatDetector.detectByExtension("MD"))
    }

    @Test
    fun `should detect plaintext by extension`() {
        assertEquals(Document.FORMAT_PLAINTEXT, FormatDetector.detectByExtension("txt"))
        assertEquals(Document.FORMAT_PLAINTEXT, FormatDetector.detectByExtension("text"))
    }

    @Test
    fun `should detect csv by extension`() {
        assertEquals(Document.FORMAT_CSV, FormatDetector.detectByExtension("csv"))
        assertEquals(Document.FORMAT_CSV, FormatDetector.detectByExtension("tsv"))
    }

    @Test
    fun `should detect latex by extension`() {
        assertEquals(Document.FORMAT_LATEX, FormatDetector.detectByExtension("tex"))
        assertEquals(Document.FORMAT_LATEX, FormatDetector.detectByExtension("latex"))
    }

    @Test
    fun `should detect orgmode by extension`() {
        assertEquals(Document.FORMAT_ORGMODE, FormatDetector.detectByExtension("org"))
    }

    @Test
    fun `should detect wikitext by extension`() {
        assertEquals(Document.FORMAT_WIKITEXT, FormatDetector.detectByExtension("wiki"))
    }

    @Test
    fun `should detect restructuredtext by extension`() {
        assertEquals(Document.FORMAT_RESTRUCTUREDTEXT, FormatDetector.detectByExtension("rst"))
    }

    @Test
    fun `should detect asciidoc by extension`() {
        assertEquals(Document.FORMAT_ASCIIDOC, FormatDetector.detectByExtension("adoc"))
        assertEquals(Document.FORMAT_ASCIIDOC, FormatDetector.detectByExtension("asciidoc"))
    }

    @Test
    fun `should detect jupyter by extension`() {
        assertEquals(Document.FORMAT_JUPYTER, FormatDetector.detectByExtension("ipynb"))
    }

    @Test
    fun `should detect rmarkdown by extension`() {
        assertEquals(Document.FORMAT_RMARKDOWN, FormatDetector.detectByExtension("rmd"))
        assertEquals(Document.FORMAT_RMARKDOWN, FormatDetector.detectByExtension("Rmd"))
    }

    @Test
    fun `should detect keyvalue by extension`() {
        assertEquals(Document.FORMAT_KEYVALUE, FormatDetector.detectByExtension("ini"))
        assertEquals(Document.FORMAT_KEYVALUE, FormatDetector.detectByExtension("toml"))
        assertEquals(Document.FORMAT_KEYVALUE, FormatDetector.detectByExtension("yaml"))
        assertEquals(Document.FORMAT_KEYVALUE, FormatDetector.detectByExtension("json"))
    }

    @Test
    fun `should default to plaintext for unknown extension`() {
        assertEquals(Document.FORMAT_PLAINTEXT, FormatDetector.detectByExtension("xyz"))
        assertEquals(Document.FORMAT_PLAINTEXT, FormatDetector.detectByExtension("unknown"))
    }

    @Test
    fun `should detect latex content`() {
        assertEquals(Document.FORMAT_LATEX, FormatDetector.detectByContent("\\documentclass{article}"))
        assertEquals(Document.FORMAT_LATEX, FormatDetector.detectByContent("\\begin{document}\nHello\n\\end{document}"))
    }

    @Test
    fun `should detect todotxt content`() {
        assertEquals(Document.FORMAT_TODOTXT, FormatDetector.detectByContent("(A) Important task"))
        assertEquals(Document.FORMAT_TODOTXT, FormatDetector.detectByContent("x 2024-01-01 Completed task"))
    }

    @Test
    fun `should detect markdown content`() {
        assertEquals(Document.FORMAT_MARKDOWN, FormatDetector.detectByContent("# Heading\nSome text"))
        assertEquals(Document.FORMAT_MARKDOWN, FormatDetector.detectByContent("## Subheading"))
    }

    @Test
    fun `should detect csv content`() {
        assertEquals(Document.FORMAT_CSV, FormatDetector.detectByContent("a,b,c\n1,2,3"))
    }

    @Test
    fun `should return null for empty content`() {
        assertNull(FormatDetector.detectByContent(""))
        assertNull(FormatDetector.detectByContent("   "))
    }

    @Test
    fun `should return null for unrecognized content`() {
        assertNull(FormatDetector.detectByContent("Just plain text with no special formatting."))
    }

    @Test
    fun `getAllExtensions should return non-empty list`() {
        val extensions = FormatDetector.getAllExtensions()
        assertTrue(extensions.isNotEmpty())
        assertTrue(extensions.all { it.startsWith(".") })
        assertTrue(extensions.contains(".md"))
        assertTrue(extensions.contains(".txt"))
    }
}

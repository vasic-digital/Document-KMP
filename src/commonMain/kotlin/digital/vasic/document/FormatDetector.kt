package digital.vasic.document

/**
 * Simple format detection based on file extension and content analysis.
 * Provides a default set of extension-to-format mappings and content detection patterns.
 */
object FormatDetector {

    private val extensionMap: Map<String, String> = buildMap {
        put("md", Document.FORMAT_MARKDOWN)
        put("markdown", Document.FORMAT_MARKDOWN)
        put("mdown", Document.FORMAT_MARKDOWN)
        put("mkd", Document.FORMAT_MARKDOWN)
        put("mkdn", Document.FORMAT_MARKDOWN)
        put("txt", Document.FORMAT_PLAINTEXT)
        put("text", Document.FORMAT_PLAINTEXT)
        put("csv", Document.FORMAT_CSV)
        put("tsv", Document.FORMAT_CSV)
        put("tex", Document.FORMAT_LATEX)
        put("latex", Document.FORMAT_LATEX)
        put("org", Document.FORMAT_ORGMODE)
        put("wiki", Document.FORMAT_WIKITEXT)
        put("mediawiki", Document.FORMAT_WIKITEXT)
        put("rst", Document.FORMAT_RESTRUCTUREDTEXT)
        put("rest", Document.FORMAT_RESTRUCTUREDTEXT)
        put("adoc", Document.FORMAT_ASCIIDOC)
        put("asciidoc", Document.FORMAT_ASCIIDOC)
        put("asc", Document.FORMAT_ASCIIDOC)
        put("taskpaper", Document.FORMAT_TASKPAPER)
        put("textile", Document.FORMAT_TEXTILE)
        put("creole", Document.FORMAT_CREOLE)
        put("tid", Document.FORMAT_TIDDLYWIKI)
        put("tiddlywiki", Document.FORMAT_TIDDLYWIKI)
        put("ipynb", Document.FORMAT_JUPYTER)
        put("rmd", Document.FORMAT_RMARKDOWN)
        put("rmarkdown", Document.FORMAT_RMARKDOWN)
        put("ini", Document.FORMAT_KEYVALUE)
        put("cfg", Document.FORMAT_KEYVALUE)
        put("conf", Document.FORMAT_KEYVALUE)
        put("properties", Document.FORMAT_KEYVALUE)
        put("toml", Document.FORMAT_KEYVALUE)
        put("yaml", Document.FORMAT_KEYVALUE)
        put("yml", Document.FORMAT_KEYVALUE)
        put("json", Document.FORMAT_KEYVALUE)
        put("bin", Document.FORMAT_BINARY)
        put("exe", Document.FORMAT_BINARY)
        put("dll", Document.FORMAT_BINARY)
        put("so", Document.FORMAT_BINARY)
        put("dylib", Document.FORMAT_BINARY)
    }

    private data class ContentPattern(val regex: Regex, val format: String)

    private val contentPatterns: List<ContentPattern> = listOf(
        ContentPattern(Regex("\\\\documentclass\\{"), Document.FORMAT_LATEX),
        ContentPattern(Regex("\\\\begin\\{document\\}"), Document.FORMAT_LATEX),
        ContentPattern(Regex("^\\(\\w\\)\\s+", RegexOption.MULTILINE), Document.FORMAT_TODOTXT),
        ContentPattern(Regex("^x\\s+\\d{4}-\\d{2}-\\d{2}\\s+", RegexOption.MULTILINE), Document.FORMAT_TODOTXT),
        ContentPattern(Regex("^#{1,6}\\s+.+", RegexOption.MULTILINE), Document.FORMAT_MARKDOWN),
        ContentPattern(Regex("^\\*{1,3}\\s+.+", RegexOption.MULTILINE), Document.FORMAT_ORGMODE),
        ContentPattern(Regex("^=={1,5}\\s+.+\\s+=={1,5}", RegexOption.MULTILINE), Document.FORMAT_WIKITEXT),
        ContentPattern(Regex("^[^,\\n]+,[^,\\n]+,[^,\\n]+\\n[^,\\n]+,[^,\\n]+,[^,\\n]+", RegexOption.MULTILINE), Document.FORMAT_CSV),
    )

    fun detectByExtension(extension: String): String {
        return extensionMap[extension.lowercase()] ?: Document.FORMAT_PLAINTEXT
    }

    fun detectByContent(content: String): String? {
        if (content.isBlank()) return null
        for (pattern in contentPatterns) {
            if (pattern.regex.containsMatchIn(content)) {
                return pattern.format
            }
        }
        return null
    }

    fun getAllExtensions(): List<String> {
        return extensionMap.keys.map { ".$it" }
    }
}

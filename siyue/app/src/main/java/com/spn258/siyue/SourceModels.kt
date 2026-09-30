package com.spn258.siyue

import com.google.gson.JsonParser

data class ImportedSource(val name: String, val url: String, val rawJson: String)

object SourceDraftParser {
    fun parse(raw: String): Result<ImportedSource> = runCatching {
        val json = JsonParser.parseString(raw).asJsonObject
        val name = json.get("bookSourceName")?.asString?.trim().orEmpty()
        val url = json.get("bookSourceUrl")?.asString?.trim().orEmpty()
        require(name.isNotEmpty()) { "缺少 bookSourceName" }
        require(url.isNotEmpty()) { "缺少 bookSourceUrl" }
        require(json.has("searchUrl")) { "缺少 searchUrl" }
        ImportedSource(name, url, raw)
    }
}

interface BookSourceEngine {
    suspend fun search(keyword: String): List<SearchBook>
}

data class SearchBook(val name: String, val author: String, val sourceName: String)

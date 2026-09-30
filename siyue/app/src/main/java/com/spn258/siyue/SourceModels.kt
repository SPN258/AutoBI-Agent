package com.spn258.siyue

import org.json.JSONObject

data class ImportedSource(val name: String, val url: String, val rawJson: String)

object SourceDraftParser {
    fun parse(raw: String): Result<ImportedSource> = runCatching {
        val json = JSONObject(raw)
        val name = json.optString("bookSourceName").trim()
        val url = json.optString("bookSourceUrl").trim()
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

package com.spn258.siyue

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SourceDraftParserTest {
    @Test
    fun parsesLegadoShapeWithoutPretendingToExecuteRules() {
        val result = SourceDraftParser.parse(
            """{"bookSourceName":"Fixture","bookSourceUrl":"https://example.invalid","searchUrl":"https://example.invalid/search?q={{key}}"}"""
        )
        assertTrue(result.isSuccess)
        assertEquals("Fixture", result.getOrThrow().name)
    }

    @Test
    fun rejectsMissingRequiredShape() {
        assertTrue(SourceDraftParser.parse("""{"bookSourceName":"x"}""").isFailure)
    }
}

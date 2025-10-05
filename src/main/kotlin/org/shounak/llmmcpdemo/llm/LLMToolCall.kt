package org.shounak.llmmcpdemo.llm

data class ToolCall(
    val tool: String,
    val city: String? = null,
    val date: String? = null,
    val explain: String? = null
)

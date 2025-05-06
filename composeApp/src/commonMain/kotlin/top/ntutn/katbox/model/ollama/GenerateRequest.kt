package top.ntutn.katbox.model.ollama

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GenerateRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean,
    val context: List<Int>? // context 已经被废弃，检查下用什么代替。
)

@Serializable
data class GenerateResponse(
    val model: String,
    @SerialName("created_at") val createdAt: String,
    val response: String,
    val done: Boolean,
    val context: List<Int>?,
    @SerialName("total_duration") val totalDuration: Long?,
)
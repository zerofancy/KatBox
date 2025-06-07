package top.ntutn.katbox.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class ChatMessage @OptIn(ExperimentalUuidApi::class) constructor(val uuid: String = Uuid.random().toString(), val timestamp: Long, val text: String, val role: Role, val completed: Boolean)

enum class Role {
    USER,
    ASSISTANT,
    SYSTEM
}
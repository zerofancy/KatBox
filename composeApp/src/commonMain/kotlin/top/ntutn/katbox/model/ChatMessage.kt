package top.ntutn.katbox.model

data class ChatMessage(val timestamp: Long, val text: String, val role: String, val completed: Boolean)
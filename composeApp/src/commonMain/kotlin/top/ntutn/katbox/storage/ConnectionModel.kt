package top.ntutn.katbox.storage

import kotlinx.serialization.Serializable

@Serializable
data class ConnectionModel(val url: String)
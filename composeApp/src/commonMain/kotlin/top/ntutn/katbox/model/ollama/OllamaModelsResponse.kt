package top.ntutn.katbox.model.ollama

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OllamaModelsResponse(
    @SerialName("models") val models: List<Model>
)

@Serializable
data class Model(
    @SerialName("name") val name: String,
    @SerialName("model") val model: String,
    @SerialName("modified_at") val modifiedAt: String,
    @SerialName("size") val size: Long,
    @SerialName("digest") val digest: String,
    @SerialName("details") val details: ModelDetails
)

@Serializable
data class ModelDetails(
    @SerialName("parent_model") val parentModel: String,
    @SerialName("format") val format: String,
    @SerialName("family") val family: String,
    @SerialName("families") val families: List<String>,
    @SerialName("parameter_size") val parameterSize: String,
    @SerialName("quantization_level") val quantizationLevel: String
)

package codes.drinky.testapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Uploads(
    val uploads: ArrayList<Upload>
)

@Serializable
data class Upload(
    val uploadDate: Long,
    val url: String,
)
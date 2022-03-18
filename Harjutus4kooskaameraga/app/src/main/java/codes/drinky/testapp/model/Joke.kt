package codes.drinky.testapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Joke(
    var joke: String,
    var category: String,
)

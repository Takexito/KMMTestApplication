package com.example.kmmtestapplication.sample.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PokemonsByTypeResponse(
    @SerialName("pokemon") val pokemons: List<PokemonWrapperResponse>
)

@Serializable
class PokemonResponse(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
class PokemonWrapperResponse(
    @SerialName("pokemon") val pokemon: PokemonResponse
)
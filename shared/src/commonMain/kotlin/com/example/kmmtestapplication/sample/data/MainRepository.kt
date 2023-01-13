package com.example.kmmtestapplication.sample.data

import com.example.kmmtestapplication.network.setDefaultErrorHandler
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class MainRepository {
    suspend fun getPokemonByType(): PokemonsByTypeResponse {
        val client = HttpClient() {
            setDefaultErrorHandler()
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val pokemonResponse = client.get {
            url("https://pokeapi.co/api/v2/type/11")
        }

        return pokemonResponse.body()

    }
}
package com.example.pokmon_explorer.data

import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApi {
    @GET("type/{name}")
    suspend fun getTypeByName(@Path("name") name: String): TypeResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonByName(@Path("name") name: String): PokemonResponse
}
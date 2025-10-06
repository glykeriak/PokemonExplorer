package com.example.pokmon_explorer.data

import com.google.gson.annotations.SerializedName

data class TypeResponse(val pokemon: List<PokemonSlot>)

data class PokemonSlot(val pokemon: NamedResource, val slot: Int?= null)

data class NamedResource(val name: String, val url: String)

data class PokemonResponse(val name: String, val sprites: Sprites, val stats: List<Stat>)

data class Sprites(val other: OtherSprites)

data class OtherSprites(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork)

data class OfficialArtwork(
    @SerializedName("front_default")
    val front_default: String?)

data class Stat(val base_stat: Int, val stat: StatName)

data class StatName(val name: String)

data class CardItem(val title : String, val imageUrl : String? = null)
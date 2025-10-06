package com.example.pokmon_explorer

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokmon_explorer.data.ApiClient
import com.example.pokmon_explorer.data.CardItem
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var myRecyclerView: RecyclerView
    private lateinit var myAdapter: Adapter
    private val myCards= mutableListOf<CardItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        myRecyclerView = findViewById(R.id.typeRecyclerView)
        myRecyclerView.layoutManager = GridLayoutManager(this, 2)

        myAdapter = Adapter(myCards) { tapped ->
            val i = Intent(this, SelectedPokemonActivity::class.java)
            i.putExtra("type_name", tapped.title.lowercase())
            startActivity(i)
        }
        myRecyclerView.adapter = myAdapter


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadTypes()

    }

    private fun loadTypes(){
        val types = listOf("fire", "water", "grass", "electric", "dragon", "psychic", "ghost", "dark", "steel", "fairy")

        lifecycleScope.launch {
            try {
                for(type in types){
                    val response = ApiClient.api.getTypeByName(type)
                    val firstPokemonName = response.pokemon.firstOrNull()?.pokemon?.name ?: continue
                    val pokemonDeail = ApiClient.api.getPokemonByName(firstPokemonName)
                    val imageUrl = pokemonDeail.sprites.other.officialArtwork.front_default

                    myCards.add(
                        CardItem(
                            title = type.replaceFirstChar { it.uppercase() },
                            imageUrl = imageUrl
                        )
                    )
                }
                myAdapter.notifyDataSetChanged()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
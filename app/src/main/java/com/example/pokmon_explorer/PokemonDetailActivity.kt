package com.example.pokmon_explorer

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.pokmon_explorer.data.ApiClient
import com.example.pokmon_explorer.data.PokemonResponse
import kotlinx.coroutines.launch
import android.widget.Button
import android.text.Html

class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var imagePokemon: ImageView
    private lateinit var pokemonName: TextView
    private lateinit var hp: TextView
    private lateinit var attack: TextView
    private lateinit var defense: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pokemon_detail)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imagePokemon = findViewById(R.id.imagePokemon)
        pokemonName = findViewById(R.id.name)
        hp = findViewById(R.id.hp)
        attack = findViewById(R.id.attack)
        defense = findViewById(R.id.defense)

        val name = intent.getStringExtra("name")

        loadDetails(name ?: "")
        
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            finish()
        }
    }

    private fun loadDetails(name: String) {
        lifecycleScope.launch {

            try {
                val detail: PokemonResponse = ApiClient.api.getPokemonByName(name)
                pokemonName.text = detail.name.replaceFirstChar { it.uppercase() }

                val imageURL = detail.sprites.other.officialArtwork?.front_default
                imagePokemon.load(imageURL ?: R.drawable.outline_adb_24){
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_background)
                    error(R.drawable.ic_launcher_foreground)
                }

                val pokhp = detail.stats.find { it.stat.name == "hp" }?.base_stat ?:0
                val pokattack = detail.stats.find { it.stat.name == "attack" }?.base_stat ?:0
                val pokdefense = detail.stats.find { it.stat.name == "defense" }?.base_stat ?:0

                hp.text = Html.fromHtml("<b>Hp:</b> $pokhp")
                attack.text = Html.fromHtml("<b>Attack:</b> $pokattack")
                defense.text = Html.fromHtml("<b>Defense:</b> $pokdefense")
            } catch (e: Exception) {
                e.printStackTrace()

            }

        }
    }
}
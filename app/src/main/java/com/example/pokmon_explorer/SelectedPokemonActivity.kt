package com.example.pokmon_explorer

import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextWatcher
import androidx.lifecycle.lifecycleScope
import com.example.pokmon_explorer.data.ApiClient
import com.example.pokmon_explorer.data.extractedID
import kotlinx.coroutines.launch

class SelectedPokemonActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var title: TextView
    private lateinit var searchBox: EditText
    private lateinit var loadMoreBtn: Button

    private val fullList = mutableListOf<SimplePokemon>()

    private val shownList = mutableListOf<SimplePokemon>()
    private lateinit var adapter: PokemonAdapter

    private var nextIndx = 0
    private val limit = 10
    private var current: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_selected_pokemon)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        title = findViewById(R.id.textView)
        searchBox = findViewById(R.id.searchBox)
        recyclerView = findViewById(R.id.recyclerView)
        loadMoreBtn = findViewById(R.id.loadMoreBtn)

        val type = intent.getStringExtra("type_name")?: "unknown"
        title.text = "Type: $type"

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PokemonAdapter(shownList){tapped ->
            val i = android.content.Intent(this, PokemonDetailActivity::class.java)
            i.putExtra("name", tapped.name)
            startActivity(i)
        }

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            finish()
        }

        recyclerView.adapter = adapter

        load(type)

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                current = s?.toString()?.trim()?.lowercase() ?: ""
                applyFilter()
            }
        })
        loadMoreBtn.setOnClickListener{ showNextPage() }
    }

    private fun applyFilter(){
        shownList.clear()
        adapter.notifyDataSetChanged()
        nextIndx = 0
        showNextPage()
    }

    private fun showNextPage(){
        val source =  if (current.isBlank()) {
            fullList
        } else {
            fullList.filter { it.name.contains(current) }
        }

        val end= (nextIndx + limit ).coerceAtMost(source.size)
        if(nextIndx < end){
            shownList.addAll(source.subList(nextIndx, end))
            adapter.notifyItemRangeInserted(shownList.size - (end - nextIndx), end - nextIndx)
            nextIndx = end
        }

        loadMoreBtn.isEnabled = nextIndx < source.size
    }

    private fun load(type: String){
        lifecycleScope.launch {
            try {
                val response = ApiClient.api.getTypeByName(type)
                val mapped = response.pokemon.map {slot ->
                    val id = extractedID(slot.pokemon.url)
                    val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
                    SimplePokemon(slot.pokemon.name, url)
            }
                fullList.clear()
                fullList.addAll(mapped)

                shownList.clear()
                adapter.notifyDataSetChanged()
                nextIndx = 0
                showNextPage()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
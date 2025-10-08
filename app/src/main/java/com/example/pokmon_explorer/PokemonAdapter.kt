package com.example.pokmon_explorer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pokmon_explorer.data.SimplePokemon

class PokemonAdapter (
    private val data:  MutableList<SimplePokemon>,
    private val onTap: (SimplePokemon) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.Box>(){

        inner class Box(view: View) : RecyclerView.ViewHolder(view) {
            val row: CardView = view as CardView
            val name: TextView = view.findViewById(R.id.pokemonName)
            val image: ImageView = view.findViewById(R.id.pokemonImage)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Box {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
            return Box(view)
        }

        override fun onBindViewHolder(holder: Box, position: Int) {
            val currentItem = data[position]
            holder.name.text = currentItem.name
            holder.image.load(currentItem.imageURL){
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
            }
            holder.row.setOnClickListener{
                onTap(currentItem)
            }
        }

        override fun getItemCount() = data.size

}
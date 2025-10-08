package com.example.pokmon_explorer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pokmon_explorer.data.CardItem

class Adapter (
    private val data: List<CardItem>,
    private val onCardClick: (CardItem)-> Unit
): RecyclerView.Adapter<Adapter.CardViewHolder>(){

    inner class CardViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val card: CardView = view as CardView
        val titleText: TextView = view.findViewById(R.id.typeTitle)
        val picture: ImageView = view.findViewById(R.id.typeImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = data[position]
        holder.titleText.text = currentItem.title
        holder.picture.load(currentItem.imageUrl){
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_background)
        }

        holder.card.setOnClickListener {
            onCardClick(currentItem)
        }
    }

    override fun getItemCount() = data.size

}
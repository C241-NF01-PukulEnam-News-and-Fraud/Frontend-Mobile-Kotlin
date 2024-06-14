package com.example.pukul6.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pukul6.R

class HorizontalRecyclerView : RecyclerView.Adapter<HorizontalRecyclerView.MyViewHolder>() {

    private val newsTypes = listOf("All", "Finance", "Entertainment", "International", "Health", "Lifestyle", "National", "Sports", "Automotive", "Politics", "Politics", "Technology")
    private val selectedPositions = mutableSetOf<Int>() // Set untuk melacak posisi yang diklik

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rowbutton, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return newsTypes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.newsTypeTextView.text = newsTypes[position]

        // Set warna awal
        setDefaultColor(holder)

        // Set click listener
        holder.cardView.setOnClickListener {
            if (selectedPositions.contains(position)) {
                // Jika posisi sudah ada di set (diklik sebelumnya), kembalikan ke warna default
                selectedPositions.remove(position)
                setDefaultColor(holder)
            } else {
                // Jika posisi belum ada di set (belum diklik), ubah warna menjadi "diklik"
                selectedPositions.add(position)
                setClickedColor(holder)
            }
        }
    }

    private fun setDefaultColor(holder: MyViewHolder) {
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        holder.newsTypeTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
    }

    private fun setClickedColor(holder: MyViewHolder) {
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.clickedType))
        holder.newsTypeTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsTypeTextView: TextView = itemView.findViewById(R.id.tv_news_type)
        val cardView: CardView = itemView.findViewById(R.id.card_view)
    }
}
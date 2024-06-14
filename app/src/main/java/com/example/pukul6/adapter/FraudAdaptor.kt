package com.example.pukul6.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pukul6.R
import com.example.pukul6.data.database.ClassificationResult
import com.noowenz.showmoreless.ShowMoreLess

class FraudAdaptor(
    private val fraudItems: MutableList<ClassificationResult>,
    private val onDeleteClick: (ClassificationResult) -> Unit
) : RecyclerView.Adapter<FraudAdaptor.FraudViewHolder>() {

    fun removeItem(position: Int) {
        val fraudItem = fraudItems[position]
        fraudItems.removeAt(position)
        notifyItemRemoved(position)
        onDeleteClick(fraudItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FraudViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rowfraud, parent, false)
        return FraudViewHolder(view)
    }

    override fun onBindViewHolder(holder: FraudViewHolder, position: Int) {
        val fraudItem = fraudItems[position]
        holder.bind(fraudItem)
    }

    override fun getItemCount(): Int {
        return fraudItems.size
    }

    inner class FraudViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvProbability: TextView? = itemView.findViewById(R.id.tv_item_status)
        private val tvItemTitle: TextView? = itemView.findViewById(R.id.tv_item_title)
        private val tvItemList: TextView? = itemView.findViewById(R.id.listEnt)
        private val tvItemBias: TextView? = itemView.findViewById(R.id.textView3)
        private val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)

        init {
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    removeItem(position)
                }
            }
        }

        fun bind(fraudItem: ClassificationResult) {
            val probability = fraudItem.probability
            val title = fraudItem.inputText
            val bias = fraudItem.bias
            val listEnt = fraudItem.entities
            val label = if (probability > 0.5) "Hoax" else "Valid"

            val bulletPoint = "\u2022" // Unicode for bullet point
            val formattedEntities = listEnt.joinToString("\n") { "$bulletPoint $it" }

            tvItemList?.let {
                it.text = formattedEntities
            }

            tvItemBias?.let {
                it.text = bias.toString()
            }

            tvProbability?.let {
                it.text = label
                it.setBackgroundColor(if (label == "Hoax") Color.RED else Color.GREEN)
                it.setTextColor(Color.WHITE)
                it.setPadding(8, 4, 8, 4)
            }

            tvItemTitle?.let {
                ShowMoreLess.Builder(itemView.context)
                    .textLengthAndLengthType(100, ShowMoreLess.TYPE_CHARACTER)
                    .showMoreLabel("show more")
                    .showLessLabel("show less")
                    .showMoreLabelColor(Color.BLUE)
                    .showLessLabelColor(Color.BLUE)
                    .textClickable(true, true)
                    .build()
                    .addShowMoreLess(it, title, isContentExpanded = false)
            }
        }
    }
}

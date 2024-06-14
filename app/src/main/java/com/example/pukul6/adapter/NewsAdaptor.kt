package com.example.pukul6.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pukul6.R
import com.example.pukul6.data.response.Post
import com.example.pukul6.ui.NewsDetailActivity
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

class NewsAdaptor : PagingDataAdapter<Post, NewsAdaptor.MyViewHolder>(POST_COMPARATOR) {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tv_item_name)
        val content: TextView = view.findViewById(R.id.tv_item_info)
        val image: ImageView = view.findViewById(R.id.img_item_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rownews, parent, false)
        Log.d("NewsAdaptor", "ViewHolder created")
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = getItem(position)
        if (post != null) {
            Log.d("NewsAdaptor", "Binding post at position $position: ${post.title.rendered}")
            holder.title.text = post.title.rendered

            // Format date
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val date: Date = sdf.parse(post.date)!!
            val prettyTime = PrettyTime(Locale.getDefault())
            val timeAgo = prettyTime.format(date)

            holder.content.text = "${post.yoast_head_json.author} • $timeAgo"

            // Check for non-null yoast_head_json and og_image
            val imageUrl = post.yoast_head_json?.og_image?.firstOrNull()?.url
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(holder.itemView.context)
                    .load(imageUrl)
                    .into(holder.image)
            } else {
                holder.image.setImageResource(R.drawable.ic_launcher_background)  // Set a placeholder image
            }

            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, NewsDetailActivity::class.java)
                intent.putExtra("post", post)
                context.startActivity(intent)
            }
        } else {
            Log.d("NewsAdaptor", "Post is null at position $position")
        }
    }

    companion object {
        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem == newItem
        }
    }
}

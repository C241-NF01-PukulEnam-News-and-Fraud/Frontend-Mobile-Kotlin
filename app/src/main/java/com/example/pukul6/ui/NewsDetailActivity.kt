package com.example.pukul6.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pukul6.R
import com.example.pukul6.data.response.Post
import com.example.pukul6.databinding.ActivityNewsDetailBinding
import com.parassidhu.simpledate.toDateStandardConcise
import net.nightwhistler.htmlspanner.HtmlSpanner
import java.text.SimpleDateFormat
import java.util.Locale

class NewsDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val htmlSpanner = HtmlSpanner()

        val post = intent.getParcelableExtra<Post>("post")
        post?.let {
            binding.title.text = it.title.rendered
            binding.author.text = it.yoast_head_json.author

            Log.d("html",it.content.rendered)
            val spannedHtml = htmlSpanner.fromHtml(it.content.rendered)
            binding.content.text = spannedHtml

            val dateString = it.date // Misalnya "2024-06-12T06:00:00"
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = dateFormat.parse(dateString)

            binding.date.text = date.toDateStandardConcise()

            val imageUrl = post.yoast_head_json?.og_image?.firstOrNull()?.url
            if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(it.yoast_head_json.og_image.first().url)
                .into(binding.imageView)
            }else{
                binding.imageView.setImageResource(R.drawable.ic_launcher_background)
            }
        }
    }
}
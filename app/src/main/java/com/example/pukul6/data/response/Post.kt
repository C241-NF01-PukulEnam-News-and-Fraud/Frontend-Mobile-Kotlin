package com.example.pukul6.data.response
import android.os.Parcelable
import kotlinx.parcelize.Parcelize




@Parcelize
data class Post(
    val id: Int,
    val title: Title,
    val content: Content,
    val categories: List<Int>,
    val author: Int,
    val date : String,
    val yoast_head_json: YoastHeadJson
) : Parcelable

@Parcelize
data class Title(
    val rendered: String
) : Parcelable

@Parcelize
data class YoastHeadJson(
    val og_image: List<OgImage>,
    val author: String
) : Parcelable
@Parcelize
data class Content(
    val rendered: String
) : Parcelable


@Parcelize
data class OgImage(
    val url: String,
    val width: Int,
    val height: Int,
    val type: String  // Optional, if you need to use the image type somewhere in the app
) : Parcelable
data class Response(
    val result: List<Float>,
    val entities: List<String>,
    val bias: List<String>
)

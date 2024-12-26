package com.mobile_computing.halifaxexplore.blogs

import java.io.Serializable

data class Blog(
    val title: String,
    val description: String,
    val detail: String,
    val date_published: String,
    val author: String,
    val image: String,
    val url: String
) : Serializable

data class BlogResponse(val blogs: List<Blog>)
package com.example.snapsphere.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostData(
    val postId: String? = null,
    val userId: String? = null,
    val username: String? = null,
    val userImage: String? = null,
    val postImage: String? = null,
    val postDescription: String? = null,
    val time: Long? = null,
    val likes: List<String>? = null
): Parcelable
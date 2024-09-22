package com.example.snapsphere.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// user data to store in firebase database
// also setting every property of this class null as firebase requires empty constructor while creating an account
@Parcelize
data class UserData(
    var userId: String? = null,
    var name: String? = null,
    var username: String? = null,
    var imageUrl: String? = null,
    var bio: String? = null,
    var following: List<String>? = null
): Parcelable {
    // converting to map because firebase takes data in form of map
    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "username" to username,
        "imageUrl" to imageUrl,
        "bio" to bio,
        "following" to following
    )
}

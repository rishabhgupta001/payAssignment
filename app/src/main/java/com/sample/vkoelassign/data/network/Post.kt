package com.sample.vkoelassign.data.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    var postId: String = "",
    var postImage: String = "",
    var postDescription: String = "",
    var postPublisher: String = "",
    var screenTitle: String = ""
) : Parcelable

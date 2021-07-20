package com.sample.vkoelassign.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comment(
    var comment: String = "",
    var publisher: String = "",

): Parcelable

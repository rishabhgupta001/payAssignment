package com.sample.vkoelassign.data.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *
 * Purpose – Pojo class Comment
 *
 * @author Rishabh Gupta
 *
 * */
@Parcelize
data class Comment(
    var comment: String = "",
    var publisher: String = "",

): Parcelable

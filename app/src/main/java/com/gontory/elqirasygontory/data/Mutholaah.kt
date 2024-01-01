package com.gontory.elqirasygontory.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mutholaah(
    @DocumentId
    val urutan: Int,
    val img_url: String,
    val title: String,
    val video_url: String
) : Parcelable {
    constructor(): this(0, "", "", "")
}
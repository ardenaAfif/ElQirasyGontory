package com.gontory.elqirasygontory.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mutholaah(
    @DocumentId
    val mutholaahId: String,
    val urutan: Int,
    val img_url: String,
    val title: String,
    val video_url: String,
    val questions: Long
) : Parcelable {
    constructor() : this("", 0, "", "", "", 0)
}
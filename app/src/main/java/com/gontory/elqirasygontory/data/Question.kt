package com.gontory.elqirasygontory.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    @DocumentId
    val question: String,
    val answer: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val time: Long,
) : Parcelable {
    constructor(): this("","", "", "", "", "", 0L)
}

package com.gontory.elqirasygontory.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rating (
    val rating: Long,
    val message: String
) : Parcelable
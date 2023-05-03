package com.asifaltaf.movies.domain.model


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("Source") @ColumnInfo(defaultValue = "") val source: String? = "",
    @SerializedName("Value") @ColumnInfo(defaultValue = "") val value: String? = ""
)
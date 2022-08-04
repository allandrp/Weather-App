package com.allandrp.weatherapplication.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "weather")
@Parcelize
data class Weather(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Int = 0,

    @ColumnInfo
    val location: String,

    @ColumnInfo
    val desc: String,

    @ColumnInfo
    val iconCode: String,

    @ColumnInfo
    val temperature: Float,

    @ColumnInfo
    val lon: Float,

    @ColumnInfo
    val lat: Float,

): Parcelable
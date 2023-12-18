package com.example.baskaryaapp.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

//Dummy apabila local
@Entity(tableName = "BookmarkBatik")
@Parcelize
data class BookmarkBatik(
    @PrimaryKey
    var id: Int = 0,
    var title: String = "",
    var imageUrl: String? = null
) : Parcelable
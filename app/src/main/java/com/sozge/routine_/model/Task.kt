package com.sozge.routine_.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(

    @ColumnInfo(name= "new")
    var task : String,

    @ColumnInfo(name = "aciklama")
    var desc : String

) {
    @PrimaryKey(autoGenerate = true)
    var id= 0
}
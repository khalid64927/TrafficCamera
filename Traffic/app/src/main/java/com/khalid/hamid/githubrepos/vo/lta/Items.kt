package com.khalid.hamid.githubrepos.vo.lta

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class Items (
		@Expose @SerializedName("timestamp") val timestamp : String,
		@Expose @SerializedName("cameras") val cameras : List<Cameras>
)
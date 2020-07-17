package com.khalid.hamid.githubrepos.vo.lta

import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class Image_metadata (
		@Expose @SerializedName("height") val height : Int,
		@Expose @SerializedName("width") val width : Int,
		@Expose @SerializedName("md5") val md5 : String
)
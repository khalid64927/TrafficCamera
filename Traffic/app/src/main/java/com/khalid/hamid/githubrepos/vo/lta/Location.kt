package com.khalid.hamid.githubrepos.vo.lta

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class Location (
		@Expose @SerializedName("latitude") val latitude : Double,
		@Expose @SerializedName("longitude") val longitude : Double
)
package com.khalid.hamid.githubrepos.vo.lta

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class GetTrafficResponse (
	@Expose @SerializedName("items") val items : List<Items>,
	@Expose @SerializedName("api_info") val api_info : Api_info
)
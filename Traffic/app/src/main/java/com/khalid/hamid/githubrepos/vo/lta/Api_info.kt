package com.khalid.hamid.githubrepos.vo.lta

import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class Api_info (
	@SerializedName ("status") @Expose val status : String

)
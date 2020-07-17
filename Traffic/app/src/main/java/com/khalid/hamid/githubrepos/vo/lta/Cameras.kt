package com.khalid.hamid.githubrepos.vo.lta

import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class Cameras(
    @SerializedName("timestamp") @Expose val timestamp : String,
    @SerializedName("image") @Expose val image : String,
    @SerializedName("location") @Expose val location : Location,
    @SerializedName("camera_id") @Expose val camera_id : Int,
    @SerializedName("image_metadata") @Expose val image_metadata : Image_metadata
)
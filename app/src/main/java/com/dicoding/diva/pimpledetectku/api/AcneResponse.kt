package com.dicoding.diva.pimpledetectku.api

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class Response(
	@field:SerializedName("token")
	val token : String,
	@field:SerializedName("name")
	val name : String

)


data class ResponseLogin(
	@field:SerializedName("success")
	val success: Boolean,
	@field:SerializedName("data")
	val data: Response,
	@field:SerializedName("message")
	val message: String
)

data class GetAcneList(
	@field:SerializedName("success")
	val success: Boolean,
	@field:SerializedName("data")
	val data: ArrayList<AcneItems>,
	@field:SerializedName("message")
	val message: String
)

@Parcelize
data class AcneItems(
	@field:SerializedName("id")
	val id: Int,
	@field:SerializedName("name")
	val name: String,
	@field:SerializedName("description")
	val description: String,
	@field:SerializedName("cause")
	val cause: String,
	@field:SerializedName("solution")
	val solution: String,
	@field:SerializedName("created_at")
	val created_at: String,
	@field:SerializedName("updated_at")
	val updated_at: String
) : Parcelable


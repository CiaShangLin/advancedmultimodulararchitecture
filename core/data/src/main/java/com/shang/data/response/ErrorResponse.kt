package com.shang.data.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
  @SerializedName("errorCode")
  val errorCode: Int,
  @SerializedName("errorMessage")
  val errorMessage: String,
  @SerializedName("errorFieldList")
  val errorFieldList: List<String>,
)

package com.shang.login.data.response

import com.google.gson.annotations.SerializedName

class LoginResponse(
  @SerializedName("id")
  val id: String,
  @SerializedName("fullName")
  val fullName: String,
  @SerializedName("email")
  val email: String,
  @SerializedName("photo")
  val photo: String,
)

package com.example.application.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    val userId:String? = "",
    val userName:String? = "",
    val profileImage:String? = ""
)
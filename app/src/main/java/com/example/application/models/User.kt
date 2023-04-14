package com.example.application.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User(val userId: String, val username: String, val profileImageUrl: String): Parcelable
{
    constructor(): this("", "","")
}

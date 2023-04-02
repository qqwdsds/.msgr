package com.example.application.models

import android.net.Uri

class User(val userId: String, val username: String, val profileImageUrl: String){
    constructor(): this("", "","")
}

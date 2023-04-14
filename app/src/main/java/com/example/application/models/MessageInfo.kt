package com.example.application.models

data class MessageInfo(val id: String, val text: String, val time: Long, val toId: String, val fromId: String){
    constructor():this("", "", -1, "", "")
}
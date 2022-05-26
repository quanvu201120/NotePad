package com.quanvu201120.notepad.model

import java.io.Serializable

class User(var userId : String = "", var userNote : ArrayList<String>? = null) : Serializable{
    override fun toString(): String {
        return "User(userId='$userId', userNote=$userNote)"
    }
}
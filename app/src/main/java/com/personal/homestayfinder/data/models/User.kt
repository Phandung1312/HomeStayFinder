package com.personal.homestayfinder.data.models

import com.google.firebase.auth.FirebaseUser

data class User(
    val userId : String? = null,
    val gmail : String? = null,
    val name : String? = null,
    val imgUrl : String? = null,
    val online : Boolean? = false
)
fun FirebaseUser.toUser() : User{
    return User(
        userId = this.uid,
        gmail = this.email,
        name = this.displayName,
        imgUrl = this.photoUrl.toString()
    )
}

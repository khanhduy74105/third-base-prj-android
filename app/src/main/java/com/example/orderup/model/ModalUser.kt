package com.example.orderup.model

class ModalUser {
    var uid:String = ""
    var email:String = ""
    var username:String = ""
    var timestamp:Long = 0
    var likes:Long? = 0
    var profileImage:String = ""
    var userType:String = ""
    var address:String = ""
    constructor()
    constructor(
        uid: String,
        email: String,
        username: String,
        timestamp: Long,
        likes: Long,
        profileImage: String,
        userType: String,
        address: String,
    ) {
        this.uid = uid
        this.email = email
        this.username = username
        this.timestamp = timestamp
        this.likes = likes
        this.profileImage = profileImage
        this.userType = userType
        this.address = address
    }


}
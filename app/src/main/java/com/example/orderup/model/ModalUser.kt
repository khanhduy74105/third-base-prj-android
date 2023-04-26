package com.example.orderup.model

class ModalUser {
    var uid:String = ""
    var email:String = ""
    var username:String = ""
    var timestamp:Long = 0
    var profileImage:String = ""
    var userType:String = ""
    var address:String = ""
    var provider:String? = ""
    constructor()
    constructor(
        uid: String,
        email: String,
        username: String,
        timestamp: Long,
        profileImage: String,
        userType: String,
        address: String,
        provider: String?
    ) {
        this.uid = uid
        this.email = email
        this.username = username
        this.timestamp = timestamp
        this.profileImage = profileImage
        this.userType = userType
        this.address = address
        this.provider = provider
    }


}
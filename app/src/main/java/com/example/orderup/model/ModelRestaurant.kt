package com.example.orderup.model

class ModelRestaurant {
    var id:String = ""
    var timestamp:Long = 0
    var uid:String = ""
    var name:String = ""
    var image:String=""
    var address:String = ""
    var likes : Int=0
    constructor()
    constructor(id: String, name: String,address: String,image: String, timestamp: Long, uid: String,likes: Int ) {
        this.id = id
        this.timestamp = timestamp
        this.uid = uid
        this.name = name
        this.image = image
        this.address = address
        this.likes = likes
    }


}
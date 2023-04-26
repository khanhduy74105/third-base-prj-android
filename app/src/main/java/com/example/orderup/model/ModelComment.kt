package com.example.orderup.model

class ModelComment {
    var id:String = ""
    var uid:String = ""
    var timestamp:Long = 0
    var content: String= ""
    var rating: Long = 0

    constructor()
    constructor(id: String, uid: String, timestamp: Long, content: String, rating: Long) {
        this.id = id
        this.uid = uid
        this.timestamp = timestamp
        this.content = content
        this.rating = rating
    }


}
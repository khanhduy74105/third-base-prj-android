package com.example.orderup.model

class ModelFood {
    var id:String = ""
    var foodname:String = ""
    var price: String= ""
    var description: String= ""
    var category: String= ""
    var timestamp:Long = 0
    var imageUrl: String = ""
    var buyCount:Long = 0
    var numberOfLike:Int = 0
    var uid: String =""
    constructor()
    constructor(
        id: String,
        foodname: String,
        price: String,
        description: String,
        category: String,
        timestamp: Long,
        imageUrl: String,
        buyCount: Long,
        numberOfLike: Int,
        uid: String
    ) {
        this.id = id
        this.foodname = foodname
        this.price = price
        this.description = description
        this.category = category
        this.timestamp = timestamp
        this.imageUrl = imageUrl
        this.buyCount = buyCount
        this.numberOfLike = numberOfLike
        this.uid = uid
    }


}
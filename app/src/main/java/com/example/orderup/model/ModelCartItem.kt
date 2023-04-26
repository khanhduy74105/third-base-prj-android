package com.example.orderup.model

class ModelCartItem {
    var id:String = ""
    var foodname:String = ""
    var foodid:Long = 0
    var amount:Int = 0
    var timestamp:Long = 0
    var imageUrl:String = ""
    var price:String = ""
    constructor()
    constructor(id: String, foodname: String, foodid: Long, amount: Int, timestamp: Long,imageUrl:String,price:String) {
        this.id = id
        this.foodname = foodname
        this.foodid = foodid
        this.amount = amount
        this.timestamp = timestamp
        this.imageUrl = imageUrl
        this.price =price
    }

}
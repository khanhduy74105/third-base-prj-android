package com.example.orderup.model

class ModelFood {
    var id:String = ""
    var foodName:String = ""
    var price: Long= 0
    var description: String= ""
    var categoryId: String= ""
    var timestamp:Long = 0
    var url: String = ""
    var viewCount:Long = 0
    constructor()
    constructor(
        id: String,
        foodName: String,
        price: Long,
        description: String,
        categoryId: String,
        timestamp: Long,
        url: String,
        viewCount: Long
    ) {
        this.id = id
        this.foodName = foodName
        this.price = price
        this.description = description
        this.categoryId = categoryId
        this.timestamp = timestamp
        this.url = url
        this.viewCount = viewCount
    }


}
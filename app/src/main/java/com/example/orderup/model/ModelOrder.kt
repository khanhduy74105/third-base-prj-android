package com.example.orderup.model

class ModelOrder {
    var uid: String = ""
    var id:String = ""
    var items: ArrayList<ModelCartItem> = ArrayList()
    var total: Long = 0
    var timestamp:Long = 0
    var state: String = ""
    var address: String = ""
    constructor()
    constructor(
        uid: String,
        id: String,
        items: ArrayList<ModelCartItem>,
        total: Long,
        timestamp: Long,
        state: String,
        address: String
    ) {
        this.uid = uid
        this.id = id
        this.items = items
        this.total = total
        this.timestamp = timestamp
        this.state = state
        this.address = address
    }

}
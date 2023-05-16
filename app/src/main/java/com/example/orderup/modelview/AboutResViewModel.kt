package com.example.orderup.modelview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.orderup.model.ModelCartItem

class AboutResViewModel: ViewModel {
    private var _cartItemArraylist: MutableLiveData<ArrayList<ModelCartItem>> =
        MutableLiveData<ArrayList<ModelCartItem>>()
    val cartItemArraylist: LiveData<ArrayList<ModelCartItem>>
        get() = _cartItemArraylist

    init {

    }

    constructor() {
    }
}
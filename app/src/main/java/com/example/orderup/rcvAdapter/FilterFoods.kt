package com.example.orderup.rcvAdapter

import android.widget.Filter
import com.example.orderup.model.ModelCategory
import com.example.orderup.model.ModelFood

class FilterFoods : Filter {
    private var filterList: ArrayList<ModelFood>
    private var foodAdapter: FoodsAdapter

    constructor(filterList: ArrayList<ModelFood>, foodAdapter: FoodsAdapter) : super() {
        this.filterList = filterList
        this.foodAdapter = foodAdapter
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()
        if (constraint != null && constraint.isNotEmpty()) {
            constraint = constraint.toString().uppercase()
            var filteredModel: ArrayList<ModelFood> = ArrayList()
            for (i in 0 until filterList.size) {
                if (filterList[i].foodname.uppercase()
                        .contains(constraint) || filterList[i].category.uppercase()
                        .contains(constraint)
                ) {
                    filteredModel.add(filterList[i])
                }
            }
            results.count = filteredModel.size
            results.values = filteredModel
        } else {
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        foodAdapter.foodsArraylist = results.values as ArrayList<ModelFood>
        foodAdapter.notifyDataSetChanged()
    }
}
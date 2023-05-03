package com.example.orderup.restaurantdashboard

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import com.example.orderup.R
import com.example.orderup.databinding.ActivityChartForMoneyBinding
import com.example.orderup.model.ModelOrder
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class ChartForMoney : AppCompatActivity() {
    private  lateinit var binding: ActivityChartForMoneyBinding
    private lateinit var barChart : BarChart
    private lateinit var btnBack : ImageButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var usersUid: ArrayList<String>
    private lateinit var listData: ArrayList<BarEntry>
    val listMoneyMonth = arrayOf(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartForMoneyBinding.inflate(layoutInflater)
        barChart = binding.barChart
        btnBack = binding.backBtn
        usersUid = ArrayList()
        listData = ArrayList()
        getUser(listMoneyMonth)
        for(i in 1 .. 12){
            listData.add(BarEntry( i.toFloat() ,1000f))
        }
        val barDataset = BarDataSet(listData,"list")
        barDataset.setColors(ColorTemplate.MATERIAL_COLORS,255)
        barDataset.valueTextColor = Color.BLACK
        val barData = BarData(barDataset)
        barChart.setFitBars(true)
        barChart.data = barData
        btnBack.setOnClickListener {
            startActivity(Intent(this,RestaurantDashboardActivity::class.java))
        }
        setContentView(binding.root)
    }

    private fun changeTimestampToMonth(timestamp:Long):Int{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        val month = calendar.get(Calendar.MONTH)
        return month
    }
    fun getUser(moneys: Array<Long>){
        usersUid =ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    val model = ds.child("uid").value.toString()
                    usersUid.add(model)
                }
                getData(usersUid,moneys)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    fun getData(usersUid:ArrayList<String>,moneys: Array<Long>) {
        for (i in 1.. usersUid.size) {
            val ref = FirebaseDatabase.getInstance().getReference("Orders/${usersUid[i-1]}")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        val model = ds.getValue(ModelOrder::class.java)
                        if (model != null) {
                            if(model.state=="confirmed"){
                                var month = changeTimestampToMonth(model.timestamp)
                                moneys[month] += model.total
                            }
                        }
                    }
                    updateChart()

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
    fun updateChart(){
        listData.clear()
        // Rebuild the listData array with updated values from the listMoneyMonth array
        for(i in 1 .. 12){
            listData.add(BarEntry( i.toFloat() ,listMoneyMonth[i-1].toFloat()))
        }
        // Update the chart
        val barDataset = BarDataSet(listData,"list")
        barDataset.setColors(ColorTemplate.MATERIAL_COLORS,255)
        barDataset.valueTextColor = Color.BLACK
        val barData = BarData(barDataset)
        barChart.setFitBars(true)
        barChart.data = barData
    }
}
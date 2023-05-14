package com.example.orderup.restaurantdashboard

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.orderup.databinding.ActivityChartForMoneyBinding
import com.example.orderup.model.ModelOrder
import com.example.orderup.modelview.ChartForMoneyViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class ChartForMoney : AppCompatActivity() {
    private  lateinit var binding: ActivityChartForMoneyBinding
    private lateinit var barChart : BarChart
    private lateinit var btnBack : ImageButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var usersUid: ArrayList<String>

    private lateinit var moneyViewModel: ChartForMoneyViewModel
    var listMoneyMonth = arrayOf(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartForMoneyBinding.inflate(layoutInflater)
        barChart = binding.barChart
        btnBack = binding.backBtn
        usersUid = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val model = ds.child("uid").value.toString()
                    usersUid.add(model)
                }
                for (i in 1..usersUid.size) {
                    val ref =
                        FirebaseDatabase.getInstance().getReference("Orders/${usersUid[i - 1]}")
                    ref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val listData = mutableListOf<BarEntry>()
                            for (ds in snapshot.children) {
                                val model = ds.getValue(ModelOrder::class.java)
                                if (model != null) {
                                    if(model.state=="confirmed"){
                                        var month = changeTimestampToMonth(model.timestamp)
                                       listMoneyMonth[month] += model.total
                                    }
                                }
                            }
                            for(i in 1 .. 12){
                                Log.i("money",listMoneyMonth[i-1].toString())
                                listData.add(BarEntry( i.toFloat() ,listMoneyMonth[i-1].toFloat()))
                            }
                            val barDataset = BarDataSet(listData,"list")
                            barDataset.setColors(ColorTemplate.MATERIAL_COLORS,255)
                            barDataset.valueTextColor = Color.BLACK
                            val barData = BarData(barDataset)
                            barChart.setFitBars(true)
                            barChart.data = barData
                            barChart.invalidate()
                        }
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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
}
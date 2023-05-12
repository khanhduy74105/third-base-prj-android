package com.example.orderup.restaurantdashboard

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import com.example.orderup.R
import com.example.orderup.databinding.ActivityChartForMoneyBinding
import com.example.orderup.databinding.ActivityChartForOrderBinding
import com.example.orderup.model.ModelOrder
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
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

class ChartForOrder : AppCompatActivity() {
    private  lateinit var binding: ActivityChartForOrderBinding
    private lateinit var pieChart : PieChart
    private lateinit var btnBack : ImageButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var usersUid: ArrayList<String>
    private lateinit var listData: ArrayList<PieEntry>
    var confirmOrder = 0
    var cancelOrder = 0
    var waitingOrder = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityChartForOrderBinding.inflate(layoutInflater)
        pieChart= binding.pieChart
        btnBack = binding.backBtn
        usersUid = ArrayList()
        listData = ArrayList()
        getUser()
        listData.add(PieEntry( 100f,"confirm" ))
        listData.add(PieEntry(  100f,"cancel" ))
        listData.add(PieEntry(  100f,"waiting" ))
        val pieDataSet = PieDataSet(listData,"list")
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS,255)
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize=30f
        val pieData = PieData(pieDataSet)
        pieChart.centerText="Number of Order"
        pieChart.description.isEnabled=true
        pieChart.animate()

        pieChart.data = pieData
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
    fun getUser(){
        usersUid =ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    val model = ds.child("uid").value.toString()
                    usersUid.add(model)
                }
                getData(usersUid)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    fun getData(usersUid:ArrayList<String>) {
        for (i in 1.. usersUid.size) {
            val ref = FirebaseDatabase.getInstance().getReference("Orders/${usersUid[i-1]}")
            ref.addValueEventListener(object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        val model = ds.getValue(ModelOrder::class.java)
                        if (model != null) {
                            val currentMonthNumber: Int = LocalDate.now().monthValue -2
                            Log.i("confirmCurrentMonth",currentMonthNumber.toString())
                            val month = changeTimestampToMonth(model.timestamp)
                            Log.i("confirmMonth",month.toString())
                            if(month == currentMonthNumber){
                                if(model.state=="confirmed"){
                                    confirmOrder+=1
                                } else if(model.state =="waiting"){
                                    waitingOrder+=1
                                }else if (model.state=="cancel"){
                                    cancelOrder+=1
                                }
                            }
                           Log.i("confirm",confirmOrder.toString())
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
        listData = ArrayList()
        listData.add(PieEntry( confirmOrder.toFloat(),"confirm" ))
        listData.add(PieEntry(  cancelOrder.toFloat(),"cancel" ))
        listData.add(PieEntry(  waitingOrder.toFloat(),"waiting" ))
        val pieDataSet = PieDataSet(listData,"list")
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS,255)
        pieDataSet.valueTextColor = Color.BLACK
        val pieData = PieData(pieDataSet)
        pieChart.centerText="Number of Order"
        pieChart.animate()
        pieChart.data = pieData
    }
}


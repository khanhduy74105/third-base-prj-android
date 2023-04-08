  package com.example.orderup

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.orderup.databinding.ActivityFoodDetailBinding
import com.example.orderup.model.ModelFood
import com.example.orderup.rcvAdapter.foodsList

  class FoodDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodDetailBinding
    private lateinit var foodId:String

      @SuppressLint("SuspiciousIndentation")
      override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
          setContentView(binding.root)

          val intent = intent
          foodId = intent.getStringExtra("foodId")!!

          binding.backBtn.setOnClickListener {
              onBackPressed()
          }
          setUI()

      }

      private fun setUI() {
          val Food:ModelFood = foodsList.find<ModelFood> {
              it.id == foodId
          }!!

          binding.foodNameTv.text = Food.foodName
          binding.descriptionTv.text = Food.description
          binding.priceTv.text = Food.price.toString()
      }


  }
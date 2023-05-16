package com.example.orderup.usedashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.orderup.R
import com.example.orderup.databinding.ActivityRestaurantDetailBinding
import com.example.orderup.lib.tool
import com.example.orderup.model.ModalUser
import com.example.orderup.model.ModelComment
import com.example.orderup.model.ModelFood
import com.example.orderup.modelview.CommentViewModel
import com.example.orderup.modelview.FavoriteViewModel
import com.example.orderup.rcvAdapter.FoodsAdapter
import com.example.orderup.rcvAdapter.MyAdapterPageSlider
import com.google.firebase.database.*
import java.util.*

class RestaurantDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantDetailBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var restaurantId: String
    private var currentUserId: String = ""
    private lateinit var model: ModalUser
    private lateinit var foodsArraylist: ArrayList<ModelFood>
    private lateinit var adapterFood: FoodsAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
        sliderShow()
        setContentView(binding.root)

        val intent = intent
        restaurantId = intent.getStringExtra("restaurantId")!!
        currentUserId = tool.getCurrentId()
        loadDetail()
        loadFoods()
        favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.favBtn.setOnClickListener {
            if (currentUserId != "") {
                changeFavState(it)
            } else {
                Toast.makeText(this, "You need login first!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun changeFavState(it: View?) {
        val uid = tool.getCurrentId()
        if (uid != "") {
            it?.isSelected = !it?.isSelected!!
            if (it.isSelected) {
                val ref = FirebaseDatabase.getInstance().getReference("Favorites")
                ref.child("$uid/restaurants/$restaurantId")
                    .setValue(true)
                    .addOnSuccessListener {
                        val ref1 =
                            FirebaseDatabase.getInstance().getReference("Users/$restaurantId")
                        ref1.child("likes")
                            .setValue(ServerValue.increment(1))
                        Toast.makeText(this, "Add to favorites succeed!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {

                        Toast.makeText(this, "Add to favorites failed!", Toast.LENGTH_SHORT).show()
                    }
            } else {
                val ref = FirebaseDatabase.getInstance().getReference("Favorites")
                ref.child("$uid/restaurants/$restaurantId")
                    .removeValue()
                    .addOnSuccessListener {
                        val ref1 =
                            FirebaseDatabase.getInstance().getReference("Users/$restaurantId")
                        ref1.child("likes")
                            .setValue(ServerValue.increment(-1))
                        Toast.makeText(this, "Remove out favorites succeed!", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
            favoriteViewModel.loadRestaurants()

        }
    }


    fun sliderShow() {
        viewPager = binding.viewPager
        val images = listOf(
            R.drawable.item_slider1,
            R.drawable.item_slider2,
            R.drawable.item_slider3,
        )
        viewPager.adapter = MyAdapterPageSlider(images)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.offscreenPageLimit = 1

    }

    private fun loadDetail() {
        val ref = FirebaseDatabase.getInstance().getReference("Users/$restaurantId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                model = snapshot.getValue(ModalUser::class.java)!!
                binding.resNameTv.text = model.username.capitalize()
                binding.descriptionTv.text = "Description for this restaurant"
                binding.addressTv.text = "Address: ${model.address}"
                binding.countFavTv.text = "${model.likes}"
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        val uid = tool.getCurrentId()
        if (uid != "") {
            val refFav = FirebaseDatabase.getInstance().getReference("Favorites")
            refFav.child("$uid/restaurants")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (ds in snapshot.children) {
                            if (ds.key.toString() == restaurantId && ds.value == true) {
                                binding.favBtn.isSelected = true
                                break;
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }
    }

    fun loadFoods() {
        foodsArraylist = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Foods")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foodsArraylist.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue(ModelFood::class.java)
                    foodsArraylist.add(model!!)

                }
                adapterFood = FoodsAdapter(this@RestaurantDetailActivity, foodsArraylist)
                binding.foodsRcv.adapter = adapterFood
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}
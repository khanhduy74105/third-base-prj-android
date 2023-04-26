package com.example.orderup.usedashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.orderup.R
import com.example.orderup.databinding.ActivityCommentBinding
import com.example.orderup.lib.tool
import com.example.orderup.model.ModelFood
import com.example.orderup.modelview.CommentViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentBinding
    private lateinit var commentViewModel: CommentViewModel
    var ratingValue: Float = 0F
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val foodId: String? = intent.getStringExtra("foodId")
        if (foodId == "" || foodId == null){
            onBackPressed()
            return
        }
        commentViewModel = ViewModelProvider(this)[CommentViewModel::class.java]
        loadFoodInfo(foodId)

        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            ratingValue = rating
            ratingBar.rating = rating
            Log.i("CM", rating.toString())
            if (rating >= 0 && rating <= 1) binding.ratingCount.text = "Terible!"
            if (rating > 1 && rating <= 2) binding.ratingCount.text = "Quite bad!"
            if (rating > 2 && rating <= 3) binding.ratingCount.text = "Normal!"
            if (rating > 3 && rating <= 4) binding.ratingCount.text = "Good!"
            if (rating > 4 && rating <= 5) binding.ratingCount.text = "Amazing!"
        }

        binding.submitBtn.setOnClickListener {
            if (binding.editText.text.isEmpty()) {
                Toast.makeText(this, "Fill your comment!", Toast.LENGTH_SHORT).show()
            } else {
                onSubmit(foodId!!)
            }
        }
    }

    private fun onSubmit(foodId: String) {
        val uid:String = tool.getCurrentId()
        val timestamp = System.currentTimeMillis()
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["id"] = timestamp.toString()
        hashMap["content"] = binding.editText.text.toString()
        hashMap["rating"] = binding.ratingBar.rating
        hashMap["timestamp"] = timestamp
        val ref = FirebaseDatabase.getInstance().getReference("Comments/$foodId")
        ref.child(uid).setValue(hashMap).addOnSuccessListener {
            commentViewModel.loadComment(foodId)
            Toast.makeText(this, "Your comment is submitted!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadFoodInfo(foodId: String?) {
        val ref = FirebaseDatabase.getInstance().getReference("Foods")
        ref.child(foodId!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val modal = snapshot.getValue(ModelFood::class.java)!!
                binding.foodNameTv.text = modal.foodname
                val imgUri = modal.imageUrl.toUri().buildUpon().scheme("https").build()
                Glide.with(binding.foodIv.context).load(imgUri).centerCrop().apply(
                    RequestOptions().placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                ).into(binding.foodIv)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}
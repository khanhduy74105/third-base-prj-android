package com.example.orderup.rcvAdapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.orderup.FoodDetailActivity
import com.example.orderup.R
import com.example.orderup.UserSearchActivity
import com.example.orderup.databinding.RcvCategoryItemColBinding
import com.example.orderup.model.ModelCategory
import kotlin.math.nextDown
import kotlin.math.roundToInt

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private lateinit var binding:RcvCategoryItemColBinding

    private var context: Context
    public var categopryArraylist: ArrayList<ModelCategory>

    constructor(context: Context, categopryArraylist: ArrayList<ModelCategory>) {
        this.context = context
        this.categopryArraylist = categopryArraylist
    }

    inner class CategoryHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var categoryTv:TextView = binding.categoryTv
        var categoryIv: ImageView = binding.categoryIv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        binding = RcvCategoryItemColBinding.inflate(LayoutInflater.from(context), parent, false)

        return CategoryHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val model = categopryArraylist[position]
        val categoryType = model.category
        holder.categoryTv.text = categoryType

        val imageUrl=randomCategoryImg()
        val imgUri = imageUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(holder.categoryIv.context)
            .load(imgUri)
            .centerCrop()
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            .into(holder.categoryIv)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, UserSearchActivity::class.java)
            intent.putExtra("searchValue", categoryType)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categopryArraylist.size
    }

    fun randomCategoryImg(): String{
        val listImg = listOf<String>(
            "https://firebasestorage.googleapis.com/v0/b/order-up-a7a83.appspot.com/o/foods%2F1681003563686.jpg?alt=media&token=3826d92c-c953-4d7f-bbf8-09044a6cc115",
            "https://images.freeimages.com/images/large-previews/e79/california-hash-browns-1641651.jpg",
            "https://images.freeimages.com/images/previews/fa3/vegetarian-quesadilla-1639867.jpg",
            "https://images.freeimages.com/images/large-previews/783/sweet-potato-casserole-1641660.jpg",
            "https://images.freeimages.com/images/previews/43e/sunday-roast-thanksgiving-turkey-dinner-1631998.jpg"

        )
        return listImg[(Math.random() * (listImg.size - 1)).roundToInt()]
    }

}
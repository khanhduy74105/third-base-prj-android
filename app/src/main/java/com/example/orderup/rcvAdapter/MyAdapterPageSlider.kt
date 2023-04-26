package com.example.orderup.rcvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderup.R
import com.example.orderup.databinding.RcvCategoryItemRestaurantBinding

class MyAdapterPageSlider(private val images: List<Int>) : RecyclerView.Adapter<MyAdapterPageSlider.ViewHolder>() {
    private lateinit var binding: RcvCategoryItemRestaurantBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vp_item_slider, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(image: Int) {
            imageView.setImageResource(image)
        }
    }
}
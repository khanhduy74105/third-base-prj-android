package com.example.orderup.rcvAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.orderup.R
import com.example.orderup.databinding.RcvCommentItemBinding
import com.example.orderup.databinding.RcvFavoriteItemsBinding
import com.example.orderup.lib.tool
import com.example.orderup.model.ModalUser
import com.example.orderup.model.ModelComment
import com.example.orderup.model.ModelFood
import com.example.orderup.modelview.FavoriteViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.Holder> {
    private lateinit var binding: RcvCommentItemBinding
    private var context: Context
    public var commentList: ArrayList<ModelComment>

    constructor(context: Context, commentList: ArrayList<ModelComment>) : super() {
        this.context = context
        this.commentList = commentList
    }


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var usernameTv: TextView = binding.usernameTv
        var userIv: ImageView = binding.userIv
        var cmtContentTv: TextView = binding.cmtContentTv
        var timestamp: TextView = binding.timestamp
        var ratingBar: RatingBar = binding.rating
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = RcvCommentItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return Holder(binding.root)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val comment = commentList[position]
        holder.cmtContentTv.text = comment.content
        holder.ratingBar.rating = comment.rating.toFloat()
        holder.timestamp.text = tool.calculateDiffTime(comment.timestamp)
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(comment.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(ModalUser::class.java)!!
                    holder.usernameTv.text = user.username
                    val imgUri = user.profileImage.toUri().buildUpon().scheme("https").build()
                    Glide.with(holder.userIv.context)
                        .load(imgUri)
                        .centerCrop()
                        .apply(
                            RequestOptions()
                                .placeholder(R.drawable.loading_animation)
                                .error(R.drawable.ic_broken_image)
                        )
                        .into(holder.userIv)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

}
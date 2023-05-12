package com.example.orderup.restaurantdashboard

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.orderup.R
import com.example.orderup.databinding.FragmentAboutRestaurantBinding
import com.example.orderup.databinding.FragmentInfoRestaurantBinding
import com.example.orderup.model.ModalUser
import com.example.orderup.model.ModelOrder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfoRestaurant.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoRestaurant : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentInfoRestaurantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoRestaurantBinding.inflate(inflater,container,  false)
        val ref = FirebaseDatabase.getInstance().getReference("Userrs/")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val model = ds.getValue(ModalUser::class.java)
                    if (model != null) {
                        val imageUrl = model.profileImage
                        val imgUri = imageUrl.toUri().buildUpon().scheme("https").build()
                        if(model.userType=="owner"){
                           Glide.with(binding.imgInfoRes.context)
                               .load(imgUri)
                               .centerCrop()
                               .apply(
                                   RequestOptions()
                                       .placeholder(R.drawable.loading_animation)
                                       .error(R.drawable.ic_broken_image))
                               .into(binding.imgInfoRes)
                            binding.nameRes.text = model.username
                            binding.address.text = model.address
                       }
                    }
                }

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return inflater.inflate(R.layout.fragment_info_restaurant, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InfoRestaurant.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoRestaurant().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
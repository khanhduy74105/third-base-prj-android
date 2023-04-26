package com.example.orderup.usedashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.orderup.databinding.FragmentFavoriteBinding
import com.example.orderup.model.ModelFood
import com.example.orderup.modelview.FavoriteViewModel
import com.example.orderup.rcvAdapter.FavoritesAdapter
import com.example.orderup.rcvAdapter.FoodsAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var foodBtn: Button
    private lateinit var restaurantBtn: Button
    private lateinit var foodsAdapter: FoodsAdapter
    private lateinit var foodList: ArrayList<ModelFood>
    private lateinit var favoriteViewModel: FavoriteViewModel
    private var isFoodState = true
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
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater,container , false)
        favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
        foodBtn = binding.foodsBtn
        restaurantBtn = binding.restaurantsBtn
        foodBtn.isSelected = true
        restaurantBtn.isSelected = false
        foodBtn.setOnClickListener {
            isFoodState = true
            stateChanged()
        }

        restaurantBtn.setOnClickListener {
            isFoodState = false
            stateChanged()
        }
        setFavFoods(container!!)
        return binding.root
    }



    private fun setFavFoods(container: ViewGroup) {
        favoriteViewModel.favFoodItemArraylist.observe(viewLifecycleOwner){
            foodList = it
            val adapterCartItem = FavoritesAdapter(container.context, it, favoriteViewModel)
            binding.favoriteRcv.adapter = adapterCartItem
        }
    }
    private fun stateChanged() {
        if (isFoodState){
            foodBtn.isSelected =  true
            restaurantBtn.isSelected = false

        }else{
            foodBtn.isSelected =  false
            restaurantBtn.isSelected = true
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavoriteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
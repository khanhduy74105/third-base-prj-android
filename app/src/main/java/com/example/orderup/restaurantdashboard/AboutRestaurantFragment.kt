package com.example.orderup.restaurantdashboard.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.orderup.R
import com.example.orderup.databinding.FragmentAboutRestaurantBinding
import com.example.orderup.databinding.FragmentManageCategoryBinding
import com.example.orderup.rcvAdapter.MyAdapterPageSlider
import com.example.orderup.restaurantdashboard.ManageCategory
import com.example.orderup.restaurantdashboard.ManageOrder
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AboutRestaurantFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutRestaurantFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentAboutRestaurantBinding
    private lateinit var viewPager :ViewPager2
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
        binding = FragmentAboutRestaurantBinding.inflate(inflater,container,  false)
        sliderShow()
        return binding.root
    }
    fun sliderShow(){
        viewPager=binding.viewPager
        val images = listOf(
            R.drawable.item_slider1,
            R.drawable.item_slider2,
            R.drawable.item_slider3,
        )
        viewPager.adapter = MyAdapterPageSlider(images)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.offscreenPageLimit = 1

        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            var currentItem = viewPager.currentItem
            currentItem += 1
            if (currentItem >= images.size) {
                currentItem = 0
            }
            viewPager.currentItem = currentItem
        }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(runnable)
            }
        }, 3000, 3000)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AboutRestaurantFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AboutRestaurantFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
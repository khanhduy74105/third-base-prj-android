package com.example.orderup.usedashboard

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.orderup.databinding.FragmentCartBinding
import com.example.orderup.lib.tool
import com.example.orderup.model.ModelCartItem
import com.example.orderup.modelview.CartViewModel
import com.example.orderup.rcvAdapter.FoodsCartAdapter
import com.google.firebase.auth.FirebaseAuth
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
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartItemArraylist: ArrayList<ModelCartItem>
    private lateinit var cartViewModel: CartViewModel
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
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        if (tool.getCurrentId() != "") loadCartItem(container!!)

        binding.checkoutBtn.setOnClickListener {
            checkoutOrder()
        }
        return binding.root
    }

    private fun checkoutOrder() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm  make this order?")
            .setMessage("Are you sure to order those items")
            .setPositiveButton("Confirm"){
                    a,d ->
                Toast.makeText(context, "Confirming...", Toast.LENGTH_SHORT).show()
                cartViewModel.checkout(total, context)
            }
            .setNegativeButton("Cancel"){
                    a,d ->
                a.dismiss()
            }
            .show()
    }
    

    private fun loadCartItem(container: ViewGroup) {
        cartViewModel.cartItemArraylist.observe(viewLifecycleOwner){
            cartItemArraylist = it
            binding.cartSv.isSelected = it.size != 0
            val adapterCartItem = FoodsCartAdapter(container.context, it, cartViewModel)
            binding.cartRcv.adapter = adapterCartItem
            fillPrice()
        }
    }


    private var subTotal: Long = 0
    private var discountPrice: Long = 0
    private var total: Long = 0
    private var delivery: Long = 0
    private fun fillPrice() {
        subTotal = 0
        for (model in cartItemArraylist){
            subTotal += (model.price.toLong() * model.amount)
            total = subTotal + discountPrice + delivery
        }
        binding.subtotalTv.text = subTotal.toString()
        binding.discountTv.text = discountPrice.toString()
        binding.totalTv.text = total.toString()
        binding.deliveryTv.text = delivery.toString()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
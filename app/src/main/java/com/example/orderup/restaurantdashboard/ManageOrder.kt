package com.example.orderup.restaurantdashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import com.example.orderup.databinding.FragmentManageOrderBinding
import com.example.orderup.lib.tool
import com.example.orderup.model.ModalUser
import com.example.orderup.model.ModelOrder
import com.example.orderup.rcvAdapter.OrderResAdapter
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
 * Use the [ManageOrder.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManageOrder : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentManageOrderBinding
    private lateinit var newOrder: Button
    private lateinit var historyOrder: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var newOrderArraylist: ArrayList<ModelOrder>
    private lateinit var historyOrderArraylist: ArrayList<ModelOrder>
    private lateinit var usersUid: ArrayList<String>

    private var isNewState = true
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
        binding = FragmentManageOrderBinding.inflate(inflater,container,  false)
        newOrder = binding.NewOrder
        historyOrder = binding.historyOrder
        getUser()
        historyOrderArraylist = ArrayList()
        newOrderArraylist = ArrayList()
        newOrder.setOnClickListener {
            isNewState = true
            stateChanged()
            getNewOrder()
        }

        historyOrder.setOnClickListener {
            isNewState = false
            stateChanged()
            getHistoryOrder()
        }
        return binding.root
    }
    fun getUser(){
        usersUid =ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    val model = ds.child("uid").value.toString()
                    usersUid.add(model)
                }
                getData(usersUid)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun getData(usersUid:ArrayList<String>) {
        for (i in 1.. usersUid.size) {
            val ref = FirebaseDatabase.getInstance().getReference("Orders/${usersUid[i-1]}")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        val model = ds.getValue(ModelOrder::class.java)
                        Log.i("Order","$model")
                        if (model != null) {
                            if(model.state=="waiting"){
                                newOrderArraylist.add(model)
                            }else{
                                historyOrderArraylist.add(model)
                            }
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }
    fun getNewOrder(){
        val adapterCategory = OrderResAdapter(this.requireContext(),newOrderArraylist)
        binding.rcvItemOrder.layoutManager = GridLayoutManager(this.context, 1)
        binding.rcvItemOrder.adapter = adapterCategory
    }
    fun getHistoryOrder(){
        historyOrderArraylist.sort()
        val adapterCategory = OrderResAdapter(this.requireContext(),historyOrderArraylist)
        binding.rcvItemOrder.layoutManager = GridLayoutManager(this.context, 1)
        binding.rcvItemOrder.adapter = adapterCategory
    }
    private fun stateChanged() {
        if (isNewState){
            newOrder.isSelected =  true
            historyOrder.isSelected = false

        }else{
            newOrder.isSelected =  false
            historyOrder.isSelected = true
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ManageOrder.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManageOrder().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
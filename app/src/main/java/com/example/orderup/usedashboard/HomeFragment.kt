package com.example.orderup.usedashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderup.R
import com.example.orderup.UserSearchActivity
import com.example.orderup.databinding.FragmentHomeBinding
import com.example.orderup.lib.tool
import com.example.orderup.model.ModelCategory
import com.example.orderup.model.ModelFood
import com.example.orderup.modelview.HomeVIewModel
import com.example.orderup.modelview.OrderViewModel
import com.example.orderup.rcvAdapter.CategoryAdapter
import com.example.orderup.rcvAdapter.FoodsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentHomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var foodsArraylist: ArrayList<ModelFood>
    private lateinit var categoriesArraylist: ArrayList<ModelCategory>
    private lateinit var homeVIewModel: HomeVIewModel
    private lateinit var adapterFood: FoodsAdapter

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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        loadCategories(container!!)
        loadFoods(container)
        homeVIewModel = ViewModelProvider(this)[HomeVIewModel::class.java]
        loadHistorySearchs(container)
        binding.seacrhBtn.setOnClickListener {
            onSearchClick()
        }
        return binding.root
    }

    private fun loadHistorySearchs(container: ViewGroup) {
        homeVIewModel.historySearchItem.observe(viewLifecycleOwner) {
            val adapter =
                ArrayAdapter<String>(container.context, android.R.layout.simple_list_item_1, it)
            binding.searchEt.setAdapter(adapter)
        }
    }

    private fun onSearchClick() {
        val searchingString: String = binding.searchEt.text.toString()
        if (searchingString.length > 0) {
            addItemHistorySearch(searchingString)
            val intent = Intent(context, UserSearchActivity::class.java)
            intent.putExtra("searchValue", searchingString)
            startActivity(intent)
        }
    }

    private fun addItemHistorySearch(searchingString: String) {
        homeVIewModel.addHistorySearchs(searchingString)
    }

    private fun loadCategories(container: ViewGroup) {
        categoriesArraylist = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categorys")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriesArraylist.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue(ModelCategory::class.java)
                    categoriesArraylist.add(model!!)
                }
                val adapterCategory = CategoryAdapter(container.context, categoriesArraylist)
                binding.categoryRcv.adapter = adapterCategory
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun loadFoods(container: ViewGroup) {
        foodsArraylist = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Foods")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foodsArraylist.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue(ModelFood::class.java)
                    foodsArraylist.add(model!!)
                    Log.i("HOMEt", model.foodname)

                }
                binding.foodsRcv.layoutManager = GridLayoutManager(container.context, 2)
                adapterFood = FoodsAdapter(container.context, foodsArraylist)
                binding.foodsRcv.adapter = adapterFood
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun checkUser() {
        val uid: String = tool.getCurrentId()
        if (uid == "") {
            binding.nameTv.text = "Not logged in"
            binding.addressTv.text = "Log in to delivery"
        } else {
            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        binding.nameTv.text = snapshot.child("username").value.toString()
                        val address = snapshot.child("address").value.toString()
                        if (address != "") binding.addressTv.text =
                            snapshot.child("address").value.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
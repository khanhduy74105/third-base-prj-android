package com.example.orderup.restaurantdashboard

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.orderup.R
import com.example.orderup.databinding.FragmentAddCategoryBinding
import com.example.orderup.lib.tool
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddCategoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentAddCategoryBinding

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
        val binding = FragmentAddCategoryBinding.inflate(inflater,container,  false)
        return  binding.root
    }
    var category =""
    private fun validateData() {
       category= categoryEt.text.toString().trim()
        if (category.isEmpty()){
            Toast.makeText(this.context, "Category", Toast.LENGTH_SHORT).show()
        }else{
           AddCategory()
        }
    }
    private fun AddCategory() {
        progressDialog.setMessage("Saving....")
        val timestamp = System.currentTimeMillis()
        val uid = tool.getCurrentId()
        val hashMap: HashMap<String, Any?> =  HashMap()
        hashMap["id"]="$timestamp"
        hashMap["uid"] = uid
        hashMap["category"]= category
        hashMap["timestamp"] = timestamp
        Log.i(TAG, "${hashMap}}")
        Log.i(TAG, "$uid")


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddCategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddCategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
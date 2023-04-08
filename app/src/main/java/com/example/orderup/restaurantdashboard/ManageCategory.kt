package com.example.orderup.restaurantdashboard

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.orderup.R
import com.example.orderup.databinding.FragmentAddCategoryBinding
import com.example.orderup.databinding.FragmentManageCategoryBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var  dialogBuiler: AlertDialog.Builder;
private lateinit var dialog : AlertDialog ;
private lateinit var newCategoryEt: TextInputEditText;
/**
 * A simple [Fragment] subclass.
 * Use the [ManageCategory.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManageCategory : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var categoryEt : TextInputEditText
    val TAG = "Add category"
    private lateinit var binding: FragmentManageCategoryBinding
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
        val binding = FragmentManageCategoryBinding.inflate(inflater,container,  false)


        binding.button.setOnClickListener{
           CreateDialog()
       }
        return  binding.root
    }
    var category =""
    fun CreateDialog() {
        dialogBuiler = AlertDialog.Builder(context)
        dialogBuiler.setTitle("Title")
        val inflater = LayoutInflater.from(context)
        val dialogLayout = inflater.inflate(R.layout.fragment_add_category, null)
        dialogBuiler.setView(dialogLayout)
        firebaseAuth = FirebaseAuth.getInstance()
        categoryEt =dialogLayout.findViewById<TextInputEditText>(R.id.categoryEt)
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        dialogBuiler.setPositiveButton("OK") { dialog, which ->
            // Do something when the OK button is clicked
            validateData()
        }
        dialogBuiler.setNegativeButton("Cancel") { dialog, which ->
            // Do something when the Cancel button is clicked
        }
    // Create and show the alert dialog
        val dialog = dialogBuiler.create()
        dialog.show()
    }
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
        val uid = firebaseAuth.uid!!
        val hashMap: HashMap<String, Any?> =  HashMap()
        hashMap["id"]="$timestamp"
        hashMap["uid"] = uid
        hashMap["category"]= category
        hashMap["timestamp"] = timestamp
        Log.i(TAG, "${hashMap}}")
        Log.i(TAG, "$uid")

        // set data to db
        val ref = FirebaseDatabase.getInstance().getReference("Categorys")
        ref.child(timestamp.toString())
            .setValue(hashMap)
            .addOnSuccessListener {
                Log.i(TAG, "luu vao DB success")
                progressDialog.dismiss()
//                Toast.makeText(this, "creating account successfully", Toast.LENGTH_SHORT).show()
//                finish()
                startActivity(Intent(requireContext(), RestaurantDashboardActivity::class.java))
            }
            .addOnFailureListener {
                Log.i(TAG, "luu vao DB that bai")
                progressDialog.dismiss()
//                Toast.makeText(this, "Failed creating account due to ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ManageCategory.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManageCategory().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
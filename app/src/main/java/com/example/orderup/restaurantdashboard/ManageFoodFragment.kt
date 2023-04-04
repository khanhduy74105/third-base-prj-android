package com.example.orderup.restaurantdashboard.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.orderup.R
import com.example.orderup.databinding.FragmentManageFoodBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private val PICK_IMAGE_REQUEST: Int = 1
private lateinit var binding: FragmentManageFoodBinding

private lateinit var firebaseAuth: FirebaseAuth

private lateinit var pickImgBtn: ImageButton
private lateinit var imgIv: ImageView
private lateinit var foodnameEt: TextInputEditText
private lateinit var desEt: TextInputEditText
private lateinit var priceEt: TextInputEditText
private lateinit var saveBtn: Button
private lateinit var progressBar: ProgressBar
private lateinit var imgUri: Uri
/**
 * A simple [Fragment] subclass.
 * Use the [RestaurantFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManageFoodFragmentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val binding = FragmentManageFoodBinding.inflate(inflater,container,  false)
        firebaseAuth = FirebaseAuth.getInstance()
        pickImgBtn = binding.pickImgBtn
        imgIv = binding.userIv
        foodnameEt = binding.foodnameEt
        desEt = binding.descriptionEt
        priceEt = binding.priceEt
        saveBtn = binding.saveBtn
        pickImgBtn.setOnClickListener {
            openFileChooser()
        }
        saveBtn.setOnClickListener {
            validateData()
        }
//        setUserData()
        return binding.root
    }
    var foodname:String = ""
    var descripton:String = ""
    var price:String = ""
    private fun validateData() {
        foodname = foodnameEt.text.toString().trim()
        descripton = desEt.text.toString().trim()
        price = priceEt.text.toString().trim()
        if (foodname.isEmpty()){
            Toast.makeText(this.context, "Enter your name", Toast.LENGTH_SHORT).show()
        }else if (descripton.isEmpty()){
            Toast.makeText(this.context, "Enter your Address", Toast.LENGTH_SHORT).show()
        }else if (price.isEmpty()){
            Toast.makeText(this.context, "Phone", Toast.LENGTH_SHORT).show()
        }else{
            uploadInfo()
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun uploadInfo() {
        val timestamp = System.currentTimeMillis()

        val uid = firebaseAuth.uid!!
        val ref = FirebaseDatabase.getInstance().getReference("Users/$uid")
        ref.child("username").setValue(foodname)
        ref.child("address").setValue(descripton)
        ref.child("phone").setValue(price)
            .addOnSuccessListener {
                Toast.makeText(this.context, "Updated!!!", Toast.LENGTH_SHORT)
            }
    }
    private fun openFileChooser() {
        progressBar.visibility = View.VISIBLE
        var intent = Intent()
        intent.type= "image/*"
        intent.action= Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RestaurantFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManageFoodFragmentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
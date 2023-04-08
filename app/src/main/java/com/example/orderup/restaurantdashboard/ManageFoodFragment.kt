package com.example.orderup.restaurantdashboard.ui

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import com.example.orderup.R
import com.example.orderup.databinding.FragmentManageFoodBinding
import com.example.orderup.restaurantdashboard.RestaurantDashboardActivity
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
private var category:String = ""
private lateinit var progressDialog: ProgressDialog
private lateinit var arrayAdapter: ArrayAdapter<*>
private lateinit var autoText : AutoCompleteTextView
private lateinit var imgUri: Uri
var TAG ="ADD_FOOD"
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
        var listCategory = arrayOf(
            "trang mieng",
            "sieu ngon",
            "khai vi",
        )
        autoText=binding.categoryAutotext
        arrayAdapter = ArrayAdapter<String>(container!!.context,R.layout.list_category_addfood, listCategory)
        autoText.setAdapter(arrayAdapter)
        autoText.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            category = parent.getItemAtPosition(position).toString()

        })

        progressDialog = ProgressDialog(container!!.context)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        pickImgBtn = binding.pickImgBtn
        imgIv = binding.userIv
        foodnameEt = binding.foodnameEt
        desEt = binding.descriptionEt
        priceEt = binding.priceEt
        saveBtn = binding.saveBtn
        progressBar = binding.progressBar
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
            Toast.makeText(this.context, "Enter your food", Toast.LENGTH_SHORT).show()
        }else if (descripton.isEmpty()){
            Toast.makeText(this.context, "Enter your Des", Toast.LENGTH_SHORT).show()
        }else if (price.isEmpty()){
            Toast.makeText(this.context, "Price", Toast.LENGTH_SHORT).show()
        }else{
           AddCategory()
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun AddCategory() {
        progressDialog.setMessage("Saving....")
        val timestamp = System.currentTimeMillis()
        val uid = firebaseAuth.uid!!
        val hashMap: HashMap<String, Any?> =  HashMap()
        hashMap["id"]="$timestamp"
        hashMap["uid"] = uid
        hashMap["foodname"] = foodname
        hashMap["descripton"] = descripton
        hashMap["price"] = price
        hashMap["category"]= category
        hashMap["timestamp"] = timestamp
        hashMap["buyCount"]=0
        hashMap["numberOfLike"]=0
        Log.i(TAG, "${hashMap}}")
        Log.i(TAG, "$uid")

        // set data to db
        val ref = FirebaseDatabase.getInstance().getReference("Foods")
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
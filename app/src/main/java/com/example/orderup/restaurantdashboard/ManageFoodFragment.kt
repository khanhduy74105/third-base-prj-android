package com.example.orderup.restaurantdashboard.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.orderup.R
import com.example.orderup.databinding.FragmentManageFoodBinding
import com.example.orderup.model.ModelCategory
import com.example.orderup.restaurantdashboard.RestaurantDashboardActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var category:String = ""
private lateinit var progressDialog: ProgressDialog
private lateinit var arrayAdapter: ArrayAdapter<*>
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

    private val PICK_IMAGE_REQUEST: Int = 1
    private lateinit var binding: FragmentManageFoodBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var pickImgBtn: ImageButton
    private lateinit var imgIv: ImageView
    private lateinit var foodnameEt: TextInputEditText
    private lateinit var desEt: TextInputEditText
    private lateinit var priceEt: TextInputEditText
    private lateinit var saveBtn: Button
    private var imgUri: Uri? = null
    private lateinit var categoryArraylist: ArrayList<ModelCategory>
    private lateinit var categoryTv: TextView
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
        binding = FragmentManageFoodBinding.inflate(inflater,container,  false)
        firebaseAuth = FirebaseAuth.getInstance()
        loadPdfCatagories()
        categoryTv = binding.categoryTv
        progressDialog = ProgressDialog(container!!.context)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        pickImgBtn = binding.pickImgBtn
        imgIv = binding.userIv
        foodnameEt = binding.foodnameEt
        desEt = binding.descriptionEt
        priceEt = binding.priceEt
        saveBtn = binding.saveBtn

        categoryTv.setOnClickListener {
            categoryPickDialog()
        }
        pickImgBtn.setOnClickListener {
//            openFileChooser()
        }
        saveBtn.setOnClickListener {
            validateData()
        }
        return binding.root
    }
    var foodname:String = ""
    var description:String = ""
    var price:String = ""
    private fun validateData() {
        foodname = foodnameEt.text.toString().trim()
        description = desEt.text.toString().trim()
        price = priceEt.text.toString().trim()
        if (foodname.isEmpty()){
            Toast.makeText(this.context, "Enter your food", Toast.LENGTH_SHORT).show()
        }else if (description.isEmpty()){
            Toast.makeText(this.context, "Enter your Des", Toast.LENGTH_SHORT).show()
        }else if (price.isEmpty()){
            Toast.makeText(this.context, "Price", Toast.LENGTH_SHORT).show()
//        }else if (imgUri == null ){
//            Toast.makeText(this.context, "Choose image!", Toast.LENGTH_SHORT).show()
        }
        else{
           AddCategory()
        }
    }
    private var selectedCategoryId = ""
    private var selectedCategoryTitle = ""
    @SuppressLint("SuspiciousIndentation")
    private fun AddCategory() {
        progressDialog.setMessage("Saving....")
        val timestamp = System.currentTimeMillis()
        val uid = firebaseAuth.uid!!
        val hashMap: HashMap<String, Any?> =  HashMap()
        hashMap["id"]="$timestamp"
        hashMap["uid"] = uid
        hashMap["foodname"] = foodname
        hashMap["description"] = description
        hashMap["price"] = price
        hashMap["category"]= selectedCategoryTitle
        hashMap["timestamp"] = timestamp
        hashMap["buyCount"]=0
        hashMap["numberOfLike"]=0
        hashMap["imageUrl"]="foods/$timestamp"
        Log.i(TAG, "${hashMap}}")
        Log.i(TAG, "$uid")

        val ref = FirebaseDatabase.getInstance().getReference("Foods")
//        uploadImgToStorage("$timestamp")
        ref.child(timestamp.toString())
            .setValue(hashMap)
            .addOnSuccessListener {
                Log.i(TAG, "luu vao DB success")
                progressDialog.dismiss()
                startActivity(Intent(requireContext(), RestaurantDashboardActivity::class.java))
            }
            .addOnFailureListener {
                Log.i(TAG, "luu vao DB that bai")
                progressDialog.dismiss()
            }

    }
//    private fun openFileChooser() {
//        val intent = Intent()
//        intent.type= "image/*"
//        intent.action= Intent.ACTION_GET_CONTENT
//        startActivityForResult(intent, PICK_IMAGE_REQUEST)
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null){
//            imgUri = data.data!!
//            Glide.with(imgIv.context)
//                .load(imgUri)
//                .apply(
//                    RequestOptions()
//                        .placeholder(R.drawable.loading_animation)
//                        .error(R.drawable.ic_broken_image))
//                .into(imgIv)
//        }
//    }

    private fun categoryPickDialog(){
        Log.d(TAG, "categoryPickDialog: categoryPickDialog categories")

        val categoriesArray = arrayOfNulls<String>(categoryArraylist.size)
        for (i in categoriesArray.indices){
            categoriesArray[i] = categoryArraylist[i].category
        }

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Please pick category")
            .setItems(categoriesArray){
                    dialog, which ->
                selectedCategoryTitle = categoryArraylist[which].category
                selectedCategoryId = categoryArraylist[which].id

                binding.categoryTv.text = selectedCategoryTitle
                Log.d(TAG, "categoryPickDialog id: $selectedCategoryId")
                Log.d(TAG, "categoryPickDialog title: $selectedCategoryTitle")
            }
            .show()
    }
    private fun loadPdfCatagories() {
        Log.d(TAG, "loadPdfCatagories: Loading ppdf categories")

        categoryArraylist = ArrayList()

        val ref= FirebaseDatabase.getInstance().getReference("Categorys")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArraylist.clear()
                for (ds in snapshot.children){
                    val model = ds.getValue(ModelCategory::class.java)
                    categoryArraylist.add(model!!)
                    Log.d(TAG, "onDataChange: Loading ppdf categories")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
//    private fun uploadImgToStorage(path: String) {
//        val filePathAndName = "foods/$path"
//        val storageRef = FirebaseStorage.getInstance().getReference(filePathAndName)
//        storageRef.putFile(imgUri!!)
//            .addOnSuccessListener {taskSnapshot ->
//                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
//                while (!uriTask.isSuccessful);
//
//                Toast.makeText(this.context, "Updated food!", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener{
//                Toast.makeText(this.context, "failed upload img due to ${it.message}", Toast.LENGTH_SHORT).show()
//            }
//
//    }
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
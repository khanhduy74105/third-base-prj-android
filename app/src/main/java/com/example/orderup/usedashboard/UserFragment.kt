package com.example.orderup.usedashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.orderup.R
import com.example.orderup.databinding.FragmentUserBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val PICK_IMAGE_REQUEST: Int = 1
    private lateinit var binding: FragmentUserBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var pickImgBtn: ImageButton
    private lateinit var imgIv: ImageView
    private lateinit var usernameTv: TextView
    private lateinit var usernameEt: TextInputEditText
    private lateinit var addressEt: TextInputEditText
    private lateinit var phoneEt: TextInputEditText
    private lateinit var saveBtn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var imgUri: Uri
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
        val binding = FragmentUserBinding.inflate(inflater,container,  false)
        firebaseAuth = FirebaseAuth.getInstance()
        pickImgBtn = binding.pickImgBtn
        imgIv = binding.userIv
        usernameTv = binding.userNameTv
        usernameEt = binding.usernameEt
        addressEt = binding.addressEt
        phoneEt = binding.phoneEt
        saveBtn = binding.saveBtn
        progressBar = binding.progressBar
        pickImgBtn.setOnClickListener {
            openFileChooser()
        }
        saveBtn.setOnClickListener {
            validateData()
        }
        setUserData()
        return binding.root
    }
    var username:String = ""
    var address:String = ""
    var phone:String = ""
    private fun validateData() {
        username = usernameEt.text.toString().trim()
        address = addressEt.text.toString().trim()
        phone = phoneEt.text.toString().trim()
        if (username.isEmpty()){
            Toast.makeText(this.context, "Enter your name", Toast.LENGTH_SHORT).show()
        }else if (address.isEmpty()){
            Toast.makeText(this.context, "Enter your Address", Toast.LENGTH_SHORT).show()
        }else if (phone.isEmpty()){
            Toast.makeText(this.context, "Phone", Toast.LENGTH_SHORT).show()
        }else{
            uploadInfo()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun uploadInfo() {
        val uid = firebaseAuth.uid!!
        val ref = FirebaseDatabase.getInstance().getReference("Users/$uid")
            ref.child("username").setValue(username)
            ref.child("address").setValue(address)
            ref.child("phone").setValue(phone)
                .addOnSuccessListener {
                    Toast.makeText(this.context, "Updated!!!", Toast.LENGTH_SHORT)
                }
    }

    private fun openFileChooser() {
        progressBar.visibility = View.VISIBLE
        var intent = Intent()
        intent.type= "image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    fun setUserData(){
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            usernameEt.setText("Not Loged in")
            addressEt.setText("Not Loged in")
            phoneEt.setText("Not Loged in")
            usernameTv.setText("Not Loged in")
            imgIv.setImageResource(R.drawable.logo_transparent)
        }else{
            val uid: String? = firebaseAuth.currentUser?.uid
            if (uid!=null){
                val ref = FirebaseDatabase.getInstance().getReference("Users")
                ref.child(uid)
                    .addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            usernameEt.setText(snapshot.child("username").value.toString())
                            usernameTv.text = snapshot.child("username").value.toString()
                            addressEt.setText(snapshot.child("address").value.toString())
                            phoneEt.setText(snapshot.child("phone").value.toString())
                            val imgUrl = snapshot.child("profileImage").value.toString()
                            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                            Glide.with(imgIv.context)
                                .load(imgUri)
                                .apply(
                                    RequestOptions()
                                        .placeholder(R.drawable.loading_animation)
                                        .error(R.drawable.ic_broken_image))
                                .into(imgIv)
                            progressBar.visibility = View.INVISIBLE

                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })


            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null){
            imgUri = data.data!!
            uploadImgToStorage()
        }
    }
    private fun uploadImgToStorage() {
        val uid = firebaseAuth.uid
        val filePathAndName = "Avatars/$uid"
        val storageRef = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageRef.putFile(imgUri!!)
            .addOnSuccessListener {taskSnapshot ->
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadPdfUrl = "${uriTask.result}"
                val ref = FirebaseDatabase.getInstance().getReference("Users/$uid")
                ref.child("profileImage").setValue(uploadPdfUrl)
                Toast.makeText(this.context, "Changed avatar", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.INVISIBLE
                imgIv.setImageURI(imgUri)
            }
            .addOnFailureListener{
                Toast.makeText(this.context, "failed upload pdf due to ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
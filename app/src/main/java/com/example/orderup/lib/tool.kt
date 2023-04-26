package com.example.orderup.lib

import android.text.format.DateFormat
import android.util.Log
import com.example.orderup.model.ModalUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import java.util.concurrent.TimeUnit

class tool {
    companion object {
        private var currentUser: ModalUser? = null
        private var currentId: String = ""

        fun getDate(timestamp: Long): String {
            val calendar = Calendar.getInstance(Locale.ENGLISH)
            calendar.timeInMillis = timestamp
            val date = DateFormat.format("dd-MM-yyyy", calendar).toString()
            return date
        }

        fun getFacebookUser(callback: (ModalUser?) -> Unit) {
            if (currentId == "") {
                Log.i("GGG", "currentId null")
                callback(null)
            }

            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(currentId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    currentUser = snapshot.getValue(ModalUser::class.java)!!
                    Log.i("GGG", "currentId: $currentId, username: ${currentUser?.username}")
                    callback(currentUser)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
        fun setCurrentId(id:String){
            currentId = id
        }
        fun getCurrentId(): String{
            return currentId
        }

        fun calculateDiffTime(startTime: Long): String{
            val diffTime = System.currentTimeMillis() - startTime
            if (TimeUnit.MILLISECONDS.toDays(diffTime) > 1){
                return "${TimeUnit.MILLISECONDS.toDays(diffTime)} days ago"
            }else if (TimeUnit.MILLISECONDS.toHours(diffTime) > 1){
                return "${TimeUnit.MILLISECONDS.toHours(diffTime)} hours ago"
            }else {
                return "${TimeUnit.MILLISECONDS.toMinutes(diffTime)} mins ago"
            }
        }
    }
}
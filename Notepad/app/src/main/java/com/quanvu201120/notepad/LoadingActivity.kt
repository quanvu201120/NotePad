package com.quanvu201120.notepad

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.quanvu201120.notepad.model.NoteP
import com.quanvu201120.notepad.model.User




class LoadingActivity : AppCompatActivity() {

    lateinit var user : User
    lateinit var auth : FirebaseAuth
    lateinit var firestore : FirebaseFirestore
    lateinit var arrayNote : ArrayList<NoteP>

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        arrayNote = ArrayList()

        firestore.collection(C_USER).document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {

                user = it.toObject(User::class.java)!!

                //
//                var note = NoteP("title3","content3","3/3/3",3,"ghi")
//                firestore.collection(C_NOTEPAD).document(note.docId)
//                    .set(note)
                //


                if (user.userNote == null || user.userNote!!.size == 0){
                    //rong
                    intentt()
                }
                else{
                    //khong rong

                    firestore.collection(C_NOTEPAD).whereIn("docId", user.userNote!!)
                        .get().addOnSuccessListener {

                            arrayNote = it.toObjects(NoteP::class.java) as ArrayList<NoteP>
                            intentt()

                        }


                }

            }
        SetColorStatusBar(this,R.color.purple_200)

    }

   fun intentt(){
       var intent = Intent(this,SyncActivity::class.java)

       intent.putExtra("ListNoteLoading",arrayNote)
       intent.putExtra("UserLoading", user)

       startActivity(intent)
       finish()
   }


}
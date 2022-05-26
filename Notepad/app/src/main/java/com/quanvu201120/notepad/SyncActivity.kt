package com.quanvu201120.notepad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.quanvu201120.notepad.fragment.BottomFragment1
import com.quanvu201120.notepad.fragment.BottomFragment2
import com.quanvu201120.notepad.model.NoteP
import com.quanvu201120.notepad.model.User

var user_sync : User = User()
var arrayNote_sync : ArrayList<NoteP> = ArrayList()

class SyncActivity : AppCompatActivity() {

    lateinit var bottom: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync)

        bottom = findViewById(R.id.bottom_navigation)

        arrayNote_sync = intent.getParcelableArrayListExtra<NoteP>("ListNoteLoading") as ArrayList<NoteP>
        user_sync = intent.getSerializableExtra("UserLoading") as User

        showFragment(R.id.itemBottom1)
        bottom.setOnItemSelectedListener {

            showFragment(it.itemId)

            return@setOnItemSelectedListener true
        }


        SetColorStatusBar(this,R.color.purple_200)


    }

    fun showFragment (id : Int){

        var fragment : Fragment? = null

        if (id == R.id.itemBottom1){
            fragment = BottomFragment1()
        }
        else{
            fragment = BottomFragment2()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_sync,fragment)
            .commit()

    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this@SyncActivity, MainActivity::class.java))
    }



}


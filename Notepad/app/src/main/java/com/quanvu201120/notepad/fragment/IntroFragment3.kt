package com.quanvu201120.notepad.fragment

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.quanvu201120.notepad.MainActivity
import com.quanvu201120.notepad.R


class IntroFragment3 : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_intro3, container, false)

        var button = view.findViewById<Button>(R.id.btn_batdau)

        button.setOnClickListener {

            var sharedPreferences : SharedPreferences = requireContext().getSharedPreferences("BatDau",Activity.MODE_PRIVATE)
            var editor : SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("OK",true)
            editor.commit()

            var intent = Intent(context,MainActivity::class.java)
            startActivity(intent)

            activity?.finish()
        }


        return view
    }


}
package com.quanvu201120.notepad

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.quanvu201120.notepad.adapter.ViewPagerIntroAdapter
import com.quanvu201120.notepad.model.ZoomOutPageTransformer
import me.relex.circleindicator.CircleIndicator3

class IntroActivity : AppCompatActivity() {

    lateinit var viewPager2: ViewPager2
    lateinit var indicator3: CircleIndicator3
    lateinit var adapter : ViewPagerIntroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)


        var sharedPreferences = getSharedPreferences("BatDau", Activity.MODE_PRIVATE)
        var check : Boolean = sharedPreferences.getBoolean("OK",false)
        if (check){
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewPager2 = findViewById(R.id.viewpager2)
        indicator3 = findViewById(R.id.cricleIndicator)

        adapter = ViewPagerIntroAdapter(supportFragmentManager,lifecycle)
        viewPager2.adapter = adapter
        viewPager2.setPageTransformer(ZoomOutPageTransformer())

        indicator3.setViewPager(viewPager2)


        SetColorStatusBar(this,R.color.purple_200)

    }


}
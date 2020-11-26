package com.ramotion.navigationtoolbar.example.pager

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.ramotion.navigationtoolbar.example.MainActivity
import com.ramotion.navigationtoolbar.example.R

class Profile: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        //button
        val mStartActBtn = findViewById<ImageView>(R.id.back)
        //handle button click
        mStartActBtn.setOnClickListener {
            //start activity intent
            startActivity(Intent(this, MainActivity::class.java))
        }

    }
}
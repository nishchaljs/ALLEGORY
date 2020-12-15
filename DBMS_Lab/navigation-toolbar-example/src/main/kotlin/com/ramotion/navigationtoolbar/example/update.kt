package com.ramotion.navigationtoolbar.example

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ramotion.navigationtoolbar.example.pager.ItemUser


class update : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.updateactivity)
        Toast.makeText(this,"Welcome",Toast.LENGTH_SHORT)
        var title = findViewById<EditText>(R.id.title)
        var desc = findViewById<EditText>(R.id.desc)
        var genre = findViewById<EditText>(R.id.genre)
        var author = findViewById<EditText>(R.id.author)
        var radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val bundle: Bundle? = intent.extras

        bundle?.apply {
            //Parcelable Data
            val public: ItemUser.public? = getParcelable("ItemData")
            if (public != null) {
                title.setText(public?.name)
                desc.setText(public?.desc)
                genre.setText(public.genre)
                author.setText(public.author)
            }
        }

        var up = findViewById<TextView>(R.id.upload)
        up.setOnClickListener{
            Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT)
            startActivity(Intent(this,MainActivity::class.java))
            finishAffinity()
        }

    }

}
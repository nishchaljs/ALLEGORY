package com.ramotion.navigationtoolbar.example.pager
import android.R
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//import kotlin.random.Random.Default.Companion


class test: AppCompatActivity() {
    lateinit var etEmail: EditText
    lateinit var etSubject: EditText
    lateinit var etMessage: EditText
    lateinit var send: Button
    lateinit var attachment: Button
    lateinit var tvAttachment: TextView
    lateinit var email: String
    lateinit var subject: String
    lateinit var message: String
    lateinit var uri: Uri
    private val pickFromGallery:Int = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        attachment = findViewById(R.id.btAttachment)
//        tvAttachment = findViewById(R.id.tvAttachment)

        attachment.setOnClickListener {
            openFolder()
        }
    }
    private fun openFolder() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra("return-data", true)
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), pickFromGallery)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickFromGallery && resultCode == RESULT_OK) {
            if (data != null) {
                uri = data.data!!

            }
            tvAttachment.text = uri.lastPathSegment
            tvAttachment.visibility = View.VISIBLE
        }
    }
}
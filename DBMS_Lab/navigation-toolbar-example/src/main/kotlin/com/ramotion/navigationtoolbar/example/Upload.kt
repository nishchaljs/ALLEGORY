package com.ramotion.navigationtoolbar.example

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.net.URI
import java.sql.Types.NULL
import java.util.*
import kotlin.random.Random.Default.Companion


class Upload: AppCompatActivity() {
    private val pickPDF:Int = 2
    lateinit var uri: Uri
    lateinit var PATH_FILE: String
    lateinit var tvAttachment: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload_page)

        //button
        val mStartActBtn = findViewById<ImageView>(R.id.back)
        //handle button click
        mStartActBtn.setOnClickListener {
            //start activity intent
            startActivity(Intent(this, MainActivity::class.java))
        }

        var eText = findViewById<TextView>(R.id.date)
        val dateimage = findViewById<ImageView>(R.id.dateimage)
        eText.setInputType(InputType.TYPE_NULL)
        dateimage.setOnClickListener {
            //start activity intent
            val cldr = Calendar.getInstance()
            val day = cldr[Calendar.DAY_OF_MONTH]
            val month = cldr[Calendar.MONTH]
            val year = cldr[Calendar.YEAR]
            var datepicker = DatePickerDialog(this,
                    OnDateSetListener { view, year, monthOfYear, dayOfMonth -> eText.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year) }, year, month, day)
            datepicker.show()
        }


        val uploadpdf = findViewById<ImageView>(R.id.uploadpdf)
        tvAttachment = findViewById<TextView>(R.id.pdfpath)
        uploadpdf.setOnClickListener(View.OnClickListener { openFolder() })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickPDF && resultCode == RESULT_OK) {
            if (data != null) {
                uri = data.data!!
            }

            tvAttachment.setText(uri.toString())
            tvAttachment.visibility = View.VISIBLE
        }
    }

    private fun openFolder() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra("return-data", true)
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), pickPDF)
    }
}






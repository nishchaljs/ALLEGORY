package com.ramotion.navigationtoolbar.example.View

import android.app.DatePickerDialog
import java.time.LocalDateTime
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ramotion.navigationtoolbar.example.MainActivity
import com.ramotion.navigationtoolbar.example.Model.publicationModel
import com.ramotion.navigationtoolbar.example.R
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import java.io.IOException
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
//import kotlin.random.Random.Default.Companion


class Upload: AppCompatActivity() {
    var mHandler =  Handler(Looper.getMainLooper())
    private val client = OkHttpClient()
    private val pickPDF:Int = 2
    lateinit var uri: Uri
    lateinit var PATH_FILE: String
    private lateinit var mDatabase: DatabaseReference
    lateinit  var publist: MutableList<publicationModel>
    lateinit var tvAttachment: EditText
    lateinit var title : EditText
    lateinit var desc :EditText
   lateinit var genre :EditText
    lateinit  var author : EditText
    lateinit  var eText : EditText
    lateinit var radioGroup : RadioGroup

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload_page)

        //button
        val mStartActBtn = findViewById<ImageView>(R.id.back)
        //handle button click
        mStartActBtn.setOnClickListener {
            //start activity intent
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }

        title = findViewById<EditText>(R.id.title)
        desc = findViewById<EditText>(R.id.desc)
        genre = findViewById<EditText>(R.id.genre)
        author = findViewById<EditText>(R.id.author)
        radioGroup = findViewById<RadioGroup>(R.id.radioGroup)




        eText = findViewById<EditText>(R.id.date)
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
        tvAttachment = findViewById<EditText>(R.id.pdfpath)
        uploadpdf.setOnClickListener(View.OnClickListener { openFolder() })


        val uploadbutton = findViewById<TextView>(R.id.upload)
        //handle button click
        uploadbutton.setOnClickListener {
            if (validateInput(title.text.toString(), desc.text.toString(), genre.text.toString(), author.text.toString(), eText.text.toString(), tvAttachment.text.toString() )) {
                var user = FirebaseAuth.getInstance().currentUser
                var email = user?.email;
                var times = LocalDateTime.now()
                var time = times.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                val date = eText.text.toString()
                publist = mutableListOf()
                mDatabase = FirebaseDatabase.getInstance().getReference("publication")
                val pubID = (0..1000000000).random().toString()
                val intSelectButton: Int = radioGroup!!.checkedRadioButtonId
                val radioButton = findViewById<RadioButton>(intSelectButton)
                val pub = publicationModel(pubID!!, title.text.toString(), author.text.toString(),
                        radioButton.text.toString(), genre.text.toString(), desc.text.toString(), "story1", email.toString(), time.toString(), date.toString(), 12.toString())
                var typ = "story"
                if (pub.type == "Story") {
                    typ = "story"
                } else {
                    typ = "poem"
                }
                mDatabase.child(pubID).setValue(pub).addOnCompleteListener {
                    println("Pub created")
                    var url = "https://dbmsibm.herokuapp.com/api/publications?id='" + pubID + "'&typ='" + typ + "'&title='" + pub.name + "'&desc='" + pub.desc + "'&genre='" + pub.genre + "'&email='" + email.toString() + "'&time='" + time + "'&date='" + date + "'"
                    val client = OkHttpClient()
                    val request2 = Request.Builder()
                            .url(url)
                            .build()
                    client.newCall(request2).enqueue(object : Callback {
                        override fun onFailure(request: Request?, e: IOException?) {
                            println("FAIL AGIAN")
                        }

                        override fun onResponse(response: com.squareup.okhttp.Response?) {
                            mHandler.post {
                                println("Response  ${response?.message()}")
                                if (response?.message().toString() == "OK") {
                                    Toast.makeText(this@Upload, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    Toast.makeText(this@Upload, "Upload Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    })
                }

                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
        }

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

    private fun validateInput(inTitle: String, inDesc: String, inGenre: String, inAuthor: String, inDate: String, inPdfpath: String): Boolean {
        if (inTitle.isEmpty()) {
            title!!.error = "Title is empty."
            return false
        }
        if (inDesc.isEmpty()) {
            desc!!.error = "Description is empty."
            return false
        }
        if (inGenre.isEmpty()) {
            genre!!.error = "Genre is empty."
            return false
        }
        if (inAuthor.isEmpty()) {
            author!!.error = "Author is empty."
            return false
        }
        if (inDate.isEmpty()) {
            eText!!.error = "Date is empty."
            return false
        }
        if (inPdfpath.isEmpty()) {
            tvAttachment!!.error = "PDF Not Uploaded."
            return false
        }

        return true
    }
}






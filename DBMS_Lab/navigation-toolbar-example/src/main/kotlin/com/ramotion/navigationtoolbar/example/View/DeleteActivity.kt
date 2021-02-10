package com.ramotion.navigationtoolbar.example.View

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ramotion.navigationtoolbar.example.MainActivity
import com.ramotion.navigationtoolbar.example.R
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.IOException


class DeleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.delete_page)

        val intent = intent
        val pubID = intent.getStringExtra("pubID").toString()
        val typ = intent.getStringExtra("typ").toString()
        var title = findViewById<EditText>(R.id.title)
        var desc = findViewById<EditText>(R.id.desc)
        var genre = findViewById<EditText>(R.id.genre)
        var author = findViewById<EditText>(R.id.author)
        var radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val bundle: Bundle? = intent.extras
        var delete = findViewById<TextView>(R.id.delete)
        val client = OkHttpClient()
        var user = FirebaseAuth.getInstance().currentUser
        var email = user?.email
        var mHandler =  Handler(Looper.getMainLooper())
        desc.setText(intent.getStringExtra("desc").toString())
        title.setText(intent.getStringExtra("title").toString())
        genre.setText(intent.getStringExtra("genre").toString())
        author.setText(intent.getStringExtra("author").toString())
        radioGroup.check(R.id.radioMale)


        suspend fun dosoemthingAll() {

            val request1 = Request.Builder()
                    .url("https://dbmsibm.herokuapp.com/api/delpub?id='" + pubID.toString() + "'&typ='" + typ.toString() + "'")
                    .build()
            client.newCall(request1).enqueue(object : Callback {
                override fun onFailure(request: Request?, e: IOException?) {
                    println("FAIL AGIAN")
                }
                override fun onResponse(response: com.squareup.okhttp.Response?) {
                    mHandler.post {
                        println("Response  ${response?.message()}")
                        if (response?.message().toString() == "OK") {
                            Toast.makeText(this@DeleteActivity, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this@DeleteActivity, "Delete Failed: " + response?.message().toString(), Toast.LENGTH_SHORT).show()
                            println("Failed bcz: " + response?.message().toString())
                        }
                    }
                    //
                }
            }
            )

        }




        delete.setOnClickListener{
            runBlocking<Unit>{

                val x = async {
                    dosoemthingAll()
                }
                x.await()
                Toast.makeText(baseContext, "Delete Success", Toast.LENGTH_SHORT)

            }

            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }

    }

}
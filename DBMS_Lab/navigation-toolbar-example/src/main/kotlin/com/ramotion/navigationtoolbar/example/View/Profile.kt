package com.ramotion.navigationtoolbar.example.View

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ramotion.navigationtoolbar.example.MainActivity
import com.ramotion.navigationtoolbar.example.R
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import java.io.IOException


class Profile: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)


        //button
        val mStartActBtn = findViewById<ImageView>(R.id.back)
        val signout = findViewById<TextView>(R.id.signout)
        val name = findViewById<TextView>(R.id.name)
        val phno = findViewById<TextView>(R.id.phno)
        val pubid = findViewById<TextView>(R.id.pubid)
        val eml = findViewById<TextView>(R.id.email)


        var auth = Firebase.auth
        var user = auth.currentUser
        var anon = user?.isAnonymous


 //       setSpinner()

        signout.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
        //handle button click
        mStartActBtn.setOnClickListener {
            //start activity intent
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }

        if(anon == true){


        }

        else {


            val client = OkHttpClient()
            user = FirebaseAuth.getInstance().currentUser
            var email = user?.email
            var mHandler = Handler(Looper.getMainLooper())

            eml.text = email
            pubid.text = email






            suspend fun dosoemthingAll(url: String) {

                val request1 = Request.Builder()
                        .url(url)
                        .build()
                client.newCall(request1).enqueue(object : Callback {
                    override fun onFailure(request: Request?, e: IOException?) {
                        println("FAIL AGIAN")
                    }

                    override fun onResponse(response: com.squareup.okhttp.Response?) {

                        mHandler.post {
                            var r = response?.body()?.string()
                            var obj = JSONArray(r)
                            val item = JSONArray(obj.get(0).toString())
                            name.text = item[0].toString().capitalize()
                            phno.text = item[1].toString()


                        }
                    }
                }
                )

            }

            runBlocking<Unit> {

                val x = async {
                    dosoemthingAll("https://dbmsibm.herokuapp.com/api/publisherret?email='$email'")
                }
                x.await()

            }
        }


    }

}
package com.ramotion.navigationtoolbar.example.View

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ramotion.navigationtoolbar.example.MainActivity
import com.ramotion.navigationtoolbar.example.R
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import java.io.IOException

//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase


class register_activity : AppCompatActivity() {
    var mHandler =  Handler(Looper.getMainLooper());
    private val client = OkHttpClient()
    private var logo: ImageView? = null
    private var joinus: ImageView? = null
    private var username: AutoCompleteTextView? = null
    private var email: AutoCompleteTextView? = null
    private var password: AutoCompleteTextView? = null
    private var signup: Button? = null
    private var signin: TextView? = null
    private var progressDialog: ProgressDialog? = null
    private var firebaseAuth: FirebaseAuth? = null
//    private var firebaseDatabase: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        initializeGUI()
        signup!!.setOnClickListener {
            val inputName = username!!.text.toString().trim { it <= ' ' }
            val inputPw = password!!.text.toString().trim { it <= ' ' }
            val inputEmail = email!!.text.toString().trim { it <= ' ' }
            if (validateInput(inputName, inputPw, inputEmail)) registerUser(inputName, inputPw, inputEmail)
        }
        signin!!.setOnClickListener { startActivity(Intent(this@register_activity, MainActivity::class.java))
            finishAffinity()}
    }

    private fun initializeGUI() {
        logo = findViewById(R.id.ivRegLogo)
        joinus = findViewById(R.id.ivJoinUs)
        username = findViewById(R.id.atvUsernameReg)
        email = findViewById(R.id.atvEmailReg)
        password = findViewById(R.id.atvPasswordReg)
        signin = findViewById(R.id.tvSignIn)
        signup = findViewById(R.id.btnSignUp)
        progressDialog = ProgressDialog(this)
        firebaseAuth = FirebaseAuth.getInstance()

    }

    private fun registerUser(inputName: String, inputPw: String, inputEmail: String) {

        val url = "https://dbmsibm.herokuapp.com/api/publisher?name='$inputName'&email='$inputEmail'&pwd='$inputPw'&phno='9012343215'"
        progressDialog!!.setMessage("Verificating...")
        progressDialog!!.show()
        firebaseAuth!!.createUserWithEmailAndPassword(inputEmail, inputPw).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val request2 = Request.Builder()
                        .url(url)
                        .build()
                client.newCall(request2).enqueue(object : Callback {
                    override fun onFailure(request: Request?, e: IOException?) {
                        println("FAIL AGIAN")
                    }

                    override fun onResponse(response: com.squareup.okhttp.Response?) {
                        mHandler.post {
                            Toast.makeText(this@register_activity, "Registration Success", Toast.LENGTH_SHORT)
                            startActivity(Intent(this@register_activity, MainActivity::class.java))
                            finishAffinity()
                        }
                    }})
                progressDialog!!.dismiss()
              } else {
                progressDialog!!.dismiss()
                Toast.makeText(this@register_activity, "Please Enter Valid Email and Password", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun validateInput(inName: String, inPw: String, inEmail: String): Boolean {
        val emailPattern= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val pwdPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~\$^+=<>]).{8,20}\$"

        if (inName.isEmpty()) {
            username!!.error = "Username is empty."
            return false
        }
        if(inName.length < 3){
            username!!.error = "Username should be minimum 3 characters"
            return false
        }

        if (inPw.isEmpty()) {
            password!!.error = "Password is empty."
            return false
        }
        if (inEmail.isEmpty()) {
            email!!.error = "Email is empty."
            return false
        }
        if (!inEmail.matches(emailPattern.toRegex()))
        {
            email!!.error = "Email should be of the format xxx@yy.zz"
            return false
        }

        if (!inPw.matches(pwdPattern.toRegex())) {
            password!!.error = "Password should contain at least one Capital letter and a special symbol"
            return false
        }
        return true
    }
}
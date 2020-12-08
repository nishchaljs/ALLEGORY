package com.ramotion.navigationtoolbar.example

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase


class register_activity : AppCompatActivity() {
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
        signin!!.setOnClickListener { startActivity(Intent(this@register_activity, MainActivity::class.java)) }
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
        progressDialog!!.setMessage("Verificating...")
        progressDialog!!.show()
        firebaseAuth!!.createUserWithEmailAndPassword(inputEmail, inputPw).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                progressDialog!!.dismiss()
                Toast.makeText(this@register_activity, "You've been registered successfully.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@register_activity, MainActivity::class.java))
            } else {
                progressDialog!!.dismiss()
                Toast.makeText(this@register_activity, "Email already exists.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun validateInput(inName: String, inPw: String, inEmail: String): Boolean {
        if (inName.isEmpty()) {
            username!!.error = "Username is empty."
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
        return true
    }
}
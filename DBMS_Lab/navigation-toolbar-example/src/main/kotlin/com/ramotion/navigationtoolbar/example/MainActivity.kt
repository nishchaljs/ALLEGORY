package com.ramotion.navigationtoolbar.example

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private val itemCount = 40
    private var progressDialog: ProgressDialog? = null
    private val dataSet = ExampleDataSet()
    private var user: FirebaseUser? = null
    private var isExpanded = true
    private var prevAnchorPosition = 0
    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private lateinit var firebaseAuth: FirebaseAuth
    private  var firebaseAuth1: FirebaseAuth? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        user = firebaseAuth1?.currentUser

        if (user != null) {
            finish()
            startActivity(Intent(this, login_activity::class.java))
        }

        val login = findViewById<Button>(R.id.btnSignIn)
        val email = findViewById<AutoCompleteTextView>(R.id.atvEmailLog)
        val pwd = findViewById<AutoCompleteTextView>(R.id.atvPasswordLog)
        val signup = findViewById<TextView>(R.id.tvSignIn)
        val google = findViewById<SignInButton>(R.id.ivGoogle)
        val anonymous = findViewById<TextView>(R.id.normalSignIn)

        anonymous.setOnClickListener{
            var auth = Firebase.auth
            progressDialog?.setMessage("Verificating...")
            progressDialog?.show()
            auth.signInAnonymously()
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            progressDialog?.dismiss()
                            Toast.makeText(this@MainActivity, "Welcome USer", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@MainActivity,login_activity::class.java))
                        } else {
                            progressDialog?.dismiss()
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                        }

                        // ...
                    }
        }


        configureGoogleSignIn()
        progressDialog = ProgressDialog(this)
        google.setOnClickListener {

            signIn()
        }
        firebaseAuth = FirebaseAuth.getInstance()


        signup.setOnClickListener{
            startActivity(Intent(this@MainActivity,register_activity::class.java))
        }

        login.setOnClickListener { view ->
            val inEmail: String = email.getText().toString()
            val inPassword: String = pwd.getText().toString()

            if (validateInput(inEmail, inPassword)) {
                signUser(inEmail, inPassword)
            }
        }


    }



    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {

                startActivity(Intent(this@MainActivity,login_activity::class.java))
            } else {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun signIn() {
        mGoogleSignInClient.signOut()
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    fun validateInput(inemail: String, inpassword: String): Boolean {
        val login = findViewById<Button>(R.id.btnSignIn)
        val email = findViewById<AutoCompleteTextView>(R.id.atvEmailLog)
        val pwd = findViewById<AutoCompleteTextView>(R.id.atvPasswordLog)
        if (inemail.isEmpty()) {
            email.setError("Email field is empty.")
            return false
        }
        if (inpassword.isEmpty()) {
            pwd.setError("Password is empty.")
            return false
        }
        return true
    }

     fun signUser(email: String, password: String) {

         // ...
// Initialize Firebase Auth
         var auth: FirebaseAuth = Firebase.auth

        progressDialog?.setMessage("Verificating...")
        progressDialog?.show()
         println("Inside Signin")
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            println("Inside Signin _ but no")
            if (task.isSuccessful) {
                progressDialog?.dismiss()
                Toast.makeText(this@MainActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@MainActivity,login_activity::class.java))
            } else {
                progressDialog?.dismiss()
                Toast.makeText(this@MainActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

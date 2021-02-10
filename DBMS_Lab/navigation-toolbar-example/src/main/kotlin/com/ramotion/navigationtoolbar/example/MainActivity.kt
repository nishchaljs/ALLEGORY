package com.ramotion.navigationtoolbar.example

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.ramotion.navigationtoolbar.example.Model.ExampleDataSet
import com.ramotion.navigationtoolbar.example.Model.publicationModel
import com.ramotion.navigationtoolbar.example.View.register_activity
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import org.json.JSONArray
import java.io.IOException


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
    private lateinit var mDatabase: DatabaseReference
    private lateinit var mMessageReference: DatabaseReference
    lateinit  var publist: MutableList<publicationModel>
    private val auth: FirebaseAuth? = null
    private val client = OkHttpClient()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = firebaseAuth1?.currentUser
        println("User is $user")
        var auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            startActivity(Intent(this, login_activity::class.java))
            finishAffinity()

        }
        setContentView(R.layout.activity_login)

        val login = findViewById<Button>(R.id.btnSignIn)
        val email = findViewById<AutoCompleteTextView>(R.id.atvEmailLog)
        val pwd = findViewById<AutoCompleteTextView>(R.id.atvPasswordLog)
        val signup = findViewById<TextView>(R.id.tvSignIn)
        val google = findViewById<SignInButton>(R.id.ivGoogle)
        val anonymous = findViewById<TextView>(R.id.normalSignIn)

        //Anonymous Sign In

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
                            finishAffinity()
                        } else {
                            progressDialog?.dismiss()
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                        }

                        // ...
                    }
        }

        //      Register User
        signup.setOnClickListener{
            startActivity(Intent(this@MainActivity, register_activity::class.java))
        }

        // Login User
        login.setOnClickListener { view ->
            val inEmail: String = email.text.toString()
            val inPassword: String = pwd.text.toString()

            if (validateInput(inEmail, inPassword)) {
                signUser(inEmail, inPassword)
            }
        }


        configureGoogleSignIn()
        progressDialog = ProgressDialog(this)
        google.setOnClickListener {

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
                            finishAffinity()
                        } else {
                            progressDialog?.dismiss()
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                        }

                        // ...
                    }

            //signIn()
        }
        firebaseAuth = FirebaseAuth.getInstance()


//some code
        class data{
    var name: String?=null
    var auhtor: String?=null
    var type: String?=null
}

//get reference to the "users" node

        mDatabase = FirebaseDatabase.getInstance().getReference("publication")
       mMessageReference = FirebaseDatabase.getInstance().getReference("publication")
       publist = mutableListOf()
        mMessageReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(h in snapshot.children){
                        val data = h.getValue(publicationModel::class.java)
                        publist.add(data!!)
                    }
                }
            }

        })

        run("https://dbmsibm.herokuapp.com/api/allpoems")

    }

    fun run(url: String) {
        val request = Request.Builder()
                .url(url)
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request?, e: IOException?) {println("FAIL AGIAN")}

            override fun onResponse(response: com.squareup.okhttp.Response?) {
                var r = response?.body()?.string()
                var obj = JSONArray(r)
                var q = JSONArray(obj.get(0).toString())[0]
                println("IBM Data ${q}")
            }
        })
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
                finishAffinity()
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
        val emailPattern= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val pwdPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~\$^+=<>]).{8,20}\$"
        if (inemail.isEmpty()) {
            email.error = "Email field is empty."
            return false
        }
        if (!inemail.matches(emailPattern.toRegex()))
        {
            email.error = "Email should be of the format xxx@yy.zz"
        }

        if (inpassword.isEmpty()) {
            pwd.error = "Password is empty."
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
                finishAffinity()
            } else {
                progressDialog?.dismiss()
                Toast.makeText(this@MainActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

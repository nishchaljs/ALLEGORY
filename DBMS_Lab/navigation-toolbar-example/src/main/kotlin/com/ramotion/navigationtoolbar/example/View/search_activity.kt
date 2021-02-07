package com.ramotion.navigationtoolbar.example.View

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ramotion.foldingcell.FoldingCell
import com.ramotion.navigationtoolbar.example.FoldingCellListAdapter
import com.ramotion.navigationtoolbar.example.Item
import com.ramotion.navigationtoolbar.example.Model.ExampleDataSet
import com.ramotion.navigationtoolbar.example.Model.publicationModel

import com.ramotion.navigationtoolbar.example.R
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.search_activity.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import java.io.IOException

import java.util.*
import kotlin.collections.ArrayList

class search_activity: AppCompatActivity() {
    private var itemCount = 40
    private val dataSet = ExampleDataSet()
    val names: ArrayList<String> = ArrayList()
    private var progressDialog: ProgressDialog? = null
    val client = OkHttpClient()
    var mHandler = Handler(Looper.getMainLooper());
    // prepare elements to display
    val items: ArrayList<publicationModel> = ArrayList()



    suspend fun dosoemthingAll(url: String) {

        progressDialog?.setMessage("Loading...")
        progressDialog?.show()
        val request1 = Request.Builder()
                .url(url)
                .build()
        client.newCall(request1).enqueue(object : Callback {
            override fun onFailure(request: Request?, e: IOException?) {
                progressDialog?.dismiss()
                println("FAIL AGIAN")
            }

            override fun onResponse(response: com.squareup.okhttp.Response?) {

                mHandler.post {
                    println("Inside this")
                    var r = response?.body()?.string()
                    var obj = JSONArray(r)
                    for (i in 0 until obj.length()) {
                        val item = obj.get(i).toString()
                        names.add(item)
                    }
                    progressDialog?.dismiss()
                }
            }
        }
        )

    }

    suspend fun query(url: String, theListView: ListView ) {

        progressDialog?.setMessage("Loading...")
        progressDialog?.show()
        val request1 = Request.Builder()
                .url(url)
                .build()
        client.newCall(request1).enqueue(object : Callback {
            override fun onFailure(request: Request?, e: IOException?) {
                progressDialog?.dismiss()
                println("FAIL AGIAN")
            }

            override fun onResponse(response: com.squareup.okhttp.Response?) {

                mHandler.post {
                    println("Inside this")
                    var r = response?.body()?.string()
                    var obj = JSONArray(r)
                    for (i in 0 until obj.length()) {
                        var m = publicationModel()
                        val item = JSONArray(obj.get(i).toString())
                        m.name = item[1].toString()
                        m.desc = ""
                        m.author = item[2].toString()
                        m.genre = ""
                        m.id = item[0].toString()
                        m.time = item[3].toString()
                        m.date = item[4].toString()
                        m.type = ""
                        m.img = ""
                        m.email = ""
                        m.reads = "1"
                        items.add(m)
                        println("Look at the ${m.name}")
                    }
                    progressDialog?.dismiss()

                    if(items.isEmpty()){
                        Toast.makeText(this@search_activity, "NO PUBLICATIONS FOUND", Toast.LENGTH_LONG).show()
                    }

                    // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
                    val adapter = FoldingCellListAdapter(this@search_activity, items)

                    // add default btn handler for each request btn on each item if custom handler not found
                    adapter.defaultRequestBtnClickListener= View.OnClickListener { Toast.makeText(applicationContext, "DEFAULT HANDLER FOR ALL BUTTONS", Toast.LENGTH_SHORT).show() }

                    // set elements to adapter
                    theListView.adapter = adapter

                    // set on click event listener to list view
                    theListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, pos, l -> // toggle clicked cell state
                        (view as FoldingCell).toggle(false)
                        // register in adapter that state for selected cell is toggled
                        adapter.registerToggle(pos)
                    }
                }
            }
        }
        )

    }


    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.search_activity)
        progressDialog = ProgressDialog(this)


        val theListView = findViewById<ListView>(R.id.listview)

        runBlocking<Unit> {

            val x = async {
                dosoemthingAll("https://dbmsibm.herokuapp.com/api/all")
            }
            x.await()

        }

        val autoadapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.select_dialog_item, names)
        val actv = findViewById(R.id.searchView) as AutoCompleteTextView
        actv.threshold = 1 //will start working from first character
        actv.setAdapter(autoadapter) //setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.RED)

        search_button.setOnClickListener{

            items.clear()

            runBlocking<Unit> {

                val x = async {
                    var url = "https://dbmsibm.herokuapp.com/api/search?key='" + actv.text.toString() + "%'"
                    println("This is the URL:" + url)
                    query(url, theListView)
                }
                x.await()

            }

        }

       }

    }


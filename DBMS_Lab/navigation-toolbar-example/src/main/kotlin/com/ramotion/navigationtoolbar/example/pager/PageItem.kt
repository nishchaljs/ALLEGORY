package com.ramotion.navigationtoolbar.example.pager

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.ramotion.foldingcell.FoldingCell
import com.ramotion.navigationtoolbar.example.*
import com.ramotion.navigationtoolbar.example.Model.PageDataSet
import com.ramotion.navigationtoolbar.example.Model.publicationModel
import com.ramotion.navigationtoolbar.example.View.DeleteActivity
import com.ramotion.navigationtoolbar.example.View.update
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import java.io.IOException
import java.util.*


sealed class PageItem(view: View) : RecyclerView.ViewHolder(view) {
    fun clearContent() {}
}

class ItemUser(view: View, context: Context) : PageItem(view) {
    //private val avatar = view.findViewById<ImageView>(R.id.avatar)

    private val unfoldedIndexes = HashSet<Int>()
    var publist: MutableList<publicationModel> = mutableListOf()
    private val client = OkHttpClient()
    private  val read = view.findViewById<TextView>(R.id.content_read_btn)
    private val author = view.findViewById<TextView>(R.id.content_name_view)
    private val type = view.findViewById<TextView>(R.id.type)
    private val desc = view.findViewById<TextView>(R.id.description)
    private val genre = view.findViewById<TextView>(R.id.genre)
    private val name =   view.findViewById<TextView>(R.id.name)
    private val name2 =   view.findViewById<TextView>(R.id.titleTextView)
    private val desc2 = view.findViewById<TextView>(R.id.detailsTextView)

    private val Update = view.findViewById<TextView>(R.id.content_update_btn)
    private val delete = view.findViewById<TextView>(R.id.content_delete_btn)
    private val blank1 = view.findViewById<TextView>(R.id.blank1)
    private val blank2 = view.findViewById<TextView>(R.id.blank2)

    private  val cell = view.findViewById<FoldingCell>(R.id.cell)
    private  val card = view.findViewById<LinearLayout>(R.id.card)

    private val img = view.findViewById<ImageView>(R.id.postImageView)
    private val auth_img = view.findViewById<ImageView>(R.id.authorImageView)
    private val inside_img = view.findViewById<ImageView>(R.id.insideimg)

    private val time = view.findViewById<TextView>(R.id.content_delivery_time)
    private val date = view.findViewById<TextView>(R.id.content_delivery_date)
    private val reads = view.findViewById<TextView>(R.id.reads)

    //private val status = view.findViewById<TextView>(R.id.status)

    @Parcelize
    data class public(
            val name: String,
            val desc: String,
            val author: String,
            val type: String,
            val genre: String
    ) : Parcelable






    fun setContent(content: PageDataSet.ItemData, position: Int, page:Int, context: Context) {
        var auth = Firebase.auth
        var user = auth.currentUser
        var anon = user?.isAnonymous()
        lateinit var mDatabase: DatabaseReference
        lateinit var mMessageReference: DatabaseReference
        var mHandler =  Handler(Looper.getMainLooper());
        var usr = FirebaseAuth.getInstance().currentUser
        var email = usr?.email;
        var im_s = arrayOf("story1","story2","story3","story4","poem1","poem2","poem3","pic1","pic2","pic3","pic4","pic5","pic6","pic7")
        var im_auth = arrayOf("aaron_bradley","barry_allen","bella_holmes","caroline_shaw",
                "connor_graham","deann_hunt","ella_cole","jayden_shaw","jerry_carrol", "lena_lucas","leonrd_kim","marc_baker","marjorie_ellis", "mattew_jordan")

        im_s.shuffle()
        im_auth.shuffle()


            Update.visibility =  View.GONE
            delete.visibility = View.GONE
            blank1.visibility = View.VISIBLE
            blank2.visibility = View.VISIBLE

        suspend fun dosoemthingAll(url1:String,url2:String, typ:String){

            val request1 = Request.Builder()
                    .url(url1)
                    .build()
            client.newCall(request1).enqueue(object : Callback {
                override fun onFailure(request: Request?, e: IOException?) {
                    println("FAIL AGIAN")
                }

                override fun onResponse(response: com.squareup.okhttp.Response?) {

                    var r = response?.body()?.string()
                    var obj = JSONArray(r)
                    for (i in 0 until obj.length()) {
                        
                        var m = publicationModel()
                        val item = JSONArray(obj.get(i).toString())
                        m.name = item[1].toString()
                        m.desc = item[2].toString()
                        m.author = item[3].toString()
                        m.genre = item[4].toString()
                        m.id = item[0].toString()
                        m.type = typ
                        m.time = item[5].toString()
                        m.date = item[6].toString()
                        m.img = im_s[position%im_s.size]
                        m.email = item[8].toString()
                        m.reads = item[7].toString()
                        publist.add(m)
                        println("DAta here ${m.name}")

                    }

        }
            }
            )

            val request2 = Request.Builder()
                    .url(url2)
                    .build()
            client.newCall(request2).enqueue(object : Callback {
                override fun onFailure(request: Request?, e: IOException?) {
                    println("FAIL AGIAN")
                }

                override fun onResponse(response: com.squareup.okhttp.Response?) {

                    var r = response?.body()?.string()
                    var obj = JSONArray(r)
                    for (i in 0 until obj.length()) {
                        var m = publicationModel()
                        val item = JSONArray(obj.get(i).toString())
                        m.name = item[1].toString()
                        m.desc = item[2].toString()
                        m.author = item[3].toString()
                        m.genre = item[4].toString()
                        m.id = item[0].toString()
                        m.time = item[5].toString()
                        m.date = item[6].toString()
                        m.type = typ
                        m.img = im_s[position%im_s.size]
                        m.email = item[8].toString()
                        m.reads = item[7].toString()
                        publist.add(m)
                        println("DAta here ${m.name}")
                    }

                    mHandler.post {

                        if(publist[position%publist.size].email == email.toString()){
                            var typ = " "
                            if(publist[position%publist.size].type == "Poem")
                            {
                                typ="poem"
                            }
                            else{
                                typ ="story"
                            }
                            blank1.visibility = View.GONE
                            blank2.visibility = View.GONE
                            Update.visibility = View.VISIBLE
                            delete.visibility = View.VISIBLE


                            Update.setOnClickListener {
                                context.startActivity(Intent(context, update::class.java)
                                        .putExtra("pubID",publist[position%publist.size].id)
                                        .putExtra("typ", typ)
                                        .putExtra("desc",publist[position%publist.size].desc)
                                        .putExtra("title", publist[position%publist.size].name)
                                        .putExtra("author", publist[position%publist.size].author)
                                        .putExtra("genre",publist[position%publist.size].genre))

                            }

                            delete.setOnClickListener{
                                context.startActivity(Intent(context, DeleteActivity::class.java)
                                        .putExtra("pubID",publist[position%publist.size].id)
                                        .putExtra("typ", typ)
                                        .putExtra("desc",publist[position%publist.size].desc)
                                        .putExtra("title", publist[position%publist.size].name)
                                        .putExtra("author", publist[position%publist.size].author)
                                        .putExtra("genre",publist[position%publist.size].genre))
                            }

                        }

                        if(publist[position%publist.size].type == "Poem"){
                            read.setOnClickListener {

                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://static1.squarespace.com/static/51f912e6e4b0cc5aa449f476/t/58502a43b8a79bf9d5651cdd/1481648710707/Poetry+for+Perseverance.pdf")))
                            }

                        }
                        else{
                            read.setOnClickListener {

                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/1Ud8T6pEWcUIIUX-pKiBWFIhJaJi0yHsT/view?usp=sharing")))
                            }
                        }

                        if (page % 7 == 2) {
                                println("Publist size before ${publist[position % publist.size].author}")
                                author.text = publist[position % publist.size].author
                                name.text = publist[position % publist.size].name
                                name2.text = publist[position % publist.size].name
                                type.text = publist[position % publist.size].type
                                desc.text = publist[position % publist.size].desc
                                desc2.text = publist[position % publist.size].desc
                                genre.text = publist[position % publist.size].genre
                                date.text = publist[position % publist.size].date
                                time.text = publist[position % publist.size].time
                                reads.text = publist[position % publist.size].reads

                                // Display an image into image view from assets folder
                                var id = context.resources.getIdentifier(publist[position % publist.size].img, "drawable", context.packageName)
                                img.setImageResource(id)

                            id = context.resources.getIdentifier(im_auth[position % im_auth.size], "drawable", context.packageName)
                            auth_img.setImageResource(id)
                            inside_img.setImageResource(id)



                            }
                        }
                    }

                }

            )


        }


        suspend fun dosoemthing(url:String, typ:String) {

            val request = Request.Builder()
                    .url(url)
                    .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(request: Request?, e: IOException?) {
                    println("FAIL AGIAN")
                }

                override fun onResponse(response: com.squareup.okhttp.Response?) {

                    var r = response?.body()?.string()
                    var obj = JSONArray(r)
                    for (i in 0 until obj.length()) {
                        var m = publicationModel()
                        val item = JSONArray(obj.get(i).toString())
                        m.name = item[1].toString()
                        m.desc = item[2].toString()
                        m.author = item[3].toString()
                        m.genre = item[4].toString()
                        m.id = item[0].toString()
                        m.time = item[5].toString()
                        m.date = item[6].toString()
                        m.type = typ
                        m.img = im_s[position%im_s.size]
                        m.reads = item[7].toString()
                        publist.add(m)
                        m.email = item[8].toString()
                        println("DAta here ${m.name}")
                    }


                    mHandler.post {

                        if(publist[position%publist.size].email == email.toString()){
                            var typ = " "
                            if(publist[position%publist.size].type == "Poem")
                            {
                                typ="poem"
                            }
                            else{
                                typ ="story"
                            }
                            blank1.visibility = View.GONE
                            blank2.visibility = View.GONE
                            Update.visibility = View.VISIBLE
                            delete.visibility = View.VISIBLE


                            Update.setOnClickListener {
                                context.startActivity(Intent(context, update::class.java)
                                        .putExtra("pubID",publist[position%publist.size].id)
                                        .putExtra("typ", typ)
                                        .putExtra("desc",publist[position%publist.size].desc)
                                        .putExtra("title", publist[position%publist.size].name)
                                        .putExtra("author", publist[position%publist.size].author)
                                        .putExtra("genre",publist[position%publist.size].genre))

                            }

                            delete.setOnClickListener{
                                context.startActivity(Intent(context, DeleteActivity::class.java)
                                        .putExtra("pubID",publist[position%publist.size].id)
                                        .putExtra("typ", typ)
                                        .putExtra("desc",publist[position%publist.size].desc)
                                        .putExtra("title", publist[position%publist.size].name)
                                        .putExtra("author", publist[position%publist.size].author)
                                        .putExtra("genre",publist[position%publist.size].genre))
                            }

                        }

                        if(publist[position%publist.size].type == "Poem"){
                        read.setOnClickListener {

                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://static1.squarespace.com/static/51f912e6e4b0cc5aa449f476/t/58502a43b8a79bf9d5651cdd/1481648710707/Poetry+for+Perseverance.pdf")))
                        }

                    }
                    else{
                        read.setOnClickListener {

                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/1Ud8T6pEWcUIIUX-pKiBWFIhJaJi0yHsT/view?usp=sharing")))
                        }
                    }

                        if (page % 7 == 0) {
                            if (publist[position % publist.size].type == "Story") {
                                println("Publist size before ${publist[position % publist.size].author}")
                                author.text = publist[position % publist.size].author
                                name.text = publist[position % publist.size].name
                                name2.text = publist[position % publist.size].name
                                type.text = publist[position % publist.size].type
                                desc.text = publist[position % publist.size].desc
                                desc2.text = publist[position % publist.size].desc
                                genre.text = publist[position % publist.size].genre
                                date.text = publist[position % publist.size].date
                                time.text = publist[position % publist.size].time
                                reads.text = publist[position % publist.size].reads

                                // Display an image into image view from assets folder
                                var id = context.resources.getIdentifier(publist[position % publist.size].img, "drawable", context.packageName)
                                img.setImageResource(id)

                                id = context.resources.getIdentifier(im_auth[position % im_auth.size], "drawable", context.packageName)
                                auth_img.setImageResource(id)
                                inside_img.setImageResource(id)



                            } else {
                                card.visibility = View.GONE
                                cell.visibility = View.GONE

                            }
                        }

                        else if(page%7==1){

                        if(publist[position%publist.size].type == "Poem"){
                            author.text= publist[position%publist.size].author
                            name.text= publist[position%publist.size].name
                            name2.text= publist[position%publist.size].name
                            type.text= publist[position%publist.size].type
                            desc.text= publist[position%publist.size].desc
                            desc2.text= publist[position%publist.size].desc
                            genre.text= publist[position%publist.size].genre
                            date.text = publist[position % publist.size].date
                            time.text = publist[position % publist.size].time
                            reads.text = publist[position % publist.size].reads

                            var id = context.resources.getIdentifier(publist[position%publist.size].img, "drawable", context.packageName)
                            img.setImageResource(id)

                            id = context.resources.getIdentifier(im_auth[position % im_auth.size], "drawable", context.packageName)
                            auth_img.setImageResource(id)
                            inside_img.setImageResource(id)


                        }
                        else{
                            card.visibility = View.GONE
                            cell.visibility=View.GONE
                        }
                    }
                    else if(page%7==2){
                        author.text= publist[position%publist.size].author
                        name.text= publist[position%publist.size].name
                        name2.text= publist[position%publist.size].name
                        type.text= publist[position%publist.size].type
                        desc.text= publist[position%publist.size].desc
                        desc2.text= publist[position%publist.size].desc
                        genre.text= publist[position%publist.size].genre
                            date.text = publist[position % publist.size].date
                            time.text = publist[position % publist.size].time
                            reads.text = publist[position % publist.size].reads

                        var id = context.resources.getIdentifier(publist[position%publist.size].img, "drawable", context.packageName)
                        img.setImageResource(id)

                            id = context.resources.getIdentifier(im_auth[position % im_auth.size], "drawable", context.packageName)
                            auth_img.setImageResource(id)
                            inside_img.setImageResource(id)


                    }
                    else if(page%7==3){
                        author.text= publist[position%publist.size].author
                        name.text= publist[position%publist.size].name
                        name2.text= publist[position%publist.size].name
                        type.text= publist[position%publist.size].type
                        desc.text= publist[position%publist.size].desc
                        desc2.text= publist[position%publist.size].desc
                        genre.text= publist[position%publist.size].genre
                            date.text = publist[position % publist.size].date
                            time.text = publist[position % publist.size].time
                            reads.text = publist[position % publist.size].reads

                        var id = context.resources.getIdentifier(publist[position%publist.size].img, "drawable", context.packageName)
                        img.setImageResource(id)

                            id = context.resources.getIdentifier(im_auth[position % im_auth.size], "drawable", context.packageName)
                            auth_img.setImageResource(id)
                            inside_img.setImageResource(id)

                    }
                    else if(page%7==4){
                        author.text= publist[position%publist.size].author
                        name.text= publist[position%publist.size].name
                        name2.text= publist[position%publist.size].name
                        type.text= publist[position%publist.size].type
                        desc.text= publist[position%publist.size].desc
                        desc2.text= publist[position%publist.size].desc
                        genre.text= publist[position%publist.size].genre
                            date.text = publist[position % publist.size].date
                            time.text = publist[position % publist.size].time
                            reads.text = publist[position % publist.size].reads

                        var id = context.resources.getIdentifier(publist[position%publist.size].img, "drawable", context.packageName)
                        img.setImageResource(id)

                            id = context.resources.getIdentifier(im_auth[position % im_auth.size], "drawable", context.packageName)
                            auth_img.setImageResource(id)
                            inside_img.setImageResource(id)



                    }

                        else if(page%7==5){
                            author.text= publist[position%publist.size].author
                            name.text= publist[position%publist.size].name
                            name2.text= publist[position%publist.size].name
                            type.text= publist[position%publist.size].type
                            desc.text= publist[position%publist.size].desc
                            desc2.text= publist[position%publist.size].desc
                            genre.text= publist[position%publist.size].genre
                            date.text = publist[position % publist.size].date
                            time.text = publist[position % publist.size].time
                            reads.text = publist[position % publist.size].reads

                            var id = context.resources.getIdentifier(publist[position%publist.size].img, "drawable", context.packageName)
                            img.setImageResource(id)

                            id = context.resources.getIdentifier(im_auth[position % im_auth.size], "drawable", context.packageName)
                            auth_img.setImageResource(id)
                            inside_img.setImageResource(id)


                        }

                        else if(page%7==6){
                            author.text= publist[position%publist.size].author
                            name.text= publist[position%publist.size].name
                            name2.text= publist[position%publist.size].name
                            type.text= publist[position%publist.size].type
                            desc.text= publist[position%publist.size].desc
                            desc2.text= publist[position%publist.size].desc
                            genre.text= publist[position%publist.size].genre
                            date.text = publist[position % publist.size].date
                            time.text = publist[position % publist.size].time
                            reads.text = publist[position % publist.size].reads

                            var id = context.resources.getIdentifier(publist[position%publist.size].img, "drawable", context.packageName)
                            img.setImageResource(id)

                            id = context.resources.getIdentifier(im_auth[position % im_auth.size], "drawable", context.packageName)
                            auth_img.setImageResource(id)
                            inside_img.setImageResource(id)


                        }

                    }
                }
            })
        }

        runBlocking<Unit>{

            val x = async {
                if(page%7 ==0)
                dosoemthing("https://dbmsibm.herokuapp.com/api/allstories","Story")
                else if(page%7 ==1)
                    dosoemthing("https://dbmsibm.herokuapp.com/api/allpoems","Poem")
                else if(page%7 ==2)
                    dosoemthingAll("https://dbmsibm.herokuapp.com/api/allpoems","https://dbmsibm.herokuapp.com/api/allstories","All")
                else if(page%7 ==3)
                    dosoemthing("https://dbmsibm.herokuapp.com/api/toprateds","Top Rated")
                else if(page%7 ==4)
                    dosoemthing("https://dbmsibm.herokuapp.com/api/mostrecents","Most Recent")
                else if(page%7 ==5)
                    dosoemthing("https://dbmsibm.herokuapp.com/api/topratedp","Top Rated")
                else if(page%7 ==6)
                    dosoemthing("https://dbmsibm.herokuapp.com/api/mostrecentp","Most Recent")
            }
            x.await()

        }



//
//        mMessageReference = FirebaseDatabase.getInstance().getReference("publication")
//
//        mMessageReference.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                if(snapshot.exists()){
//                    for(h in snapshot.children){
//                        val data = h.getValue(publicationModel::class.java)
//                        publist.add(data!!)
//                    }
//
//                    println("Publist size after ${publist.size}")
//
//                    if(page%5>2){
//                        publist.shuffle()
//                    }
//
//
//
//
//
//                    if(publist[position%publist.size].type == "Poem"){
//                        read.setOnClickListener {
//
//                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://static1.squarespace.com/static/51f912e6e4b0cc5aa449f476/t/58502a43b8a79bf9d5651cdd/1481648710707/Poetry+for+Perseverance.pdf")))
//                        }
//
//                    }
//                    else{
//                        read.setOnClickListener {
//
//                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://shortstorylines.com/wp-content/uploads/2020/01/the-boy-who-cried-wolf-pdf.pdf")))
//                        }
//                    }
//
//                    if(page%5==0){
//                             if(publist[position%publist.size].type == "Story")    {
//
//                                 author.text= publist[position%publist.size].author
//                                 name.text= publist[position%publist.size].name
//                                 name2.text= publist[position%publist.size].name
//                                 type.text= publist[position%publist.size].type
//                                 desc.text= publist[position%publist.size].desc
//                                 desc2.text= publist[position%publist.size].desc
//                                 genre.text= publist[position%publist.size].genre
//
//                                 // Display an image into image view from assets folder
//                                 val id = context.resources.getIdentifier(publist[position%publist.size].img, "drawable", context.packageName)
//                                 img.setImageResource(id)
//
//
//                                 Update.setOnClickListener{
//                                     context.startActivity(Intent(context, update::class.java))
////                                             .putExtra("ItemData",public(publist[position%publist.size].name,
////                                             publist[position%publist.size].author, publist[position%publist.size].desc,publist[position%publist.size].type,publist[position%publist.size].genre )))
//
//                                 }
//
//                             }
//                        else
//                             {
//                                 card.visibility = View.GONE
//                                 cell.visibility=View.GONE
//
//                             }
//                    }
//                    else if(page%5==1){
//
//                        if(publist[position%publist.size].type == "Poem"){
//                            author.text= publist[position%publist.size].author
//                            name.text= publist[position%publist.size].name
//                            name2.text= publist[position%publist.size].name
//                            type.text= publist[position%publist.size].type
//                            desc.text= publist[position%publist.size].desc
//                            desc2.text= publist[position%publist.size].desc
//                            genre.text= publist[position%publist.size].genre
//
//                            val id = context.resources.getIdentifier(publist[position%publist.size].img, "drawable", context.packageName)
//                            img.setImageResource(id)
//
//                            Update.setOnClickListener{
//                                context.startActivity(Intent(context, update::class.java))
////                                             .putExtra("ItemData",public(publist[position%publist.size].name,
////                                             publist[position%publist.size].author, publist[position%publist.size].desc,publist[position%publist.size].type,publist[position%publist.size].genre )))
//
//                            }
//                        }
//                        else{
//                            card.visibility = View.GONE
//                            cell.visibility=View.GONE
//                        }
//                    }
//                    else if(page%5==2){
//                        author.text= publist[position%publist.size].author
//                        name.text= publist[position%publist.size].name
//                        name2.text= publist[position%publist.size].name
//                        type.text= publist[position%publist.size].type
//                        desc.text= publist[position%publist.size].desc
//                        desc2.text= publist[position%publist.size].desc
//                        genre.text= publist[position%publist.size].genre
//
//                        val id = context.resources.getIdentifier(publist[position%publist.size].img, "drawable", context.packageName)
//                        img.setImageResource(id)
//
//                        Update.setOnClickListener{
//                            context.startActivity(Intent(context, update::class.java))
////                                             .putExtra("ItemData",public(publist[position%publist.size].name,
////                                             publist[position%publist.size].author, publist[position%publist.size].desc,publist[position%publist.size].type,publist[position%publist.size].genre )))
//
//                        }
//                    }
//                    else if(page%5==3){
//                        author.text= publist[position%publist.size].author
//                        name.text= publist[position%publist.size].name
//                        name2.text= publist[position%publist.size].name
//                        type.text= publist[position%publist.size].type
//                        desc.text= publist[position%publist.size].desc
//                        desc2.text= publist[position%publist.size].desc
//                        genre.text= publist[position%publist.size].genre
//
//                        val id = context.resources.getIdentifier(publist[position%publist.size].img, "drawable", context.packageName)
//                        img.setImageResource(id)
//
//                        Update.setOnClickListener{
//                            context.startActivity(Intent(context, update::class.java))
////                                             .putExtra("ItemData",public(publist[position%publist.size].name,
////                                             publist[position%publist.size].author, publist[position%publist.size].desc,publist[position%publist.size].type,publist[position%publist.size].genre )))
//
//                        }
//                    }
//                    else if(page%5==4){
//                        author.text= publist[position%publist.size].author
//                        name.text= publist[position%publist.size].name
//                        name2.text= publist[position%publist.size].name
//                        type.text= publist[position%publist.size].type
//                        desc.text= publist[position%publist.size].desc
//                        desc2.text= publist[position%publist.size].desc
//                        genre.text= publist[position%publist.size].genre
//
//                        val id = context.resources.getIdentifier(publist[position%publist.size].img, "drawable", context.packageName)
//                        img.setImageResource(id)
//
//                        Update.setOnClickListener{
//                            context.startActivity(Intent(context, update::class.java))
////                                             .putExtra("ItemData",public(publist[position%publist.size].name,
////                                             publist[position%publist.size].author, publist[position%publist.size].desc,publist[position%publist.size].type,publist[position%publist.size].genre )))
//
//                        }
//                    }
//
//                    else
//                        return
//
//                }
//            }
//
//
//
//        })



  //      author.setText(content.requests)


//        price.setText(content.price)

//        time.setText(content.time)
//        date.setText(content.date)
//        fromAddress.setText(content.fromAddress)
//        toAddress.setText(content.toAddress)
//        requestsCount.setText(content.requests)
//        pledgePrice.setText(content.pledgePrice)
      //  status.setText(content.status)
       // avatar.setImageResource(content.avatar)

       // Glide.with(avatar).load(content.avatar).into(avatar)
    }

    // simple methods for register cell state changes
    fun registerToggle(position: Int) {
        if (unfoldedIndexes.contains(position)) registerFold(position) else registerUnfold(position)
    }

    fun registerFold(position: Int) {
        unfoldedIndexes.remove(position)
    }

    fun registerUnfold(position: Int) {
        unfoldedIndexes.add(position)
    }
}


//class ItemImage(view: View) : PageItem(view) {
//    private val imageView = view.findViewById<ImageView>(R.id.page_image)
//
//    fun setImage(imgId: Int) {
//        Glide.with(imageView).load(imgId).into(imageView)
//    }
//}

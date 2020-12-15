package com.ramotion.navigationtoolbar.example.pager

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.ramotion.foldingcell.FoldingCell
import com.ramotion.navigationtoolbar.example.PageDataSet
import com.ramotion.navigationtoolbar.example.R
import com.ramotion.navigationtoolbar.example.publicationModel
import com.ramotion.navigationtoolbar.example.update
import kotlinx.android.parcel.Parcelize
import java.util.*


sealed class PageItem(view: View) : RecyclerView.ViewHolder(view) {
    fun clearContent() {}
}

class ItemUser(view: View, context: Context) : PageItem(view) {
    //private val avatar = view.findViewById<ImageView>(R.id.avatar)

    private val unfoldedIndexes = HashSet<Int>()
//    private val request = view.findViewById<TextView>(R.id.title_requests_count)
//    private val price = view.findViewById<TextView>(R.id.user_name)
//    private val time = view.findViewById<TextView>(R.id.title_time_label)
//    private val date = view.findViewById<TextView>(R.id.title_date_label)
//    private val fromAddress = view.findViewById<TextView>(R.id.title_from_address)
//    private val toAddress = view.findViewById<TextView>(R.id.title_to_address)
//    private val requestsCount = view.findViewById<TextView>(R.id.title_requests_count)
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
        lateinit  var publist: MutableList<publicationModel>
        if(anon == true){
            Update.visibility =  View.GONE
            delete.visibility = View.GONE
            blank1.visibility = View.VISIBLE
            blank2.visibility = View.VISIBLE
        }



        mMessageReference = FirebaseDatabase.getInstance().getReference("publication")
        publist = mutableListOf()
        mMessageReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(h in snapshot.children){
                        val data = h.getValue(publicationModel::class.java)
                        publist.add(data!!)
                    }

                    println("Page is$page $position ${publist[position%publist.size].type.toString()}")

                    if(page%5>2){
                        publist.shuffle()
                    }





                    if(publist[position%publist.size].type == "Poem"){
                        read.setOnClickListener {

                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://static1.squarespace.com/static/51f912e6e4b0cc5aa449f476/t/58502a43b8a79bf9d5651cdd/1481648710707/Poetry+for+Perseverance.pdf")))
                        }

                    }
                    else{
                        read.setOnClickListener {

                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://shortstorylines.com/wp-content/uploads/2020/01/the-boy-who-cried-wolf-pdf.pdf")))
                        }
                    }

                    if(page%5==0){
                             if(publist[position%publist.size].type == "Story")    {

                                 author.text= publist[position%publist.size].author
                                 name.text= publist[position%publist.size].name
                                 name2.text= publist[position%publist.size].name
                                 type.text= publist[position%publist.size].type
                                 desc.text= publist[position%publist.size].desc
                                 desc2.text= publist[position%publist.size].desc
                                 genre.text= publist[position%publist.size].genre

                                 // Display an image into image view from assets folder
                                 val id = context.resources.getIdentifier(publist[position%publist.size].img, "drawable", context.packageName)
                                 img.setImageResource(id)


                                 Update.setOnClickListener{
                                     context.startActivity(Intent(context, update::class.java))
//                                             .putExtra("ItemData",public(publist[position%publist.size].name,
//                                             publist[position%publist.size].author, publist[position%publist.size].desc,publist[position%publist.size].type,publist[position%publist.size].genre )))

                                 }

                             }
                        else
                             {
                                 card.visibility = View.GONE
                                 cell.visibility=View.GONE

                             }
                    }
                    else if(page%5==1){

                        if(publist[position%publist.size].type == "Poem"){
                            author.text= publist[position%publist.size].author
                            name.text= publist[position%publist.size].name
                            name2.text= publist[position%publist.size].name
                            type.text= publist[position%publist.size].type
                            desc.text= publist[position%publist.size].desc
                            desc2.text= publist[position%publist.size].desc
                            genre.text= publist[position%publist.size].genre

                            val id = context.resources.getIdentifier(publist[position%publist.size].img, "drawable", context.packageName)
                            img.setImageResource(id)

                            Update.setOnClickListener{
                                context.startActivity(Intent(context, update::class.java))
//                                             .putExtra("ItemData",public(publist[position%publist.size].name,
//                                             publist[position%publist.size].author, publist[position%publist.size].desc,publist[position%publist.size].type,publist[position%publist.size].genre )))

                            }
                        }
                        else{
                            card.visibility = View.GONE
                            cell.visibility=View.GONE
                        }
                    }
                    else if(page%5==2){
                        author.text= publist[position%publist.size].author
                        name.text= publist[position%publist.size].name
                        name2.text= publist[position%publist.size].name
                        type.text= publist[position%publist.size].type
                        desc.text= publist[position%publist.size].desc
                        desc2.text= publist[position%publist.size].desc
                        genre.text= publist[position%publist.size].genre

                        val id = context.resources.getIdentifier(publist[position%publist.size].img, "drawable", context.packageName)
                        img.setImageResource(id)

                        Update.setOnClickListener{
                            context.startActivity(Intent(context, update::class.java))
//                                             .putExtra("ItemData",public(publist[position%publist.size].name,
//                                             publist[position%publist.size].author, publist[position%publist.size].desc,publist[position%publist.size].type,publist[position%publist.size].genre )))

                        }
                    }
                    else if(page%5==3){
                        author.text= publist[position%publist.size].author
                        name.text= publist[position%publist.size].name
                        name2.text= publist[position%publist.size].name
                        type.text= publist[position%publist.size].type
                        desc.text= publist[position%publist.size].desc
                        desc2.text= publist[position%publist.size].desc
                        genre.text= publist[position%publist.size].genre

                        val id = context.resources.getIdentifier(publist[position%publist.size].img, "drawable", context.packageName)
                        img.setImageResource(id)

                        Update.setOnClickListener{
                            context.startActivity(Intent(context, update::class.java))
//                                             .putExtra("ItemData",public(publist[position%publist.size].name,
//                                             publist[position%publist.size].author, publist[position%publist.size].desc,publist[position%publist.size].type,publist[position%publist.size].genre )))

                        }
                    }
                    else if(page%5==4){
                        author.text= publist[position%publist.size].author
                        name.text= publist[position%publist.size].name
                        name2.text= publist[position%publist.size].name
                        type.text= publist[position%publist.size].type
                        desc.text= publist[position%publist.size].desc
                        desc2.text= publist[position%publist.size].desc
                        genre.text= publist[position%publist.size].genre

                        val id = context.resources.getIdentifier(publist[position%publist.size].img, "drawable", context.packageName)
                        img.setImageResource(id)

                        Update.setOnClickListener{
                            context.startActivity(Intent(context, update::class.java))
//                                             .putExtra("ItemData",public(publist[position%publist.size].name,
//                                             publist[position%publist.size].author, publist[position%publist.size].desc,publist[position%publist.size].type,publist[position%publist.size].genre )))

                        }
                    }

                    else
                        return

                }
            }



        })



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

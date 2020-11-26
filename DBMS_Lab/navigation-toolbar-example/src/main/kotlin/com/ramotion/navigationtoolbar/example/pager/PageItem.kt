package com.ramotion.navigationtoolbar.example.pager

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ramotion.navigationtoolbar.example.PageDataSet
import com.ramotion.navigationtoolbar.example.R
import java.util.HashSet

sealed class PageItem(view: View) : RecyclerView.ViewHolder(view) {
    fun clearContent() {}
}

class ItemUser(view: View) : PageItem(view) {
    //private val avatar = view.findViewById<ImageView>(R.id.avatar)
    private val unfoldedIndexes = HashSet<Int>()
//    private val request = view.findViewById<TextView>(R.id.title_requests_count)
//    private val price = view.findViewById<TextView>(R.id.user_name)
//    private val time = view.findViewById<TextView>(R.id.title_time_label)
//    private val date = view.findViewById<TextView>(R.id.title_date_label)
//    private val fromAddress = view.findViewById<TextView>(R.id.title_from_address)
//    private val toAddress = view.findViewById<TextView>(R.id.title_to_address)
//    private val requestsCount = view.findViewById<TextView>(R.id.title_requests_count)
//    private val pledgePrice = view.findViewById<TextView>(R.id.title_pledge)
    private val contentRequestBtn = view.findViewById<TextView>(R.id.content_request_btn)
    //private val status = view.findViewById<TextView>(R.id.status)

    fun setContent(content: PageDataSet.ItemData) {
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

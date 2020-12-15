package com.ramotion.navigationtoolbar.example.pager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.ramotion.navigationtoolbar.example.publicationModel

class publicationAdapter(mctx: Context, layoutResId: Int, pubList: List<publicationModel>)
    :ArrayAdapter<publicationModel>(mctx, layoutResId, pubList){

//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val layoutInflater: LayoutInflater= LayoutInflater.from(mctx)
//
//    }



}
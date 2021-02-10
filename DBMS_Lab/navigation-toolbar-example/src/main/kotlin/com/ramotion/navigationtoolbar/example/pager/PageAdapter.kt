package com.ramotion.navigationtoolbar.example.pager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ramotion.navigationtoolbar.example.Model.PageDataSet
import com.ramotion.navigationtoolbar.example.R
import java.util.*


class PageAdapter(private val count: Int,
                  private val dataSet: PageDataSet, private val page: Int, private val context: Context) : RecyclerView.Adapter<PageItem>() {

    private var unfoldedIndexes = HashSet<Int>()
    private var defaultRequestBtnClickListener: View.OnClickListener? = null

    var onItemClick: ((PageDataSet) -> Unit)? = null
    private enum class ItemType(val value: Int) {
        USER(1);
       // IMAGE(2);

        companion object {
            private val map = ItemType.values().associateBy(ItemType::value)
            fun fromInt(type: Int, defaultValue: ItemType = USER) = map.getOrElse(type) {defaultValue}
        }
    }

    override fun getItemCount() = count


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageItem {
        return when (ItemType.fromInt(viewType)) {
            ItemType.USER -> createItemUser(parent)
            //ItemType.IMAGE -> createItemImage(parent)
        }
    }

    override fun onBindViewHolder(holder: PageItem, position: Int) {
        when (holder) {

            is ItemUser -> { holder.
            setContent(dataSet.getItemData(position,page), position,page, context) }
            //is ItemImage -> { holder.setImage(dataSet.secondItemImage) }
        }
    }


    override fun onViewRecycled(holder: PageItem) {
        super.onViewRecycled(holder)
        holder.clearContent()
    }



    private fun createItemUser(parent: ViewGroup): ItemUser {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell, parent, false)
        return ItemUser(view, parent.context)
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
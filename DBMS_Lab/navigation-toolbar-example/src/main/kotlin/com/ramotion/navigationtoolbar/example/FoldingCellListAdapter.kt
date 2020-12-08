//package com.ramotion.navigationtoolbar.example
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import android.widget.TextView
//import com.ramotion.foldingcell.FoldingCell
//import com.ramotion.navigationtoolbar.example.pager.ItemUser
//import com.ramotion.navigationtoolbar.example.pager.PageItem
//import java.util.*
//
//
///**
// * Simple example of ListAdapter for using with Folding Cell
// * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
// */
//class FoldingCellListAdapter(context: Context?, objects: List<Item?>?) : ArrayAdapter<Item?>(context, 0, objects) {
//    private val unfoldedIndexes = HashSet<Int>()
//    var defaultRequestBtnClickListener: View.OnClickListener? = null
//
//    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
//        // get item for selected view
//        val item = getItem(position)
//        // if cell is exists - reuse it, if not - create the new one from resource
//        var cell = convertView as FoldingCell
//        val viewHolder: ViewHolder
//        if (cell == null) {
//            viewHolder = ViewHolder()
//            val vi = LayoutInflater.from(context)
//            cell = vi.inflate(R.layout.cell, parent, false) as FoldingCell
//            // binding view parts to view holder
////            viewHolder.price = cell.findViewById(R.id.title_price)
////            viewHolder.time = cell.findViewById(R.id.title_time_label)
////            viewHolder.date = cell.findViewById(R.id.title_date_label)
////            viewHolder.fromAddress = cell.findViewById(R.id.title_from_address)
////            viewHolder.toAddress = cell.findViewById(R.id.title_to_address)
////            viewHolder.requestsCount = cell.findViewById(R.id.title_requests_count)
////            viewHolder.pledgePrice = cell.findViewById(R.id.title_pledge)
//            viewHolder.contentRequestBtn = cell.findViewById(R.id.content_request_btn)
//            cell.tag = viewHolder
//        } else {
//            // for existing cell set valid valid state(without animation)
//            if (unfoldedIndexes.contains(position)) {
//                cell.unfold(true)
//            } else {
//                cell.fold(true)
//            }
//            viewHolder = cell.tag as ViewHolder
//        }
//        if (null == item) return cell
//
//        // bind data from selected element to view through view holder
//        viewHolder.price?.setText(item.getPrice_new())
//        viewHolder.time?.setText(item.getTime_new())
//        viewHolder.date?.setText(item.getDate_new())
//        viewHolder.fromAddress?.setText(item.getFromAddress_new())
//        viewHolder.toAddress?.setText(item.getToAddress_new())
//        viewHolder.requestsCount?.setText((item.getRequestsCount_new()))
//        viewHolder.pledgePrice?.setText(item.getPledgePrice_new())
//
//        // set custom btn handler for list item from that item
//        if (item.getRequestBtnClickListener_new() != null) {
//            viewHolder.contentRequestBtn!!.setOnClickListener(item.getRequestBtnClickListener_new())
//        } else {
//            // (optionally) add "default" handler if no handler found in item
//            viewHolder.contentRequestBtn!!.setOnClickListener(defaultRequestBtnClickListener)
//        }
//        return cell
//    }
//
//    // simple methods for register cell state changes
//    fun registerToggle(position: Int) {
//        if (unfoldedIndexes.contains(position)) registerFold(position) else registerUnfold(position)
//    }
//
//    fun registerFold(position: Int) {
//        unfoldedIndexes.remove(position)
//    }
//
//    fun registerUnfold(position: Int) {
//        unfoldedIndexes.add(position)
//    }
//
//    // View lookup cache
//    private class ViewHolder {
//        var price: TextView? = null
//        var contentRequestBtn: TextView? = null
//        var pledgePrice: TextView? = null
//        var fromAddress: TextView? = null
//        var toAddress: TextView? = null
//        var requestsCount: TextView? = null
//        var date: TextView? = null
//        var time: TextView? = null
//    }
//
//
//    private enum class ItemType(val value: Int) {
//        USER(1);
//        // IMAGE(2);
//
//        companion object {
//            private val map = ItemType.values().associateBy(ItemType::value)
//            fun fromInt(type: Int, defaultValue: ItemType = USER) = map.getOrElse(type) {defaultValue}
//        }
//    }
//
//    fun getItemCount() = count
//
//    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageItem {
//        return when (ItemType.fromInt(viewType)) {
//            ItemType.USER -> createItemUser(parent)
//            //ItemType.IMAGE -> createItemImage(parent)
//        }
//    }
//
//
////    override fun getItemViewType(position: Int): Int {
////        return (if (position == 1) ItemType.IMAGE else ItemType.USER).value
////    }
//
//
//    private fun createItemUser(parent: ViewGroup): ItemUser {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.list, parent, false)
//        return ItemUser(view)
//    }
//}
//
//private fun <T> ArrayAdapter<T>.onViewRecycled(holder: PageItem) {
//
//}
//
//

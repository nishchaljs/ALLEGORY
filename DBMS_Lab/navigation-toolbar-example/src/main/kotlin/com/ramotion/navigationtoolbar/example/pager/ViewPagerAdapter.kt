package com.ramotion.navigationtoolbar.example.pager

//import com.ramotion.navigationtoolbar.example.FoldingCellListAdapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.ramotion.foldingcell.FoldingCell
import com.ramotion.navigationtoolbar.example.R
import com.ramotion.navigationtoolbar.example.ViewPagerDataSet
import java.util.*


class ViewPagerAdapter(val context:Context, private val count: Int,
                       private val dataSet: ViewPagerDataSet) : PagerAdapter() {

    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }

    fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object: RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener({
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                })
            }
        })
    }

    private companion object {
        val random = Random()
    }

    override fun getCount(): Int = count

    override fun isViewFromObject(view: View, key: Any): Boolean = view == key

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.list, container, false)
        initRecyclerView(view as RecyclerView, position)

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, key: Any) {
        container.removeView(key as View)
    }

    override fun getPageTitle(position: Int): CharSequence = position.toString()

    private fun initRecyclerView(recyclerView: RecyclerView, position: Int) {
        val adapter = PageAdapter(5, dataSet.getPageData(position))

        // prepare elements to display
        //val items = Item.getTestingList()
        //val foldadapter = FoldingCellListAdapter(context, items)
        recyclerView.adapter = adapter
        recyclerView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                // toggle clicked cell state

                // toggle clicked cell state
                (view as FoldingCell).toggle(false)
                // register in adapter that state for selected cell is toggled
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(position)

            }
        })
        // set on click event listener to list view
    }

}
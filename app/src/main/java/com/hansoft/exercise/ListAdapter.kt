package com.hansoft.exercise

/**
 * created by qi zhu 10/12/2011
 * Create ListAdapter class to show the title,description and imageHref information
 */

import android.R
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.*

class ListAdapter(activity : MainActivity,data: ArrayList<Canada>, listener: OnItemClickListener?) : RecyclerView.Adapter<ListViewHolder?>() {

    private val listener: OnItemClickListener?


    private var myActivity = activity
    private var canadas = ArrayList<Canada>()
    init {
        canadas = data
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            com.hansoft.exercise.R.layout.list_item, parent, false
        )
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val canada = canadas[position]

        holder.mTitle!!.setText(canada.title)
        holder.mDescription!!.setText(canada.description)
        holder.imageView!!.tag = position
        var url: String = canadas[position % canadas.size].imageHref!!

        if (url == "null") {        // url is "null"


            holder.imageView!!.setImageDrawable(null)
            
        } else if (url.indexOf("https") >= 0) {
            Glide.with(myActivity).load(url)
                .placeholder(com.hansoft.exercise.R.drawable.pagenotfound)
                .into(holder.imageView!!)

        } else if (url.indexOf("http") >= 0) {
            // replace http with https
            url = url.replace("http", "https")

            Glide.with(myActivity).load(url)
                .placeholder(com.hansoft.exercise.R.drawable.pagenotfound)
                .into(holder.imageView!!)
        }

        if (listener != null) {
            holder.mTitle!!.setOnClickListener(View.OnClickListener { listener.onItemClick(position) })
        }
    }

    override fun getItemCount(): Int {
        // get the count of the arraylist
        return if (canadas == null) 0 else canadas!!.size
    }


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


}
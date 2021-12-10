package com.hansoft.exercise

/**
 * created by qi zhu 10/12/2011
 * Create ListViewHolder class to show the title,description and imageHref information on the list_item.xml
 */

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    var mTitle: TextView? = null
    var mDescription: TextView? = null
    var imageView: ImageView? = null

    init {

        mTitle = itemView!!.findViewById(R.id.item_title)
        mDescription = itemView!!.findViewById<View>(R.id.item_description) as TextView
        imageView = itemView!!.findViewById(R.id.item_imageHref)

    }


}
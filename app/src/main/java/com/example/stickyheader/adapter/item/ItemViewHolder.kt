package com.example.stickyheader.adapter.item

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stickyheader.R
import java.lang.ref.WeakReference

class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val view = WeakReference(itemView)

    private var textViewTitle: TextView? = null
    private var textViewDate: TextView? = null

    var itemModel: ItemModel? = null

    init {
        view.get()?.let {
            textViewTitle = it.findViewById(R.id.textViewTitle)
            textViewDate = it.findViewById(R.id.textViewDate)
        }
    }

    fun updateView(){
        textViewTitle?.text = itemModel?.title
        textViewDate?.text = itemModel?.date
    }
}
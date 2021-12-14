package com.example.stickyheader.adapter.item

import android.content.Context
import android.widget.FrameLayout
import android.widget.TextView
import com.example.stickyheader.R
import java.util.*

class SectionViewHolder(context: Context): FrameLayout(context) {
    private lateinit var textViewDate: TextView

    init{
        inflate(context, R.layout.view_holder_section, this)

        findView()
    }

    private fun findView() {
        textViewDate = findViewById(R.id.textViewDate)
    }

    fun setDate(dateString: String){
        textViewDate.text = dateString
    }

}
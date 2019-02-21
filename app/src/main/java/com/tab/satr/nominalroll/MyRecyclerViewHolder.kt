package com.tab.satr.nominalroll

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.tab.satr.R

class MyRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var mUserName: TextView = itemView.findViewById(R.id.info_text)
    var mCheckBox: CheckBox = itemView.findViewById(R.id.checkbox_status)

}
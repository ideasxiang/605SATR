package com.tab.satr.nominalroll

import android.graphics.Color
import android.support.annotation.RequiresPermission
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.tab.satr.R
import java.util.*
import kotlin.collections.HashMap



class MyRecyclerViewAdapter(private var mainActivity: NominalRoll, private var userArrayList: ArrayList<com.tab.satr.nominalroll.User>?) :
    RecyclerView.Adapter<MyRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecyclerViewHolder {

        val layoutInflater = LayoutInflater.from(mainActivity.baseContext)
        val view = layoutInflater.inflate(R.layout.recycler_item, parent, false)

        return MyRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRecyclerViewHolder, position: Int) {

        holder.mUserName.text = userArrayList!![position].userName
        holder.mCheckBox.isChecked = userArrayList!![position].checked
        holder.mCheckBox.setOnClickListener {
            val checked: Boolean = holder.mCheckBox.isChecked
            val data = HashMap<String,Any?>()
            data["checked"] = checked
            mainActivity.db!!.collection("users").document(userArrayList!![position].userName).update(data)
        }
        if(position %2 == 1)
        {
            // Set a background color for ListView regular row/item
            holder.mUserName.setBackgroundColor(Color.parseColor("#e6ffe6"))
            holder.mCheckBox.setBackgroundColor(Color.parseColor("#e6ffe6"))
        }
        else
        {
            // Set the background color for alternate row/item
            holder.mUserName.setBackgroundColor(Color.parseColor("#fff69b"))
            holder.mCheckBox.setBackgroundColor(Color.parseColor("#fff69b"))
        }

    }

    override fun getItemCount(): Int {
        return userArrayList!!.size
    }

    private fun onCheckboxClicked(view: View) {

        }
}
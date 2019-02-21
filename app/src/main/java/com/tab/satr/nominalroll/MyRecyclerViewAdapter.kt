package com.tab.satr.nominalroll

import android.preference.PreferenceManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.tab.satr.R
import java.util.*

class MyRecyclerViewAdapter(private var mainActivity: NominalRoll, private var userArrayList: ArrayList<com.tab.satr.nominalroll.User>?) :
    RecyclerView.Adapter<MyRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecyclerViewHolder {

        val layoutInflater = LayoutInflater.from(mainActivity.baseContext)
        val view = layoutInflater.inflate(R.layout.recyclerview_item, parent, false)

        return MyRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRecyclerViewHolder, position: Int) {

        holder.mUserName.text = this.userArrayList!![position].userName
        val checked = PreferenceManager.getDefaultSharedPreferences(mainActivity.baseContext)
            .getBoolean("checkBoxStatus", false)
        holder.mCheckBox.isChecked = checked
        holder.mCheckBox.setOnClickListener {onCheckboxClicked(holder.mCheckBox)}

    }

    override fun getItemCount(): Int {
        return userArrayList!!.size
    }

    private fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked
            if (checked) {
                PreferenceManager.getDefaultSharedPreferences(mainActivity.baseContext).edit()
                    .putBoolean("checkBoxStatus", checked).apply()
                }
            else{
                PreferenceManager.getDefaultSharedPreferences(mainActivity.baseContext).edit()
                    .putBoolean("checkBoxStatus", checked).apply()
            }
            }
        }
    }
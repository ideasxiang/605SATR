package com.tab.satr.nominalroll

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tab.satr.R
import java.util.*

class MyRecyclerViewAdapter(private var mainActivity: NominalRoll, private var recordsArrayList: ArrayList<com.tab.satr.nominalroll.Records>?) :
    RecyclerView.Adapter<MyRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecyclerViewHolder {

        val layoutInflater = LayoutInflater.from(mainActivity.baseContext)
        val view = layoutInflater.inflate(R.layout.recycler_item, parent, false)

        return MyRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRecyclerViewHolder, position: Int) {

        holder.mUserName.text = recordsArrayList!![position].name
        holder.mCheckBox.isChecked = recordsArrayList!![position].present
        holder.mCheckBox.setOnClickListener {
            val data = HashMap<String,Any?>()
            val checked: Boolean = holder.mCheckBox.isChecked
            data["present"] = checked
            mainActivity.db.collection("satr_courses")
                    .document(recordsArrayList!![position].documentId)
                    .update(data)
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
        return recordsArrayList!!.size
    }

}
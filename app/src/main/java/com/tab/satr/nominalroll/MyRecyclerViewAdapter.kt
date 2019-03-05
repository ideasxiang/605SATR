package com.tab.satr.nominalroll

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.tab.satr.R
import java.util.*
import kotlin.collections.ArrayList

class MyRecyclerViewAdapter(private var mainActivity: NominalRoll, private var recordsArrayList: ArrayList<com.tab.satr.nominalroll.Records> = ArrayList()) :
    RecyclerView.Adapter<MyRecyclerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecyclerViewHolder {

        val layoutInflater = LayoutInflater.from(mainActivity.baseContext)
        val view = layoutInflater.inflate(R.layout.recycler_item, parent, false)

        return MyRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRecyclerViewHolder, position: Int) {
        val data = HashMap<String,Any?>()
        var reasonText: String
        holder.mUserName.text = recordsArrayList[position].name
        holder.mCheckBox.isChecked = recordsArrayList[position].present
        holder.mEditExplanation.setText(recordsArrayList[position].reason)
        mainActivity.saveBtn!!.setOnClickListener{
            reasonText = holder.mEditExplanation.text.toString()
            data["reason"] = reasonText
            mainActivity.usercourses
                .document(recordsArrayList[position].documentId)
                .update(data)
            Toast.makeText(mainActivity,"Saved",Toast.LENGTH_SHORT).show()
        }
        holder.mCheckBox.setOnClickListener {
            val checked: Boolean = holder.mCheckBox.isChecked
            data["present"] = checked
            mainActivity.usercourses
                    .document(recordsArrayList[position].documentId)
                    .update(data)
        }
        if(position %2 == 1)
        {
            // Set a background color for ListView regular row/item
            holder.mUserName.setBackgroundColor(Color.parseColor("#e6ffe6"))
            holder.mCheckBox.setBackgroundColor(Color.parseColor("#e6ffe6"))
            holder.mEditExplanation.setBackgroundColor(Color.parseColor("#e6ffe6"))
        }
        else
        {
            // Set the background color for alternate row/item
            holder.mUserName.setBackgroundColor(Color.parseColor("#fff69b"))
            holder.mCheckBox.setBackgroundColor(Color.parseColor("#fff69b"))
            holder.mEditExplanation.setBackgroundColor(Color.parseColor("#fff69b"))
        }

    }

    override fun getItemCount(): Int {
        return recordsArrayList.size
    }

}
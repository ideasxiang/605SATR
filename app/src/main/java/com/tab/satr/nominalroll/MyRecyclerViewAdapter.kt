package com.tab.satr.nominalroll

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.tab.satr.R

class MyRecyclerViewAdapter// data is passed into the constructor
internal constructor(context: Context, private val mData: java.util.ArrayList<com.tab.satr.nominalroll.User>) : RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null
    private lateinit var userArrayList: ArrayList<User>
    private lateinit var mainActivity: NominalRoll

    // inflates the cell layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mUserName.text = mData[position].toString()
        holder.mDeleteRow.setOnClickListener { deleteSelectedRow(position) }

    }

    // total number of cells
    override fun getItemCount(): Int {
        return mData.size
    }


    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mUserName: TextView = itemView.findViewById(R.id.info_text)
        var mDeleteRow: Button = itemView.findViewById(R.id.mRowDeleteBtn)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }
    }

    // convenience method for getting data at click position
    internal fun getItem(id: Int): String {
        return mData[id].toString()
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    private fun deleteSelectedRow(position: Int) {
        mainActivity.db.collection("users")
                .document(userArrayList[position].uid!!)
                .delete()
                .addOnSuccessListener(OnSuccessListener<Void> {
                    Toast.makeText(mainActivity.baseContext, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                    mainActivity.loadDataFromFirebase()
                })
                .addOnFailureListener(OnFailureListener { e ->
                    Toast.makeText(mainActivity, "Unable To Delete --3--", Toast.LENGTH_SHORT).show()
                    Log.w("--3--", e.message)
                })
    }
}
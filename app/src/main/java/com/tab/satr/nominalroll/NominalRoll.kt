package com.tab.satr.nominalroll

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.tab.satr.R
import java.util.*

class NominalRoll : AppCompatActivity(), MyRecyclerViewAdapter.ItemClickListener {

    private lateinit var adapter: MyRecyclerViewAdapter
    private var userArrayList: ArrayList<User>? = null
    lateinit var db: FirebaseFirestore
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nominal_roll)

        userArrayList = ArrayList()

        setUpRecyclerView()
        setUpFireBase()
        loadDataFromFirebase()
    }

    fun loadDataFromFirebase() {
        if (userArrayList!!.size > 0)
            userArrayList!!.clear()

        db.collection("users")
                .get()
                .addOnCompleteListener { task ->
                    for (documentSnapshot in task.result!!) {
                        val user = User(documentSnapshot.id,
                                documentSnapshot.getString("firstName")!!,
                                documentSnapshot.getString("lastName")!!)
                        userArrayList!!.add(user)
                    }
                    adapter = MyRecyclerViewAdapter(this@NominalRoll, userArrayList!!)
                    mRecyclerView.adapter = adapter
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this@NominalRoll, "Problem ---1---", Toast.LENGTH_SHORT).show()
                    Log.w("---1---", e.message)
                }
    }

    private fun setUpFireBase() {
        db = FirebaseFirestore.getInstance()
    }

    private fun setUpRecyclerView() {
        mRecyclerView = findViewById(R.id.mRecyclerView)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onItemClick(view: View, position: Int) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position)
    }
}

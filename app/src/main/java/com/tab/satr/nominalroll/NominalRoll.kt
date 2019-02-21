package com.tab.satr.nominalroll

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.tab.satr.R


class NominalRoll : AppCompatActivity() {

    var db: FirebaseFirestore? = null
    private var mRecyclerView: RecyclerView? = null
    private var userArrayList: java.util.ArrayList<User>? = null
    private var adapter: MyRecyclerViewAdapter? = null
    var checked: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nominal_roll)

        userArrayList = ArrayList()

        setUpRecyclerView()
        setUpFireBase()
        loadDataFromFirebase()
    }

    fun loadDataFromFirebase() {

        db!!.collection("users")
            .get()
            .addOnCompleteListener { task ->
                for (documentSnapshot in task.result!!) {
                    val user = User(
                        documentSnapshot.getString("username")!!
                    )
                    checked = documentSnapshot.getBoolean("checked")!!
                    userArrayList!!.add(user)
                }
                adapter = MyRecyclerViewAdapter(this@NominalRoll, userArrayList)
                mRecyclerView!!.adapter = adapter
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
        mRecyclerView = findViewById(com.tab.satr.R.id.mRecyclerView)
        mRecyclerView?.setHasFixedSize(true)
        mRecyclerView?.layoutManager = LinearLayoutManager(this)
    }

}

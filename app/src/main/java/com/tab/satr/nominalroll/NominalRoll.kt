package com.tab.satr.nominalroll

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.tab.satr.R




class NominalRoll : AppCompatActivity() {

    val MY_PREFS_NAME = "MyPrefsFile"

    var db: FirebaseFirestore? = null
    private var mRecyclerView: RecyclerView? = null
    private var userArrayList: java.util.ArrayList<User>? = null
    private var adapter: MyRecyclerViewAdapter? = null
    var checked: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tab.satr.R.layout.activity_nominal_roll)

        userArrayList = ArrayList()

        val intent: Intent = intent
        val dayOfMonth = intent.getIntExtra("dayOfMonth",0)
        val month = intent.getIntExtra("month",0)
        val year = intent.getIntExtra("year",0)

        val prefs = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        val courses = prefs.getString("courses", "No courses defined")

        val date = findViewById<TextView>(R.id.date_picked)
        date.text = getString(R.string.DatePicked).plus(" $dayOfMonth").plus(" / $month").plus(" / $year")

        val coursespicked = findViewById<TextView>(R.id.courses)
        coursespicked.text = getString(R.string.courses_picked).plus(" $courses")

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

package com.tab.satr.nominalroll

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.tab.satr.R
import android.preference.PreferenceManager

class NominalRoll : AppCompatActivity() {

    val MY_PREFS_NAME = "MyPrefsFile"

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var mRecyclerView: RecyclerView? = null
    private var recordsArrayList: java.util.ArrayList<Records>? = null
    private var adapter: MyRecyclerViewAdapter? = null
    var coursespicked: String?= null
    var datedisplay: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tab.satr.R.layout.activity_nominal_roll)

        recordsArrayList = ArrayList()

        val dayOfMonth = intent.getIntExtra("dayOfMonth",0)
        val month = intent.getIntExtra("month",0)
        val year = intent.getIntExtra("year",0)

        datedisplay = "$dayOfMonth/$month/$year"

        val prefs = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        coursespicked = prefs.getString("courses", "No courses defined")

        val dateview = findViewById<TextView>(R.id.date_picked)
        val txtcoursespicked = findViewById<TextView>(R.id.courses)
        val refreshBtn = findViewById<ImageView>(R.id.btn_refresh)

        dateview.text = datedisplay
        txtcoursespicked.text = "$coursespicked"
        refreshBtn.setOnClickListener{loadDataFromFirebase()}

        addInitialNominalRoll()
        setUpRecyclerView()
        loadDataFromFirebase()
    }

    private fun addInitialNominalRoll() {

        val SIN = Users(
            coursespicked!!,
            datedisplay!!,
            "SQN HQ",
            "SIN REN XIANG",
            false
        )

        val datetrack = PreferenceManager.getDefaultSharedPreferences(this)
        val datetrackeditor = datetrack.edit()
        val previousdatetrack = datetrack.getString("date","1/1/2001")

        if (previousdatetrack != datedisplay) {
            datetrackeditor.putString("date",datedisplay)
            datetrackeditor.apply()
            db.collection("satr_courses").add(SIN)
        }
    }

    private fun loadDataFromFirebase() {
        if (recordsArrayList!!.size > 0)
            recordsArrayList!!.clear()

        val usercourses = db.collection("satr_courses")
        val query = usercourses.whereEqualTo("date", datedisplay).whereEqualTo("course_name",coursespicked)
        
        query
            .get()
            .addOnCompleteListener { task ->
                for (documentSnapshot in task.result!!) {
                    val satrcourses = Records(
                            documentSnapshot.id,
                            documentSnapshot.getString("course_name")!!,
                            documentSnapshot.getString("date")!!,
                            documentSnapshot.getString("department")!!,
                            documentSnapshot.getString("name")!!,
                            documentSnapshot.getBoolean("present")!!
                    )
                    recordsArrayList!!.add(satrcourses)
                }
                adapter = MyRecyclerViewAdapter(this@NominalRoll, recordsArrayList)
                mRecyclerView!!.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@NominalRoll, "Problem ---1---", Toast.LENGTH_SHORT).show()
                Log.w("---1---", e.message)
            }
    }

    private fun setUpRecyclerView() {
        mRecyclerView = findViewById(com.tab.satr.R.id.mRecyclerView)
        mRecyclerView?.setHasFixedSize(true)
        mRecyclerView?.layoutManager = LinearLayoutManager(this)
    }

}

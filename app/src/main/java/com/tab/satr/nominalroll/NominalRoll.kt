package com.tab.satr.nominalroll

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.tab.satr.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NominalRoll : AppCompatActivity() {

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var mRecyclerView: RecyclerView? = null
    private var recordsArrayList: java.util.ArrayList<Records>? = null
    private var adapter: MyRecyclerViewAdapter? = null
    var coursespicked: String?= null
    var datedisplay: String? = null
    val usercourses = db.collection("satr_courses")
    var saveBtn: ImageView?= null
    var unixdate: Long ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tab.satr.R.layout.activity_nominal_roll)

        recordsArrayList = ArrayList()

        val prefs = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE)
        coursespicked = prefs.getString("courses", "No courses defined")
        unixdate = prefs.getLong("unixdate",0)

        datedisplay = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(unixdate!!*1000L)

        val dateview = findViewById<TextView>(R.id.date_picked)
        val txtcoursespicked = findViewById<TextView>(R.id.courses)
        val editBtn = findViewById<ImageView>(R.id.btn_edit)
        saveBtn = findViewById(R.id.btn_save)

        dateview.text = datedisplay
        txtcoursespicked.text = "$coursespicked"

        addInitialNominalRoll()
        loadDataFromFirebase()
        setUpRecyclerView()

        editBtn.setOnClickListener{

        }
    }

    private fun addInitialNominalRoll() {
        usercourses
            .whereEqualTo("course_name",coursespicked)
            .whereEqualTo("date",datedisplay)
            .get()
            .addOnSuccessListener{ document->
                if(document.isEmpty){
                    generateUserList()
                }
            }
    }

    private fun generateUserList() {
        val usersMap = HashMap<Any?,Any?>()
        db.collection("users")
            .whereEqualTo("isord",false)
            .get()
            .addOnCompleteListener { task ->
                for (ds in task.result!!) {
                    usersMap["unixdate"] = unixdate
                    usersMap["department"] = ds.getString("department")
                    usersMap["name"] = ds.getString("name")
                    usersMap["present"] = false
                    usersMap["course_name"] = coursespicked
                    usersMap["appointment"] = ds.getString("appointment")
                    usersMap["reason"] = ""
                    usersMap["cycle"] = ds.getString("cycle")
                    db.collection("satr_courses").add(usersMap)
                }
                loadDataFromFirebase()
            }
    }

    private fun loadDataFromFirebase() {
        if (recordsArrayList!!.size > 0)
            recordsArrayList!!.clear()

        val query = usercourses
            .whereEqualTo("unixdate", unixdate)
            .whereEqualTo("course_name",coursespicked)
        
        query
            .get()
            .addOnCompleteListener { task ->
                for (ds in task.result!!) {
                    val satrcourses = Records(
                        ds.id,
                        ds.getString("name")!!,
                        ds.getBoolean("present")!!,
                        ds.getString("reason")!!
                    )
                    recordsArrayList!!.add(satrcourses)
                }
                adapter = MyRecyclerViewAdapter(this@NominalRoll, recordsArrayList)
                mRecyclerView!!.adapter = adapter
            }
    }

    private fun setUpRecyclerView() {
        mRecyclerView = findViewById(com.tab.satr.R.id.mRecyclerView)
        mRecyclerView?.setHasFixedSize(true)
        mRecyclerView?.layoutManager = LinearLayoutManager(this)
    }

}

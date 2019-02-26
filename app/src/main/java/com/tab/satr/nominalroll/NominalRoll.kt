package com.tab.satr.nominalroll

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.tab.satr.R
import java.util.HashMap
import kotlin.collections.ArrayList
import kotlin.collections.set
import kotlin.concurrent.thread


class NominalRoll : AppCompatActivity() {

    val MY_PREFS_NAME = "MyPrefsFile"

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var mRecyclerView: RecyclerView? = null
    private var recordsArrayList: java.util.ArrayList<Records>? = null
    private var usersArrayList: java.util.ArrayList<Users>? = null
    private var adapter: MyRecyclerViewAdapter? = null
    var coursespicked: String?= null
    var datedisplay: String? = null
    val usersMap = HashMap<Any?,Any?>()
    val usercourses = db.collection("satr_courses")
    var saveBtn: ImageView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tab.satr.R.layout.activity_nominal_roll)

        recordsArrayList = ArrayList()
        usersArrayList = ArrayList()

        val dayOfMonth = intent.getIntExtra("dayOfMonth",0)
        val month = intent.getIntExtra("month",0)
        val year = intent.getIntExtra("year",0)

        datedisplay = "$dayOfMonth/$month/$year"

        val prefs = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        coursespicked = prefs.getString("courses", "No courses defined")

        val dateview = findViewById<TextView>(R.id.date_picked)
        val txtcoursespicked = findViewById<TextView>(R.id.courses)
        val refreshBtn = findViewById<ImageView>(R.id.btn_refresh)
        saveBtn = findViewById(R.id.btn_save)

        dateview.text = datedisplay
        txtcoursespicked.text = "$coursespicked"

        addInitialNominalRoll()
        setUpRecyclerView()
        Thread.sleep(1000)
        loadDataFromFirebase()

        refreshBtn.setOnClickListener{loadDataFromFirebase()}
    }

    private fun addInitialNominalRoll() {

        var datetrack = ""
        var coursetrack = ""

        val query = usercourses
            .whereEqualTo("date", datedisplay)
            .whereEqualTo("course_name",coursespicked)

        query
            .get()
            .addOnCompleteListener {task ->
                for (ds in task.result!!) {
                    datetrack = ds.getString("date")!!
                    coursetrack = ds.getString("course_name")!!
                }
            }

        if (datetrack != datedisplay || coursetrack != coursespicked) {
            generateUserList()
        }
    }

    private fun generateUserList() {
        db.collection("users")
            .get()
            .addOnCompleteListener { task ->
                for (ds in task.result!!) {
                    usersMap["date"] = datedisplay
                    usersMap["department"] = ds.getString("department")
                    usersMap["name"] = ds.getString("name")
                    usersMap["present"] = false
                    usersMap["course_name"] = coursespicked
                    usersMap["vocation"] = ds.getString("vocation")
                    usersMap["cycle"] = ds.getString("cycle")
                    usersMap["reason"] = ""
                    db.collection("satr_courses").add(usersMap)
                }
            }
    }

    private fun loadDataFromFirebase() {
        if (recordsArrayList!!.size > 0)
            recordsArrayList!!.clear()

        val query = usercourses
            .whereEqualTo("date", datedisplay)
            .whereEqualTo("course_name",coursespicked)
        
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
                        documentSnapshot.getBoolean("present")!!,
                        documentSnapshot.getString("vocation")!!,
                        documentSnapshot.getString("cycle")!!,
                        documentSnapshot.getString("reason")!!
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

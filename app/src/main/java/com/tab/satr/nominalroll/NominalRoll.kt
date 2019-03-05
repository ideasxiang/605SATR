package com.tab.satr.nominalroll

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
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
    private var recordsArrayList: ArrayList<Records> = ArrayList()
    var coursespicked: String?= null
    var datedisplay: String? = null
    val usercourses = db.collection("satr_courses")
    var saveBtn: ImageView?= null
    var unixdate: Long ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tab.satr.R.layout.activity_nominal_roll)

        // Note that the Toolbar defined in the layout has the id "my_toolbar"
        setSupportActionBar(findViewById(R.id.my_toolbar))

        val prefs = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE)
        coursespicked = prefs.getString("courses", "No courses defined")
        unixdate = prefs.getLong("unixdate",0)

        datedisplay = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(unixdate!!*1000L)

        val dateview: TextView = findViewById(R.id.date_picked)
        val txtcoursespicked: TextView= findViewById(R.id.courses)
        val editBtn: ImageView = findViewById(R.id.btn_edit)
        saveBtn = findViewById(R.id.btn_save)

        dateview.text = datedisplay
        txtcoursespicked.text = "$coursespicked"

        setUpRecyclerView()
        addInitialNominalRoll()
        loadDataFromFirebase()

        editBtn.setOnClickListener{
        }
        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doMySearch(query)
                val Toolbar: android.support.v7.widget.Toolbar = findViewById(R.id.my_toolbar)
                Toolbar.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.nominal_roll_actions, menu)
        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        }
        return true
    }

    private fun doMySearch(query: String){
        val newArrayList: ArrayList<Records> = ArrayList()
        usercourses
            .whereEqualTo("unixdate", unixdate)
            .whereEqualTo("course_name",coursespicked)
            .whereEqualTo("name",query.toUpperCase())
            .get()
            .addOnSuccessListener { task ->
                if (task.isEmpty){
                    mRecyclerView!!.visibility = View.GONE
                    val emptyElement: TextView = findViewById(R.id.emptyElement)
                    emptyElement.visibility = View.VISIBLE
                }
                else{
                    for (ds in task) {
                        val satrcourses = Records(
                            ds.id,
                            ds.getString("name")!!,
                            ds.getBoolean("present")!!,
                            ds.getString("reason")!!
                        )
                        newArrayList.add(satrcourses)
                    }
                }
                val adapter = MyRecyclerViewAdapter(this@NominalRoll, newArrayList)
                mRecyclerView!!.swapAdapter(adapter,true)
            }
    }

    private fun addInitialNominalRoll() {
        usercourses
            .whereEqualTo("course_name",coursespicked)
            .whereEqualTo("unixdate",unixdate)
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
        if (recordsArrayList.size > 0)
            recordsArrayList.clear()
        usercourses
            .whereEqualTo("unixdate", unixdate)
            .whereEqualTo("course_name",coursespicked)
            .get()
            .addOnCompleteListener { task ->
                for (ds in task.result!!) {
                    val satrcourses = Records(
                        ds.id,
                        ds.getString("name")!!,
                        ds.getBoolean("present")!!,
                        ds.getString("reason")!!
                    )
                    recordsArrayList.add(satrcourses)
                }
                recordsArrayList.sortBy { it.name }
                val adapter = MyRecyclerViewAdapter(this@NominalRoll, recordsArrayList)
                mRecyclerView!!.adapter = adapter
            }
    }

    private fun setUpRecyclerView() {
        mRecyclerView = findViewById(com.tab.satr.R.id.mRecyclerView)
        mRecyclerView?.setHasFixedSize(true)
        mRecyclerView?.layoutManager = LinearLayoutManager(this)
    }

}

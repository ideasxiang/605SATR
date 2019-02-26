package com.tab.satr

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.firestore.FirebaseFirestore

class Overview : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val usercourses = db.collection("satr_courses")
    val users = db.collection("users")
    var shiftpresentvalue = 0
    var mwdspresentvalue = 0
    var nsmenpresentvalue = 0
    var shifttotalvalue = 0
    var mwdstotalvalue = 0
    var nsmentotalvalue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        getPresent()
        getTotal()
        initializeSpinner()

    }

    private fun initializeSpinner() {
        val spinner: Spinner = findViewById(R.id.year_spinner)
        spinner.onItemSelectedListener = this
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.years_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

    }

    private fun getTotal(){
        val shiftOverview = users
            .whereEqualTo("department","shift")
        val mwdsOverview = users
            .whereEqualTo("department","mwds")
        val nsmenOverview = users
            .whereEqualTo("department","nsmen")

        if (shifttotalvalue>0){
            shifttotalvalue = 0
        }
        if (mwdstotalvalue>0){
            mwdstotalvalue = 0
        }
        if (nsmentotalvalue>0){
            nsmentotalvalue = 0
        }

        shiftOverview
            .get()
            .addOnCompleteListener {task->
                for (ds in task.result!!){
                    shifttotalvalue += 1
                }
            }
        mwdsOverview
            .get()
            .addOnCompleteListener {task->
                for (ds in task.result!!){
                    mwdstotalvalue += 1
                }
            }
        nsmenOverview
            .get()
            .addOnCompleteListener {task->
                for (ds in task.result!!){
                    nsmentotalvalue += 1
                }
            }
    }

    private fun getPresent(){
        val shiftOverview = usercourses
            .whereEqualTo("present", true)
            .whereEqualTo("department","shift")
        val mwdsOverview = usercourses
            .whereEqualTo("present", true)
            .whereEqualTo("department","mwds")
        val nsmenOverview = usercourses
            .whereEqualTo("present", true)
            .whereEqualTo("department","nsmen")

        if (shiftpresentvalue>0){
            shiftpresentvalue = 0
        }
        if (mwdspresentvalue>0){
            mwdspresentvalue = 0
        }
        if (nsmenpresentvalue>0){
            nsmenpresentvalue = 0
        }

        shiftOverview
            .get()
            .addOnCompleteListener {task->
                for (ds in task.result!!){
                    shiftpresentvalue += 1
                }
            }
        mwdsOverview
            .get()
            .addOnCompleteListener {task->
                for (ds in task.result!!){
                    mwdspresentvalue += 1
                }
            }
        nsmenOverview
            .get()
            .addOnCompleteListener {task->
                for (ds in task.result!!){
                    nsmenpresentvalue += 1
                }
            }
    }
}

package com.tab.satr

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import java.text.SimpleDateFormat
import java.util.*

class Overview : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var startselecteddate: Long ?= null
    var endselecteddate: Long ?= null
    var startpreviousdate: Long ?= null
    var endpreviousdate: Long ?= null
    var shift: TextView ?= null
    var checker: TextView ?= null
    var guard: TextView ?= null
    var armed: TextView ?= null
    private lateinit var functions: FirebaseFunctions
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        functions = FirebaseFunctions.getInstance()

        initializeSpinner()
    }

    private fun updateCycle() {
        db.collection("users")
            .whereLessThan("post",startselecteddate!!-7776000L)
            .get()
            .addOnCompleteListener{Task->
                for(ds in Task.result!!){
                    db.collection("users").document(ds.id).update("cycle","2")
                }
                updateOrd()
            }
    }

    private fun updateOrd() {
        db.collection("users")
            .whereLessThan("ord",startselecteddate!!+7776000L)
            .get()
            .addOnCompleteListener { Task ->
                for (ds in Task.result!!) {
                    db.collection("users").document(ds.id).update("isord",true)
                }
                generateAll()
            }
    }

    private fun generateAll() {
        getPercent("Checker",findViewById(R.id.checker_percentage))
        getPercent("Guard Comd",findViewById(R.id.guard_percentage))
        getPercent("Armed Guard",findViewById(R.id.armed_percentage))
        getPercent("IRF",findViewById(R.id.irf_percentage))
        getPercent("Dog Handler",findViewById(R.id.mwds_percentage))
        getPercent("NS men",findViewById(R.id.nsmen_percentage))
    }

    private fun getPercent(appointment: String,view:TextView){
        db.collection("users")
            .whereEqualTo("appointment",appointment)
            .whereEqualTo("cycle","1")
            .get()
            .addOnSuccessListener {
                val qualifynumber = it.size()
                getLiveFiring(appointment,qualifynumber,view)
            }
    }

    private fun getLiveFiring(appointment: String,
                              qualifynumber: Int,
                              view:TextView){
        db.collection("satr_courses")
            .whereEqualTo("appointment",appointment)
            .whereEqualTo("course_name","Weapon Live Firing")
            .whereEqualTo("present",true)
            .whereEqualTo("cycle","2")
            .whereGreaterThan("unixdate",startpreviousdate!!)
            .whereLessThan("unixdate",endpreviousdate!!)
            .get()
            .addOnSuccessListener {
                val totallive = it.size()
                getSustain(appointment,qualifynumber,totallive,view)
            }
    }

    private fun getSustain(appointment: String,
                           qualifynumber: Int,
                           totallive: Int,
                           view:TextView){
        db.collection("users")
            .whereEqualTo("appointment", appointment)
            .whereEqualTo("cycle", "2")
            .get()
            .addOnSuccessListener {
                val sustainnumber = it.size()
                getPresent(appointment,qualifynumber,totallive,sustainnumber,view)
            }
    }

    private fun getPresent(appointment: String,
                           qualifynumber: Int,
                           totallive: Int,
                           sustainnumber: Int,
                           view:TextView) {
        db.collection("satr_courses")
            .whereEqualTo("appointment",appointment)
            .whereEqualTo("present",true)
            .whereGreaterThan("unixdate",startselecteddate!!)
            .whereLessThan("unixdate",endselecteddate!!)
            .get()
            .addOnSuccessListener {
                val presentnumber = it.size()
                var multiplyhalf = 0
                var multiplyfull = 0
                when (appointment) {
                    "Dog Handler" -> multiplyhalf = 13
                    "IRF" -> multiplyhalf = 13
                    "Checker" -> multiplyhalf = 13
                    "Guard Comd" -> multiplyhalf = 13
                    "Armed Guard" -> multiplyhalf = 13
                    "NS men" -> multiplyhalf = 13
                }
                when (appointment) {
                    "Dog Handler" -> multiplyfull = 26
                    "IRF" -> multiplyhalf = 13
                    "Checker" -> multiplyhalf = 13
                    "Guard Comd" -> multiplyhalf = 13
                    "Armed Guard" -> multiplyhalf = 13
                    "NS men" -> multiplyhalf = 13
                }
                val nolive = sustainnumber - totallive
                val totalcourses = qualifynumber * multiplyfull + totallive * (multiplyhalf - 1) + nolive * multiplyhalf
                val percentage = (presentnumber.toDouble())/totalcourses
                view.text = String.format("%.2f",percentage*100).plus("%")
            }
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
        when(parent.selectedItem.toString()){
            "Jan 2019" -> {
                startselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("31-12-2018").time/1000L
                endselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("01-07-2019").time/1000L
                startpreviousdate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("31-06-2018").time/1000L
                endpreviousdate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("01-01-2018").time/1000L}
            "Jul 2019" -> {
                startselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("30-06-2019").time/1000L
                endselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("01-01-2020").time/1000L
                startpreviousdate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("31-12-2018").time/1000L
                endpreviousdate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("01-07-2019").time/1000L
            }
            "Jan 2020" -> {
                startselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("31-12-2019").time/1000L
                endselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("01-07-2020").time/1000L
                startpreviousdate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("30-06-2019").time/1000L
                endpreviousdate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("01-01-2020").time/1000L
            }
            "Jul 2020" -> {
                startselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("30-06-2020").time/1000L
                endselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("01-01-2021").time/1000L
                startpreviousdate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("31-12-2019").time/1000L
                endpreviousdate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("01-07-2020").time/1000L
            }
            "Jan 2021" -> {
                startselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("31-12-2020").time/1000L
                endselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("01-07-2021").time/1000L
                startpreviousdate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("30-06-2020").time/1000L
                endpreviousdate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("01-01-2021").time/1000L
            }
            "Jul 2021" -> {
                startselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("30-06-2021").time/1000L
                endselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("01-01-2022").time/1000L
                startpreviousdate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("31-12-2020").time/1000L
                endpreviousdate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("01-07-2021").time/1000L
            }
            "Jan 2022" -> {
                startselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("31-12-2021").time/1000L
                endselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("01-07-2022").time/1000L
                startpreviousdate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("30-06-2021").time/1000L
                endpreviousdate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("01-01-2022").time/1000L
            }
            "Jul 2022" -> {
                startselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("30-06-2022").time/1000L
                endselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("01-01-2023").time/1000L
                startpreviousdate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("31-12-2021").time/1000L
                endpreviousdate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("01-07-2022").time/1000L
            }
        }
        updateCycle()
    }
}

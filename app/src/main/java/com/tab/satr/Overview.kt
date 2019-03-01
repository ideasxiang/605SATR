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
    var shiftpercentage: TextView ?= null
    var mwdspercentage: TextView ?= null
    var nsmenpercentage: TextView ?= null
    var startselecteddate: Long ?= null
    var endselecteddate: Long ?= null
    private lateinit var functions: FirebaseFunctions
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        functions = FirebaseFunctions.getInstance()

        shiftpercentage =  findViewById(R.id.shift_percentage)
        mwdspercentage = findViewById(R.id.mwds_percentage)
        nsmenpercentage = findViewById(R.id.nsmen_percentage)

        initializeSpinner()
        getPercent("shift1",shiftpercentage!!)
    }

    private fun getPercent(department: String,viewchange: TextView){
        db.collection("users")
            .whereEqualTo("department",department)
            .whereEqualTo("cycle","1")
            .get()
            .addOnSuccessListener {result->
                var multiplynumber = 0
                when (department){
                    "shift" -> multiplynumber = 1
                    "irf" -> multiplynumber = 1
                    "mwds" -> multiplynumber = 1
                    "nsmen" -> multiplynumber = 1
                }
                val totalnumberqualify = result.size()*multiplynumber
                getsustainnumber(department,viewchange,totalnumberqualify)
            }
    }

    private fun getsustainnumber(department: String,viewchange: TextView,totalnumberqualify: Int) {
        db.collection("users")
            .whereEqualTo("department",department)
            .whereEqualTo("cycle","2")
            .get()
            .addOnSuccessListener {result->
                var multiplynumber = 0
                when (department){
                    "shift" -> multiplynumber = 1
                    "irf" -> multiplynumber = 1
                    "mwds" -> multiplynumber = 1
                    "nsmen" -> multiplynumber = 1
                }
                val totalnumbersustain = result.size()*multiplynumber
                val totalnumber = totalnumberqualify + totalnumbersustain
                getPresent(department,viewchange,totalnumber)
            }
    }

    private fun getPresent(department: String,viewchange: TextView, totalnummber: Int) {
        db.collection("satr_courses")
            .whereEqualTo("department",department)
            .whereEqualTo("present",true)
            .whereGreaterThan("unixdate",startselecteddate!!)
            .whereLessThan("unixdate",endselecteddate!!)
            .get()
            .addOnSuccessListener {result->
                val presentnumber = result.size()
                viewchange.text = (presentnumber/totalnummber).toString()
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
                startselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse("31-12-2018").time
                endselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse("01-07-2019").time}
            "July 2019" -> {
                startselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse("30-06-2019").time
                endselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse("01-01-2020").time}
            "Jan 2020" -> {
                startselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse("31-12-2019").time
                endselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse("01-07-2020").time}
            "July 2020" -> {
                startselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse("30-06-2020").time
                endselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse("01-01-2021").time}
            "Jan 2021" -> {
                startselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse("31-12-2020").time
                endselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse("01-07-2021").time}
            "July 2021" -> {
                startselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse("30-06-2021").time
                endselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse("01-01-2022").time}
            "Jan 2022" -> {
                startselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse("31-12-2021").time
                endselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse("01-07-2022").time}
            "July 2022" -> {
                startselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse("30-06-2022").time
                endselecteddate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse("01-01-2023").time}

        }
    }
}
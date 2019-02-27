package com.tab.satr

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions

class Overview : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var shiftpercentage: TextView ?= null
    var mwdspercentage: TextView ?= null
    var nsmenpercentage: TextView ?= null
    var shifttotal: String ?= null
    private lateinit var functions: FirebaseFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        shiftpercentage =  findViewById(R.id.shift_percentage)
        mwdspercentage = findViewById(R.id.mwds_percentage)
        nsmenpercentage = findViewById(R.id.nsmen_percentage)

        initializeSpinner()

        functions = FirebaseFunctions.getInstance()

        shiftpercentage!!.text = shifttotal
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
            "Jan 2019" -> {Toast.makeText(this,"Jan 2019",Toast.LENGTH_SHORT).show()}
        }
    }
}

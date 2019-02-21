package com.tab.satr.nominalroll

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.CalendarView


class DatePicker : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tab.satr.R.layout.activity_date_picker)

        var calendarView = findViewById<CalendarView>(com.tab.satr.R.id.calendarView)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val intent = Intent(this, NominalRoll::class.java)
            startActivity(intent)
        }
    }
}

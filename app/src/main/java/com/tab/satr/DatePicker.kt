package com.tab.satr

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.CalendarView
import com.tab.satr.nominalroll.NominalRoll


class DatePicker : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker)

        var calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val intent = Intent(this, NominalRoll::class.java)
            intent.putExtra("dayOfMonth", dayOfMonth)
            intent.putExtra("month", month)
            intent.putExtra("year", year)
            startActivity(intent)
        }
    }
}

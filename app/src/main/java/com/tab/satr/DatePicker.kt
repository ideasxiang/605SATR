package com.tab.satr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.CalendarView
import com.tab.satr.nominalroll.NominalRoll
import java.text.SimpleDateFormat
import java.util.*

class DatePicker : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val editor = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE).edit()
        calendarView.setOnDateChangeListener{ _, year, month, dayOfMonth ->
            val intent = Intent(this, NominalRoll::class.java)
            val actualmonth = month+1
            val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("$dayOfMonth-$actualmonth-$year").time
            editor.putLong("unixdate",date/1000L).apply()
            startActivity(intent)
        }
    }
}

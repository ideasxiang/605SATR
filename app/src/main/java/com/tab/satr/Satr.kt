package com.tab.satr

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.tab.satr.nominalroll.DatePicker
import com.tab.satr.nominalroll.NominalRoll

class Satr : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_satr)

        val imgShift = findViewById<ImageView>(R.id.imgShift)
        val imgMwds = findViewById<ImageView>(R.id.imgMwds)
        val imgNsmen = findViewById<ImageView>(R.id.imgNsmen)

        imgShift.setOnClickListener(){
           val intent = Intent(this, DatePicker::class.java)
            startActivity(intent)
        }
        imgMwds.setOnClickListener(){
            val intent = Intent(this, DatePicker::class.java)
            startActivity(intent)
        }
        imgNsmen.setOnClickListener(){
            val intent = Intent(this, DatePicker::class.java)
            startActivity(intent)
        }
    }
}

package com.tab.satr

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class DashBoard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        val imgSatr = findViewById<ImageView>(R.id.imgSatr)
        val imgParadeState = findViewById<ImageView>(R.id.imgParadeState)

        imgSatr.setOnClickListener(){
            val intent = Intent(this, Satr::class.java)
            startActivity(intent)
        }

        imgParadeState.setOnClickListener(){
            val intent = Intent(this, ParadeState::class.java)
            startActivity(intent)
        }
    }
}

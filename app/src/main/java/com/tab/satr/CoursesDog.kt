package com.tab.satr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView


class CoursesDog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses_dog)

        val imgfence = findViewById<ImageView>(R.id.fence)
        val imghandcuff = findViewById<ImageView>(R.id.handcuff)
        val imgattack = findViewById<ImageView>(R.id.attack)
        val imgdog = findViewById<ImageView>(R.id.dog)
        val imgbomb = findViewById<ImageView>(R.id.bomb)
        val imgrules = findViewById<ImageView>(R.id.rules)
        val imglivefiring = findViewById<ImageView>(R.id.livefiring)
        val editor = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE).edit()
        
        imgfence.setOnClickListener{
            val intent = Intent(this, DatePicker::class.java)
            editor.putString("courses", "Perimeter Protection Operations").apply()
            startActivity(intent)
        }
        imghandcuff.setOnClickListener{
            val intent = Intent(this, DatePicker::class.java)
            editor.putString("courses", "Search and Arrest").apply()
            startActivity(intent)
        }
        imgdog.setOnClickListener{
            val intent = Intent(this, DatePicker::class.java)
            editor.putString("courses", "MWDS Handling Technique").apply()
            startActivity(intent)
        }
        imgbomb.setOnClickListener{
            val intent = Intent(this, DatePicker::class.java)
            editor.putString("courses", "Explosive Search Training").apply()
            startActivity(intent)
        }
        imgrules.setOnClickListener{
            val intent = Intent(this, DatePicker::class.java)
            editor.putString("courses", "Rules of Engagement").apply()
            startActivity(intent)
        }
        imglivefiring.setOnClickListener{
            val intent = Intent(this, DatePicker::class.java)
            editor.putString("courses", "Weapon Live Firing").apply()
            startActivity(intent)
        }
        imgattack.setOnClickListener{
            val intent = Intent(this, DatePicker::class.java)
            editor.putString("courses", "Attack Work Training").apply()
            startActivity(intent)
        }
    }
}

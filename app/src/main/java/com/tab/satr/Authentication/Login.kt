package com.tab.satr.Authentication

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.tab.satr.DashBoard
import com.tab.satr.R


class Login : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private val s = Login::class.toString()

    private var counter = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        val btnRegister = findViewById<Button>(R.id.btn_register)

        mAuth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            userLogin()
        }

        btnCancel.setOnClickListener {
            System.exit(0)
        }

        btnRegister.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

    }

    private fun userLogin(){

        val textCounter = findViewById<TextView>(R.id.text_counter)

        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val username: String = editTextUsername.text.toString().trim()
        val password: String = editTextPassword.text.toString().trim()

        val btnLogin = findViewById<Button>(R.id.btn_login)

        if (username.isEmpty()) {
            editTextUsername.error = "Username is required"
            editTextUsername.requestFocus()
            return
        }

        if (password.isEmpty()) {
            editTextPassword.error = "Password is required"
            editTextPassword.requestFocus()
            return
        }

        if (password.length < 6) {
            editTextPassword.error = "Minimum length of password should be 6"
            editTextPassword.requestFocus()
            return
        }

        textCounter.visibility = View.VISIBLE
        textCounter.setTextColor(Color.RED)

        if (counter == 0) {
            btnLogin.isEnabled = false
        }

        mAuth!!
                .signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, DashBoard::class.java)
                        startActivity(intent)
                        Log.d(s, "Login Successful")
                    } else {
                        Toast.makeText(this, "Wrong Credentials", Toast.LENGTH_SHORT).show()
                        Log.d(s, "Failed to Login")
                        counter--
                        textCounter.text = "$counter"
                    }
                }
    }
}

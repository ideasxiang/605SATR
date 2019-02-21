package com.tab.satr.Authentication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tab.satr.R
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class Register : AppCompatActivity() {

    //UI elements
    private var etUserName: EditText? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var btnCreateAccount: Button? = null
    private var mAuth: FirebaseAuth? = null
    private val s = Register::class.toString()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnCreateAccount = findViewById<View>(R.id.btn_register) as Button

        mAuth = FirebaseAuth.getInstance()

        btnCreateAccount!!.setOnClickListener { createNewAccount() }

    }

    private fun createNewAccount() {

        val etUserName = findViewById<View>(R.id.et_user_name) as EditText
        val etPassword = findViewById<View>(R.id.et_password) as EditText
        val etEmail = findViewById<View>(R.id.et_email) as EditText

        val username: String = etUserName.text.toString().trim()
        val password: String = etPassword.text.toString().trim()
        val email: String = etEmail.text.toString().trim()

        if (username.isEmpty()) {
            etUserName.error = "Full Name is required"
            etUserName.requestFocus()
            return
        }

        if (email.isEmpty()) {
            etEmail.error = "Email is required"
            etEmail.requestFocus()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Proper email needed"
            etEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            etPassword.error = "Password is required"
            etPassword.requestFocus()
            return
        }

        if (password.length <7) {
            etPassword.error = "Minimum 8 characters"
            etPassword.requestFocus()
            return
        }

        /*val data = HashMap<String, Any>()
        data["userName"] = etUserName?.text.toString()*/

        mAuth!!
            .createUserWithEmailAndPassword(etEmail.text.toString(),etPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(s, "createUserWithEmail:success")
                    Toast.makeText(this, "Success",
                        Toast.LENGTH_SHORT).show()
                    /*db.collection("users")
                        .add(data)
                        .addOnSuccessListener { documentReference ->
                            Log.d(s, "DocumentSnapshot written with ID: " + documentReference.id)
                        }
                        .addOnFailureListener { e ->
                            Log.w(s, "Error adding document", e)
                        }*/
                    val intent = Intent(this, Login::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(s, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }
}

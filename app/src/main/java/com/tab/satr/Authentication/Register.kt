package com.tab.satr.Authentication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.Timestamp
import com.tab.satr.R
import java.util.*

class Register : AppCompatActivity() {

    //UI elements
    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var btnCreateAccount: Button? = null
    private var mAuth: FirebaseAuth? = null
    private val s = Register::class.toString()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etFirstName = findViewById<View>(R.id.et_first_name) as EditText
        etLastName = findViewById<View>(R.id.et_last_name) as EditText
        etEmail = findViewById<View>(R.id.et_email) as EditText
        etPassword = findViewById<View>(R.id.et_password) as EditText
        btnCreateAccount = findViewById<View>(R.id.btn_register) as Button

        mAuth = FirebaseAuth.getInstance()

        btnCreateAccount!!.setOnClickListener { createNewAccount() }

    }

    private fun createNewAccount() {
        val data = HashMap<String, Any>()
        data["firstName"] = etFirstName?.text.toString()
        data["lastName"] = etLastName?.text.toString()
        data["dateExample"] = Timestamp(Date())


        /*firstName = etFirstName?.text.toString()
        lastName = etLastName?.text.toString()
        email = etEmail?.text.toString()
        password = etPassword?.text.toString()*/

        mAuth!!
            .createUserWithEmailAndPassword(etEmail?.text.toString(),etPassword?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(s, "createUserWithEmail:success")
                    Toast.makeText(this, "Success",
                        Toast.LENGTH_SHORT).show()
                    db.collection("users")
                        .add(data)
                        .addOnSuccessListener { documentReference ->
                            Log.d(s, "DocumentSnapshot written with ID: " + documentReference.id)
                        }
                        .addOnFailureListener { e ->
                            Log.w(s, "Error adding document", e)
                        }
                    val intent = Intent(this, Login::class.java)
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

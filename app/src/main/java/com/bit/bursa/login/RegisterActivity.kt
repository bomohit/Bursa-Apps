package com.bit.bursa.login

import android.content.Context
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bit.bursa.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity()  {
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_layout)

        val signupButton : Button = findViewById(R.id.buttonSubmitSignup)

        signupButton.setOnClickListener {

            // get the input
            val inputUsername : EditText = findViewById(R.id.inputUsername)
            val inputEmail : EditText = findViewById(R.id.inputEmail)
            val inputPassword : EditText = findViewById(R.id.inputPassword)
            val ly : ConstraintLayout = findViewById(R.id.layoutRegister)

            ly.setOnClickListener {
                closeKeyBoard(it)
            }

            // validate the input
            if (!valid(inputUsername, inputEmail, inputPassword)) {
                d("bomoh", "invalid input on username, email or password")
            } else {
                var taken = false
                // check if the username and email already taken or not
                db.collection("login")
                        .get()
                        .addOnSuccessListener { result ->
                            for (results in result) {
                                d("bomoh", "result data: ${results.data}")
                                if (results.getField<String>("username").toString() == inputUsername.text.toString()) {
                                    d("bomoh", "username Taken")
                                    inputUsername.error = "username taken"
                                    taken = true
                                }
                                if (results.getField<String>("email").toString() == inputEmail.text.toString()) {
                                    d("bomoh", "email Taken")
                                    inputEmail.error = "email taken"
                                    taken = true
                                }
                            }
                            if (!taken) {
                                d("bomoh", "registering")
                                val data = hashMapOf(
                                        "username" to inputUsername.text.toString(),
                                        "email" to inputEmail.text.toString(),
                                        "password" to inputPassword.text.toString(),
                                        "balance" to "0"
                                )
                                db.collection("login").document(inputEmail.text.toString())
                                        .set(data)
                                        .addOnSuccessListener {
                                            d("bomoh", "register success")
                                        }
                            }
                        }

            }


        }

    }

    private fun valid(inputUsername: EditText, inputEmail: EditText, inputPassword: EditText): Boolean {
        var valid = true
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        if (inputUsername.text.toString().isNullOrEmpty()) {
            inputUsername.error = "Required!"
            valid = false
            d("bomoh", "username empty")
        } else {
            inputUsername.error = null
        }

        if (inputEmail.text.toString().isNullOrEmpty()) {
            inputEmail.error = "Required!"
            valid = false
            d("bomoh", "email empty")
        } else {
            inputEmail.error = null
            if (!inputEmail.text.toString().matches(emailPattern.toRegex())){
                inputEmail.error = "invalid Email"
                valid = false
            } else {
                inputEmail.error = null
            }
            // add logic here for checking if the email is correct
        }

        if (inputPassword.text.toString().isNullOrEmpty()) {
            inputPassword.error = "Required!"
            valid = false
            d("bomoh", "password empty")
        } else {
            inputPassword.error = null
        }
        return valid
    }

    private fun closeKeyBoard(v : View) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }
}
package com.bit.bursa.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.bit.bursa.MainActivity
import com.bit.bursa.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin_layout)

        val loginUsername : EditText = findViewById(R.id.loginUsername)
        val loginPassword : EditText = findViewById(R.id.loginPassword)
        val login : Button = findViewById(R.id.buttonLogin)
        val ly: ConstraintLayout = findViewById(R.id.layoutSignIn)

        ly.setOnClickListener {
            closeKeyBoard(it)
        }

        login.setOnClickListener {
            if (!valid(loginUsername, loginPassword)) {
                return@setOnClickListener
            } else {
                var valid = false
                db.collection("login")
                        .get()
                        .addOnSuccessListener { result ->
                            for (results in result) {
                                if (results.getField<String>("username").toString() == loginUsername.text.toString()) {
                                    //if true , check password
                                    if (results.getField<String>("password").toString() == loginPassword.text.toString()) {
                                        //if true , log in user
                                        val intent = Intent(Intent(this, MainActivity::class.java))
                                        intent.putExtra("email", results.getField<String>("email").toString())
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                                Intent.FLAG_ACTIVITY_NEW_TASK)

                                        startActivity(intent)
                                        finish()
                                        d("bomoh", "log in success")
                                        valid = true
                                    } else {
                                        d("bomoh", "login failed : wrong password")
                                    }
                                }
                            }
                            if (!valid){
                                d("bomoh", "account not exist")
                            }
                        }
            }
        }



    }

    private fun valid(inputUsername: EditText, inputPassword: EditText): Boolean {
        var valid = true

        if (inputUsername.text.toString().isNullOrEmpty()) {
            inputUsername.error = "Required!"
            valid = false
            Log.d("bomoh", "username empty")
        } else {
            inputUsername.error = null
        }

        if (inputPassword.text.toString().isNullOrEmpty()) {
            inputPassword.error = "Required!"
            valid = false
            Log.d("bomoh", "password empty")
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
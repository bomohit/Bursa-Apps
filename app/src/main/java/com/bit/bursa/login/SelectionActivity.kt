package com.bit.bursa.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bit.bursa.MainActivity
import com.bit.bursa.R
import com.google.firebase.firestore.ktx.getField

class SelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selection_main)
//        val register : Button = findViewById(R.id.se_buttonRegister)
//        val login : Button = findViewById(R.id.sel_buttonLogin)

//        register.setOnClickListener {
//            val intent = Intent(Intent(this, RegisterActivity::class.java))
//            startActivity(intent)
//        }
//
//        login.setOnClickListener {
//            val intent = Intent(Intent(this, SignInActivity::class.java))
//            startActivity(intent)
//        }

    }
}
package com.bit.bursa

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.bit.bursa.login.LoginViewModel
import com.bit.bursa.login.LoginViewModelFactory
import com.bit.bursa.login.SelectionActivity
import com.bit.bursa.login.SignInActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        
        val viewModelFactory = LoginViewModelFactory(intent.getStringExtra("email").toString())

        val em = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java).myEmail
        d("bomoh", "Main: $em")

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                signOut()
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun signOut() : Boolean {
        val intent = Intent(this, SelectionActivity::class.java)
        startActivity(intent)
        finish()

        return true
    }
}
package com.bit.bursa.login

import android.util.Log.d
import androidx.lifecycle.ViewModel

class LoginViewModel(email: String) : ViewModel() {

    var myEmail = email

    init {
        d("bomoh", "viewmodel: $myEmail")
    }

}
package com.bit.bursa.portfolio

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.bit.bursa.R
import com.bit.bursa.login.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import java.lang.IllegalArgumentException

class AddCapitalFragment : Fragment() {
    val db = Firebase.firestore

    var currentBalance : Double? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_add_capital, container, false)
        val email = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java).myEmail
        // display balance
        val balance = db.collection("login").document(email)
        balance.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                "${snapshot.getField<String>("balance")} MYR".also { root.findViewById<TextView>(R.id.ac_balance).text = it }
                currentBalance = snapshot.getField<String>("balance").toString().toDouble()
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java).myEmail
        val buttonPlus = view.findViewById<Button>(R.id.ac_buttonCapitalPlus)
        val buttonMinus = view.findViewById<Button>(R.id.ac_buttonCapitalMinus)
        val buttonConfirm = view.findViewById<Button>(R.id.ac_buttonConfirm)

        val ac_count = view.findViewById<TextView>(R.id.ac_count)
        val balanceAfter = view.findViewById<TextView>(R.id.ac_AfterAdding)

        buttonPlus.setOnClickListener {
            if (ac_count.text.toString().isNullOrEmpty()) {
                ac_count.text = "1"
                return@setOnClickListener
            }
            val current = ac_count.text.toString().removeSuffix(" MYR").toDouble() + 1
//            ac_count.text = "$current MYR"
            ac_count.text = current.toString()
            //balance after adding
            balanceAfter.text = "${currentBalance!! + current} MYR"
        }

        buttonMinus.setOnClickListener {
            if (ac_count.text.toString().isNullOrEmpty()) {
                ac_count.text = "1"
                return@setOnClickListener
            }
            if (ac_count.text.toString().removeSuffix(" MYR").toDouble() > 0) {

                val current = ac_count.text.toString().removeSuffix(" MYR").toDouble() - 1
                ac_count.text = current.toString()
                //balance after adding
                balanceAfter.text = "${currentBalance!! + current} MYR"
            }
        }

        buttonConfirm.setOnClickListener {
        val data = hashMapOf(
            "balance" to balanceAfter.text.toString().removeSuffix(" MYR")
        )
            db.collection("login").document(email)
                .set(data, SetOptions.merge())
            Snackbar.make(view, "BALANCE UPDATED", Snackbar.LENGTH_LONG).show()

        }

        ac_count.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(t: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    val current = t.toString().toDouble()
                    balanceAfter.text = "${currentBalance!! + current} MYR"
                }catch (e: IllegalArgumentException) {
                    balanceAfter.text = "$currentBalance MYR"
                }
//                d("bomoh", "current: $current")

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }
}
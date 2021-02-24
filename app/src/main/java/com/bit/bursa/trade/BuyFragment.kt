package com.bit.bursa.trade

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bit.bursa.R
import com.bit.bursa.login.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BuyFragment : Fragment() {
    val db = Firebase.firestore
    var stock_Price : Double? = null
    var ac_balance : Double? = null
    val totalFees = 7 // Fees
    var stock_klse : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_buy_or_sell, container, false)
        val stockName = arguments?.getString("stock_name").toString()
        val bo_balance : TextView = root.findViewById(R.id.bo_balance)
        val bo_stock : TextView = root.findViewById(R.id.bo_stock)

        val email = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java).myEmail
        val balance = db.collection("login").document(email)
        balance.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
               val bal = snapshot.getField<String>("balance").toString()
                bo_balance.text = "$bal MYR"
                ac_balance = bal.toDouble()
            }
        }

        val stockPrice = db.collection("stock").document(stockName)
        stockPrice.addSnapshotListener { snapshot, error ->
            if (error != null ) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val sto = snapshot.getField<String>("stock_price").toString()
                val kl = snapshot.getField<String>("stock_klse").toString()
                bo_stock.text = sto
                stock_Price = sto.toDouble()
                stock_klse = kl
                d("bomoh", "StockPrice = $stock_Price")
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java).myEmail
        val stockName = arguments?.getString("stock_name").toString()

        val buttonPlus : Button = view.findViewById(R.id.buttonPlus)
        val buttonMinus : Button = view.findViewById(R.id.buttonMinus)
        val bo_count : TextView = view.findViewById(R.id.bo_count)

        val buttonSubmit : Button = view.findViewById(R.id.buttonBO)
        val bo_totalCost : TextView = view.findViewById(R.id.bo_totalCost)
        val bo_totalFees : TextView = view.findViewById(R.id.bo_totalFees)
        val bo_totalSpending : TextView = view.findViewById(R.id.bo_totalSpending)
        var total : Double = 0.00

        // to increase or decrease stock unit
        buttonPlus.setOnClickListener {
            val current = bo_count.text.toString().toInt() + 10

            val totalCost = current * stock_Price!!
            val totalSpending = totalCost+totalFees

            if (totalSpending <= ac_balance!!) {
                bo_count.text = current.toString()
                "$totalCost MYR".also { bo_totalCost.text = it }
                "$totalSpending MYR".also { bo_totalSpending.text = it }
                total = totalSpending
            } else {
                Toast.makeText(requireContext(), "Insufficient Balance", Toast.LENGTH_SHORT).show()
            }
        }
        buttonMinus.setOnClickListener {
            val current = bo_count.text.toString().toInt() - 10
            if (current >= 0) {
                bo_count.text = current.toString()
                val totalCost = current * stock_Price!!
                var totalSpending = totalCost+totalFees
                if (current == 0) {
                    totalSpending = 0.00
                }
                "$totalCost MYR".also { bo_totalCost.text = it }
                "$totalSpending MYR".also { bo_totalSpending.text = it }
                total = totalSpending
            }else {
                "0.00 MYR".also { bo_totalCost.text = it }
                "0.00 MYR".also { bo_totalSpending.text = it }
                Toast.makeText(requireContext(), "Cannot go lower", Toast.LENGTH_SHORT).show()
            }
        }

        buttonSubmit.setOnClickListener {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatted = current.format(formatter)

            val data = hashMapOf(
                "name" to stockName,
                "activity" to "buy",
                "quantity" to bo_count.text.toString(),
                "price" to stock_Price.toString(),
                "klse" to stock_klse
            )
            // UPDATE LOG
            db.collection("user").document("log").collection(email).document(formatted.toString())
                .set(data)
                .addOnSuccessListener {
                    Snackbar.make(view, "BUY SUCCESS", Snackbar.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    Snackbar.make(view, "BUY FAILED: $e", Snackbar.LENGTH_LONG).show()
                }

            // UPDATE BALANCE
            val df = DecimalFormat("#.##")
            val data2 = hashMapOf(
                "balance" to df.format(ac_balance!!-total).toString()
            )
            db.collection("login").document(email)
                .set(data2 , SetOptions.merge())
                .addOnSuccessListener {
                    d("bomoh", "update balance success")
                }
                .addOnFailureListener { e ->
                    d("bomoh", "update balance failed: $e")
                }

            // UPDATE STOCK OPTIONS
            db.collection("user").document("portfolio").collection(email).document(stockName)
                .get()
                .addOnSuccessListener {
                    val quantity = it.getField<String>("quantity").toString()
                    var quantity2 = 0
                    d("bomoh", "quantity2: $quantity")
                    if (!quantity.isNullOrEmpty()) {
                        quantity2 = if (quantity == "null") {
                            bo_count.text.toString().toInt()
                        } else {
                            bo_count.text.toString().toInt() + quantity.toInt()
                        }

                        val data3 = hashMapOf(
                            "quantity" to quantity2.toString()
                        )
                        db.collection("user").document("portfolio").collection(email).document(stockName)
                            .set(data3, SetOptions.merge())
                    }
                }
        }

    }
}
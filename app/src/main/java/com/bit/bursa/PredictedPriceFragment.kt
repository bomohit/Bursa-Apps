package com.bit.bursa

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase


class PredictedPriceFragment : Fragment() {
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_predicted_price, container, false)
        val buttonSearch : Button = root.findViewById(R.id.p_buttonSearch)
        val searchKey : TextView = root.findViewById(R.id.p_searchKey)
        val recyclerView : RecyclerView = root.findViewById(R.id.PredictedRecyclerView)
        val lay : ConstraintLayout = root.findViewById(R.id.frameLayout4)

        lay.setOnClickListener {
            closeKeyBoard(it)
        }

        val predictedList = mutableListOf<PredictedList>()

        fun rv() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@PredictedPriceFragment.context)
                adapter = PredictedPriceAdapter(predictedList)
            }
        }

        buttonSearch.setOnClickListener {
            closeKeyBoard(it)
            if (searchKey.text.toString().isNotEmpty()) {
                predictedList.clear()
                db.collection("predicted")
                    .get()
                    .addOnSuccessListener { result ->
                        for (results in result) {
                            if (results.id.contains(searchKey.text.toString())) {
                                val stockName = results.id
                                val p_price = results.getField<String>("price").toString()
                                db.collection("stock").document(stockName)
                                    .get()
                                    .addOnSuccessListener {
                                        val klse = it.getField<String>("stock_klse").toString()
                                        val c_price = it.getField<String>("stock_price").toString()
                                        predictedList.add(PredictedList(stockName, klse, c_price, p_price))
                                        rv()
                                    }
                            }

                        }

                    }
            }
        }

        return root
    }


    private fun closeKeyBoard(v : View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}

data class PredictedList (
    val name : String,
    val klse : String,
    val price : String,
    val predicted : String
)

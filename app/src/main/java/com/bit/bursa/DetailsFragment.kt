package com.bit.bursa

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bit.bursa.login.LoginViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import java.lang.IllegalArgumentException
import java.lang.RuntimeException


class DetailsFragment : Fragment() {
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_details, container, false)
        val stockName = arguments?.getString("stock_name").toString()
        val lay = root.findViewById<ConstraintLayout>(R.id.layoutBuySell)
        try {
            val email = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java).myEmail
        }catch (e: RuntimeException) {
            lay.isVisible = false
        }


        d("bomoh", "detailsStockName: $stockName")

        db.collection("stock").document(stockName)
            .get()
            .addOnSuccessListener {
                val founder = it.getField<String>("founder").toString()
                val founded = it.getField<String>("founded").toString()
                val companyDetail = it.getField<String>("company_detail").toString()
                val ceo = it.getField<String>("ceo").toString()
                val klse = it.getField<String>("stock_klse").toString()
                val price = it.getField<String>("stock_price").toString()
                val target = it.getField<String>("price_target").toString()
                val marketCap = it.getField<String>("market_cap").toString()

                root.findViewById<TextView>(R.id.detailsName).text = stockName
                root.findViewById<TextView>(R.id.detailsPrice).text = "$price MYR"
                root.findViewById<TextView>(R.id.detailsKlseName).text = klse
                root.findViewById<TextView>(R.id.detailsPriceTarget).text = target
                root.findViewById<TextView>(R.id.detailsFounder).text = "Founder: $founder"
                root.findViewById<TextView>(R.id.detailsFounded).text = "Founded: $founded"
                root.findViewById<TextView>(R.id.detailsCeo).text = "CEO: $ceo"
                root.findViewById<TextView>(R.id.detailsCompany).text = companyDetail
                root.findViewById<TextView>(R.id.detailsMarketCap).text = "Market Cap: $marketCap"
            }


        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.buttonBuy).setOnClickListener {
            val stockName = arguments?.getString("stock_name").toString()
            val bundle = bundleOf("stock_name" to stockName)
            try {
                findNavController().navigate(R.id.action_detailsFragment_to_buyOrSellFragment, bundle)
            } catch (e: IllegalArgumentException) {
                d("bomoh", "IllegalArgumentException:DetailsFragment > buy")
            }

        }

        view.findViewById<Button>(R.id.buttonSell).setOnClickListener {
            val stockName = arguments?.getString("stock_name").toString()
            val bundle = bundleOf("stock_name" to stockName)
            try {
                findNavController().navigate(R.id.action_detailsFragment_to_sellFragment, bundle)
            } catch (e: IllegalArgumentException) {
                d("bomoh", "IllegalArgumentException:DetailsFragment > buy")
            }
        }

    }
}
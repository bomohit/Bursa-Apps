package com.bit.bursa.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bit.bursa.R
import java.lang.IllegalArgumentException

class SelectionFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.selection_layout, container, false)


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val register : Button = view.findViewById(R.id.se_buttonRegister)
        val login : Button = view.findViewById(R.id.sel_buttonLogin)
        val search : Button = view.findViewById(R.id.se_search)
        val input : TextView = view.findViewById(R.id.se_input)

        register.setOnClickListener {
            val intent = Intent(Intent(context, RegisterActivity::class.java))
            startActivity(intent)
        }

        login.setOnClickListener {
            val intent = Intent(Intent(context, SignInActivity::class.java))
            startActivity(intent)
        }

        search.setOnClickListener {
            if (!input.text.toString().isNullOrEmpty()) {
                val bundle = bundleOf("search_input" to input.text.toString())
                try {
                    view.findNavController().navigate(R.id.action_selectionFragment_to_searchFragment2, bundle)
                }catch (e: IllegalArgumentException) {
                    Log.d("bomoh", "Search Illegal Assumption : SelectionFragment")
                }
            }
        }
    }
}
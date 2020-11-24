package com.example.su_android_app.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.su_android_app.MainActivity
import com.example.su_android_app.R
import com.example.su_android_app.connection.Api
import com.example.su_android_app.connection.VolleyObject
import com.example.su_android_app.data.EditTextState
import com.example.su_android_app.extra.JsonParser
import com.example.su_android_app.extra.UiUtils
import com.example.su_android_app.fragments.adapters.FormAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FormFragment : Fragment() {
    private lateinit var button: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: FormAdapter
    private lateinit var category: TextView
    private var categoryId: Int = -1
    private lateinit var categoryText: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_form, container, false)
        category = view.findViewById(R.id.category)
        button = view.findViewById(R.id.ok_button)
        categoryId = arguments?.getInt("category_id")!!
        categoryText = arguments?.getString("category_text")!!
        category.text = categoryText

        button.setOnClickListener {
            if (checkInput()) {
                UiUtils.hideKeyboard(activity as MainActivity)
                sendTicket()
            } else
                Toast.makeText(context, "Fill in all the fields", Toast.LENGTH_SHORT).show()
        }


        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            viewAdapter = FormAdapter()
            adapter = viewAdapter
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(activity)
                    .setMessage("Go back?")
                    .setPositiveButton("Yes") { dialog, id ->
                        UiUtils.hideKeyboard(activity as MainActivity)
                        goBack()
                    }
                    .setNegativeButton("Cancel") { dialog, id ->
                        dialog.cancel()
                    }
                builder.create().show()

            }
        })

        getQuestions()
        return view
    }

    private fun sendTicket() {
        val request = object : StringRequest(
            Method.POST, Api.NEW_TICKET,
            Response.Listener<String> { response ->
                goBack()
            },
            Response.ErrorListener {
            }) {
            override fun getParams(): Map<String, String> {
                val params = mutableMapOf<String, String>()
                params["token"] = Api.TOKEN
                params["cat_id"] = categoryId.toString()
                params["tg_id"] = "0"
                params["answers"] = viewAdapter.questions.joinToString(prefix = "{", postfix = "}")
                println(params)
                return params

            }
        }
        VolleyObject.getInstance(requireContext().applicationContext)
            .addToRequestQueue(request)

    }

    private fun getQuestions() {
        val request = object : StringRequest(
            Method.POST, Api.GET_QUESTIONS,
            Response.Listener<String> { response ->
                viewAdapter.questions = JsonParser.parseQuestions(response)
                viewAdapter.notifyDataSetChanged()
            },
            Response.ErrorListener {
                println(it.message)
            }) {
            override fun getParams(): Map<String, String> {
                val params = mutableMapOf<String, String>()
                params["token"] = Api.TOKEN
                params["cat_id"] = categoryId.toString()
                return params

            }

        }

        VolleyObject.getInstance(requireContext().applicationContext)
            .addToRequestQueue(request)
    }

    private fun checkInput(): Boolean {
        var firstMistakePos: Int = -1
        for ((index, question) in viewAdapter.questions.withIndex()) {
            val prevState = question.state
            if (question.check())
                question.state = EditTextState.OK
            else {
                question.state = EditTextState.ERROR
                if (firstMistakePos == -1)
                    firstMistakePos = index
            }
            if (prevState != question.state)
                viewAdapter.notifyItemChanged(index)
        }
        recyclerView.layoutManager?.scrollToPosition(firstMistakePos)


        return firstMistakePos == -1
    }



    private fun goBack() {
        findNavController().navigate(R.id.action_formFragment_to_myFeedbackFragment)
    }

}
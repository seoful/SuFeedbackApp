package com.example.su_android_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.su_android_app.connection.Api
import com.example.su_android_app.R
import com.example.su_android_app.connection.VolleyObject
import com.example.su_android_app.extra.JsonParser


class CategoriesFragment : Fragment() {

    private lateinit var listView: ListView

    private lateinit var categoriesMap: Map<String, Int>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        listView = view.findViewById(R.id.categories_list)
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val category = listView.getItemAtPosition(position) as String
                val bundle =
                    bundleOf("category_id" to categoriesMap[category], "category_text" to category)
                view.findNavController()
                    .navigate(R.id.action_categoriesFragment_to_formFragment, bundle)
            }
        requestCategories()

        return view
    }


    private fun requestCategories() {

        val request = object : StringRequest(Method.POST, Api.GET_CATEGORIES,
            Response.Listener<String> { response ->
                categoriesMap = JsonParser.parseCategories(response)
                initializeList()
            },
            Response.ErrorListener {
                println(it.message)
            }) {
            override fun getParams(): Map<String, String> {
                val params = mutableMapOf<String, String>()
                params["token"] = Api.TOKEN
                return params
            }
        }

        VolleyObject.getInstance(requireContext().applicationContext)
            .addToRequestQueue(request)
    }

    fun initializeList() {
        listView.adapter =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.category_item, categoriesMap.keys.toList()
            )
    }

}
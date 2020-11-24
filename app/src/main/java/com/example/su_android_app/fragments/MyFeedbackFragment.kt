package com.example.su_android_app.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.su_android_app.R
import com.example.su_android_app.connection.Api
import com.example.su_android_app.connection.VolleyObject
import com.example.su_android_app.data.Ticket
import com.example.su_android_app.extra.JsonParser
import com.example.su_android_app.fragments.adapters.MyFeedbackAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MyFeedbackFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MyFeedbackAdapter
    private lateinit var plusButton: FloatingActionButton
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var sortButton: ImageButton
    private lateinit var sortMenu: PopupMenu
    private var comparator = Ticket.COMPARATOR_DATE_ASCENDING

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_feedback, container, false)
        plusButton = view.findViewById(R.id.plus_button)
        sortButton = view.findViewById(R.id.sort_button)
        refreshLayout = view.findViewById(R.id.refresh)
        sortMenu = PopupMenu(context, sortButton)
        sortMenu.inflate(R.menu.sort_menu)
        sortMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.date_ascending -> {
                    comparator = Ticket.COMPARATOR_DATE_ASCENDING
                    sortTickets()
                    true
                }
                R.id.date_descending -> {
                    comparator = Ticket.COMPARATOR_DATE_DESCENDING
                    sortTickets()
                    true
                }
                R.id.status_ascending -> {
                    comparator = Ticket.COMPARATOR_STATUS_ASCENDING
                    sortTickets()
                    true
                }
                R.id.status_descending -> {
                    comparator = Ticket.COMPARATOR_STATUS_DESCENDING
                    sortTickets()
                    true
                }
                R.id.name_ascending -> {
                    true
                }
                R.id.name_descending -> {
                    true
                }
                else -> false
            }
        }
        sortButton.setOnClickListener { sortMenu.show() }
        refreshLayout.setOnRefreshListener { getTickets() }
        plusButton.setOnClickListener { findNavController().navigate(R.id.action_myFeedbackFragment_to_categoriesFragment) }

        viewAdapter = MyFeedbackAdapter()
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }

        getTickets()
        return view
    }

    private fun getTickets() {
        refreshLayout.isRefreshing = true
        val request = object : StringRequest(
            Method.POST, Api.GET_TICKETS,
            Response.Listener<String> { response ->
                viewAdapter.tickets = JsonParser.parseTickets(response)
                sortTickets()
                refreshLayout.isRefreshing = false
            },
            Response.ErrorListener {
                showErrorDialog()
            }) {

            override fun getParams(): Map<String, String> {
                val params = mutableMapOf<String, String>()
                params["token"] = Api.TOKEN
                params["tg_id"] = "0"
                return params
            }
        }
        request.retryPolicy = DefaultRetryPolicy(
            3000, 0,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        VolleyObject.getInstance(requireContext().applicationContext)
            .addToRequestQueue(request)
    }

    fun sortTickets() {
        viewAdapter.tickets = viewAdapter.tickets.sortedWith(comparator).toMutableList()
        viewAdapter.notifyDataSetChanged()
    }

    fun showErrorDialog() {
        val builder = AlertDialog.Builder(activity)
            .setMessage("Error connecting to server...")
            .setPositiveButton("Try again") { dialog, id ->
                dialog.cancel()
                getTickets()
            }.setCancelable(true)
        builder.create().show()
        refreshLayout.isRefreshing = false
    }


}
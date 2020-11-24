package com.example.su_android_app.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.su_android_app.R
import com.example.su_android_app.data.Ticket
import com.example.su_android_app.data.TicketState

class MyFeedbackAdapter() :
    RecyclerView.Adapter<MyFeedbackAdapter.ViewHolder>() {


    var tickets = mutableListOf<Ticket>()


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val category: TextView = v.findViewById(R.id.category)
        val state: ImageView = v.findViewById(R.id.state)
        val comment: TextView = v.findViewById(R.id.comment)
        val date: TextView = v.findViewById(R.id.date)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.ticket_item, parent, false)

        v.setOnClickListener { }
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = tickets.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.apply {
            category.text = tickets[adapterPosition].category
            comment.text = "Comment: ${tickets[adapterPosition].comment}"
            date.text = tickets[adapterPosition].dateString()
            state.setBackgroundResource(
                when (tickets[adapterPosition].state) {
                    TicketState.SUCCESS-> R.drawable.cirle_green
                    TicketState.UNDER_CONSIDERATION -> R.drawable.circle_orange
                    else -> R.drawable.circle_red
                }
            )

        }

    }
}

package com.example.su_android_app.data

import org.joda.time.DateTime

class Ticket(
    val category: String,
    state: String,
    date: String,
    val comment: String,
    val answers: String
) : Comparable<Ticket> {
    val date = DateTime(date)
    val state = when (state) {
        "success" -> TicketState.SUCCESS
        "under_consideration" -> TicketState.UNDER_CONSIDERATION
        else -> TicketState.REJECTED
    }


    override fun compareTo(other: Ticket): Int {
        return when {
            date.isBefore(other.date) -> 1
            date.isAfter(other.date) -> -1
            else -> 0
        }
    }

    fun dateString() =
        "${date.dayOfMonth().asString}.${date.monthOfYear().asString}.${date.yearOfCentury().asString}, ${date.hourOfDay().asString}:${date.minuteOfHour().asString}"


    companion object {
        val COMPARATOR_DATE_ASCENDING = Comparator<Ticket> { o1, o2 ->
            when {
                o1.date.isBefore(o2.date) -> 1
                o1.date.isAfter(o2.date) -> -1
                else -> 0
            }
        }
        val COMPARATOR_DATE_DESCENDING = Comparator<Ticket> { o1, o2 ->
            -1 * COMPARATOR_DATE_ASCENDING.compare(o1, o2)
        }

        val COMPARATOR_STATUS_ASCENDING = Comparator<Ticket> { o1, o2 ->
            val s1 = o1.state
            val s2 = o2.state
            if (s1 == s2)
                COMPARATOR_DATE_ASCENDING.compare(o1, o2)
            else if (s1 == TicketState.SUCCESS)
                -1
            else if (s1 == TicketState.REJECTED)
                1
            else if (s1 == TicketState.UNDER_CONSIDERATION && s2 == TicketState.SUCCESS)
                1
            else
                -1
        }
        val COMPARATOR_STATUS_DESCENDING = Comparator<Ticket> { o1, o2 ->
            -1 * COMPARATOR_STATUS_ASCENDING.compare(o1, o2)
        }
    }
}
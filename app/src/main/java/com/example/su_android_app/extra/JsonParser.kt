package com.example.su_android_app.extra

import com.example.su_android_app.data.Question
import com.example.su_android_app.data.Ticket
import org.json.JSONObject

class JsonParser {

    companion object {

        fun parseCategories(response: String): Map<String, Int> {
            val map = mutableMapOf<String, Int>()
            val array = JSONObject(response).getJSONArray("categories")
            for (i in 0 until array.length()) {
                map[array.getJSONObject(i).getJSONObject("fields").getString("name")] =
                    array.getJSONObject(i).getInt("pk")
            }
            return map

        }

        fun parseQuestions(response: String): MutableList<Question> {
            val questions = mutableListOf<Question>()
            val array = JSONObject(response).getJSONArray("questions")
            for (i in 0 until array.length()) {
                questions += Question(
                    array.getJSONObject(i).getJSONObject("fields").getString("text"),
                    array.getJSONObject(i).getJSONObject("fields").getInt("priority")
                )
            }
            return questions
        }

        fun parseTickets(response : String) : MutableList<Ticket>{
            val tickets = mutableListOf<Ticket>()
            val array = JSONObject(response).getJSONArray("tickets")
            for (i in 0 until array.length()) {
                tickets += Ticket(
                    array.getJSONObject(i).getJSONObject("fields").getJSONArray("category")[1].toString(),
                    array.getJSONObject(i).getJSONObject("fields").getString("state"),
                    array.getJSONObject(i).getJSONObject("fields").getString("creation_date"),
                    array.getJSONObject(i).getJSONObject("fields").getString("comment"),
                    array.getJSONObject(i).getJSONObject("fields").getString("answers")
                )
            }
            tickets.sort()
            return tickets

        }

    }
}
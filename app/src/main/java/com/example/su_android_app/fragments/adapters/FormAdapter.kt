package com.example.su_android_app.fragments.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.extensions.style
import com.example.su_android_app.R
import com.example.su_android_app.data.EditTextState
import com.example.su_android_app.data.Question

class FormAdapter() :
    RecyclerView.Adapter<FormAdapter.ViewHolder>() {


    var questions = mutableListOf<Question>()


    class ViewHolder(v: View, val listener: EditTextChangeListener) : RecyclerView.ViewHolder(v) {
        val question: TextView = v.findViewById(R.id.question)
        val editText: EditText = v.findViewById(R.id.answer_field)

        init {
            editText.addTextChangedListener(listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.feedback_form_item, parent, false)

        return ViewHolder(v, EditTextChangeListener())
    }

    override fun getItemCount(): Int = questions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println("binding $position")
        holder.apply {
            val q = questions[position]
            question.text = "${position + 1}. ${q.question}"
            listener.updatePosition(position)
            editText.setText(questions[adapterPosition].answer)
            editText.setSelection(editText.text.length)
            editText.style(if (q.state == EditTextState.ERROR) R.style.Error else R.style.Ok)
        }


    }

    inner class EditTextChangeListener : TextWatcher {

        private var position = -1

        fun updatePosition(pos: Int) {
            position = pos
        }

        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            if(s.toString() != "" && questions[position].state == EditTextState.ERROR){
                questions[position].state = EditTextState.OK
                notifyItemChanged(position)
            }
            questions[position].answer = s.toString()
        }
    }
}

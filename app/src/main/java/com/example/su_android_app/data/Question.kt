package com.example.su_android_app.data

import com.example.su_android_app.Checkable

data class Question(val question: String, val priority: Int) : Checkable {

    var answer: String = ""
    var state: EditTextState =
        EditTextState.OK
    var mandatory: Boolean = true

    override fun check() = answer != "" && mandatory

    override fun toString(): String {
        return "\"$priority\": \"$answer\""
    }


}
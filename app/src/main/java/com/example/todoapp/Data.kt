package com.example.todoapp
import java.io.Serializable

class Data : Serializable {
    private lateinit var text: String
    fun getText(): String? {
        return text
    }
    @JvmName("setText")
    fun setText(text: String?) {
        this.text = text!!
    }
}
package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.text.SpannableStringBuilder
import android.widget.Button

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val editTextField = findViewById<EditText>(R.id.editText)
        //Unpack data and set the data to UI
        var text = intent.getSerializableExtra("serializable") as Data?
        val editable: Editable = SpannableStringBuilder(text?.getText().toString())
        editTextField.text = editable

        findViewById<Button>(R.id.finishEditButton).setOnClickListener {
            //Go back to previous activity with edited text
            val intent = Intent()
            intent.putExtra("editTextValue", editTextField.text.toString())
            setResult(RESULT_OK, intent)
            onBackPressed()
        }

        findViewById<Button>(R.id.cancel_button).setOnClickListener {
            //Go back to previous activity with cancel edits
            onBackPressed()
        }
    }
}
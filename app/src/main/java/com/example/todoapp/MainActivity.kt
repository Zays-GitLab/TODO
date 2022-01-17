package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    //Holder lists setup
    var listOfTask = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
        override fun onItemLongClicked(position: Int) {
            //Remove item from list
            listOfTask.removeAt(position)
            //Notify adapter our data has changed
            adapter.notifyDataSetChanged()
            //Save data to file
            saveItems()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Detects when user clicks the submit/add button
        findViewById<Button>(R.id.submitButton).setOnClickListener {
            //Execute code when button is clicked
            Log.i("App", "User clicked on button")

        }

        //Load the data from file
        loadItems()
        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTask, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        // set up button and input field- user can add task
        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        //Get reference to button
        findViewById<Button>(R.id.submitButton).setOnClickListener {
            //Grab input text
            val userInputtedTask = inputTextField.text.toString()
            //Add string to list of tasks
            listOfTask.add(userInputtedTask)
            //Notify adapter data update
            adapter.notifyItemInserted(listOfTask.size -1)
            //Reset text field
            inputTextField.setText("")
            //Save data to file
            saveItems()
        }
    }

    //Save user inputted data using file
    //Method to fetch data
    fun getDataFile() : File {
        //Each line is 1 Task
        return File(filesDir, "ToDoData.txt")
    }

    //Load items by line
    fun loadItems(){
        try {
            listOfTask = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }
        catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    //Save items
    fun saveItems(){
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(),listOfTask)
        }
        catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
}
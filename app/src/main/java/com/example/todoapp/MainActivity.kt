package com.example.todoapp

import android.app.Activity
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
import android.content.Intent

class MainActivity : AppCompatActivity() {

    //Holder lists setup
    var posIdx: Int = 0
    var listOfTask = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    private val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
        override fun onItemLongClicked(position: Int) {
            //Remove item from list
            listOfTask.removeAt(position)
            //Notify adapter our data has changed
            adapter.notifyDataSetChanged()
            //Save data to file
            saveItems()
        }
    }

    private val onClickListener = object : TaskItemAdapter.OnClickListener{
        override fun onItemClicked(position: Int) {
            //Remove item from list
            val item = listOfTask[position]
            posIdx = position
            //Notify adapter our data has changed
            val data = Data()
            data.setText(item)
            // first parameter is the context, second is the class of the activity to launch
            val intent = Intent(this@MainActivity, MainActivity2::class.java)
            val b = Bundle()
            b.putSerializable("serializable", data)
            intent.putExtras(b)
            startActivityForResult(intent,1) // brings up the second activity
            //Save data to file
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val strEditText = data?.getStringExtra("editTextValue")
                if (strEditText != null) {
                    listOfTask[posIdx] = strEditText
                    adapter.notifyDataSetChanged()
                    saveItems()
                }
            }
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
        adapter = TaskItemAdapter(listOfTask, onLongClickListener, onClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        // set up button and input field- user can add task
        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        //Get reference to button
        findViewById<Button>(R.id.submitButton).setOnClickListener {
            //Grab input text
            val userInputtedTask = inputTextField.text.toString()
            //If task not empty
            if (userInputtedTask.replace(" ", "") != "") {
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
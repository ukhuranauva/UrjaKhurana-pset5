package com.example.urja.urjakhurana_pset5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/* This activity is to add a list. The list will become a ToDoList object. First, you can give
* the list a name, and then add tasks to the list before adding the list to your collection. */

public class AddListActivity extends AppCompatActivity {

    // initialize the variables
    ArrayAdapter toDoAdapter;
    ArrayList<String> tasks = new ArrayList<>();
    ToDoManager taskManager = ToDoManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
        // Set the empty listview for the user to add new tasks
        ListView toDoList = (ListView) findViewById(R.id.taskList);
        toDoAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks);
        toDoList.setAdapter(toDoAdapter);
    }

    /* adds the list made by the user to the file. this is done by first making the string that will
    * be saved to the file. this is done to identify the different lists, tasks etc. the following
    * convention is used: listName1:taskName1$taskStatus1*taskName2:taskStatus2+listName2: etc*/
    public void addToDoList(View view) {
        // initialize String variable to save the list
        String saveList = "";
        // get name of the list
        EditText titleField = (EditText) findViewById(R.id.listTitle);
        String listTitle = titleField.getText().toString();
        // add the seperator that seperates the name of the list from its tasks
        saveList += listTitle + ":";
        // for each task, add the seperator from the task and its status($) and other tasks(*)
        for(int i = 0; i < tasks.size(); i++) {
            String task = tasks.get(i);
            // add "tbd" as status since it is not done yet
            saveList += task + "$tbd*";
        }
        // add the seperator that seperates different lists
        saveList += "+";
        // get all the old lists and add the new one to the old ones
        String savedLists = taskManager.readFile(getApplicationContext());
        String newSavedLists = savedLists + saveList;
        // save the new lists to the file
        taskManager.writeToFile(getApplicationContext(), newSavedLists);
        Intent mainScreen = new Intent(this, MainActivity.class);
        startActivity(mainScreen);
        finish();
    }

    // add a task when the user wants to
    public void addTask(View view) {
        // get name of the task
        EditText taskField = (EditText) findViewById(R.id.task);
        String task = taskField.getText().toString();
        // add it to the list of tasks of the user and update the list showcased
        tasks.add(task);
        toDoAdapter.notifyDataSetChanged();
    }
}

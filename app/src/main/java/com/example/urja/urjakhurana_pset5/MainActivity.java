package com.example.urja.urjakhurana_pset5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/* In this activity, all of the task lists are showcased, they can also be deleted by longclicking
 * on them and viewed by tapping on them. This leads to a new activity where the tasks of that list
 * are showcased. On top of that, new lists can be added. Tapping on the add list button, a new
 * activity appears, where the user can add a new to do list. */

public class MainActivity extends AppCompatActivity {

    // initialize variables
    ListView toDoLists;
    ArrayAdapter adapter;
    ArrayList<String> toDoList = new ArrayList<>();
    ToDoManager taskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get the ToDoManager instance
        taskManager = ToDoManager.getInstance();
        toDoLists = (ListView) findViewById(R.id.listView);
        // get the lists from the file and set the taskManager with it
        getLists();
        // get listview and set adapter
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, toDoList);
        toDoLists.setAdapter(adapter);
        setListeners();
    }

    // get the lists from the file and get all the listnames
    public void getLists() {
        // get the lists from the file and set them on the taskmanager
        String fileData = taskManager.readFile(getApplicationContext());
        taskManager.readToDos(fileData);
        taskManager = ToDoManager.getInstance();
        // get all the lists as an arraylist
        ArrayList<ToDoList> lists = taskManager.getList();
        // for each list, get its name and add it to the toDolists for the listview
        for(int i = 0; i < lists.size(); i++) {
            ToDoList list = lists.get(i);
            String name = list.getTitle();
            toDoList.add(name);
        }
    }

    // go to another activity to add a new to-do list
    public void addList(View view) {
        Intent addList = new Intent(this, AddListActivity.class);
        startActivity(addList);
        finish();
    }

    // set the click listeners for a normal click and a long click
    public void setListeners() {

        // this click listener is to go to the page of the list to showcase and edit the tasks
        toDoLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // get page of movie by clicking on one of the movie names
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                TextView listView = (TextView) view;
                // get name of list
                String taskList = listView.getText().toString();
                Intent toDoPage = new Intent(getApplicationContext(), ToDoListActivity.class);
                toDoPage.putExtra("listPos", position);
                toDoPage.putExtra("listName", taskList);
                // go to new activity (no finish since you would like to go back to this activity)
                startActivity(toDoPage);
            }
        });

        // long click to delete a list
        toDoLists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView listView = (TextView) view;
                // get name of list
                String listName = listView.getText().toString();
                // delete the list from the taskManager and adapter
                taskManager.deleteList(position);
                adapter.remove(listName);
                adapter.notifyDataSetChanged();
                // save the left over lists
                taskManager.updateFile(getApplicationContext());
                return true;
            }
        });
    }
}

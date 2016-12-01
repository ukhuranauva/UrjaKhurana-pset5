package com.example.urja.urjakhurana_pset5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/* This activity showcases a list and its tasks. Tasks can be deleted or put on done. */

public class ToDoListActivity extends AppCompatActivity {

    // initialize variables
    int listPos;
    String listName;
    ToDoManager taskManager = ToDoManager.getInstance();
    ToDoList theList;
    ArrayList<ToDoItem> toDoItems;
    ArrayList<String> toDo;
    ArrayList<String> done;
    ArrayAdapter adapter;
    ArrayAdapter adapterDone;
    ListView tasks;
    ListView doneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        // get the index and name of the list
        Bundle extras = getIntent().getExtras();
        listPos = extras.getInt("listPos");
        listName = extras.getString("listName");
        // set title as the list of the name
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(listName);
        // set the lists of done and to do
        getTasks();
        // set the listview with the right tasks for done and to do
        tasks = (ListView) findViewById(R.id.tasks);
        adapter = new ArrayAdapter(this, R.layout.listview_layout, R.id.taskView, toDo);
        tasks.setAdapter(adapter);
        doneView = (ListView) findViewById(R.id.doneView);
        adapterDone = new ArrayAdapter(this, android.R.layout.simple_list_item_1, done);
        doneView.setAdapter(adapterDone);
        // set the listeners
        setListeners();
    }

    // gets the tasks of a list and divides them between done and to do lists
    public void getTasks() {
        ArrayList<ToDoList> toDoLists = taskManager.getList();
        // get the corresponding list
        theList = toDoLists.get(listPos);
        // get all of its tasks
        toDoItems = theList.getTaskList();
        toDo = new ArrayList<>();
        done = new ArrayList<>();
        // for each task in the list, get its name and status and add it to the right list
        for(int i = 0; i < toDoItems.size(); i++) {
            String taskName = toDoItems.get(i).getTitle();
            String taskStatus = toDoItems.get(i).getStatus();
            // if task has to be done add it to the right list
            if(taskStatus.equals("tbd")) {
                toDo.add(taskName);
            } else if(taskStatus.equals("done")) {
                // when task is done, add it to the right list
                done.add(taskName);
            }
        }
    }

    // puts the status of a task to done when executed by the user
    public void updateStatus(View view) {
        // get the right task and its name
        RelativeLayout layout = (RelativeLayout) view.getParent();
        TextView taskView = (TextView) layout.getChildAt(0);
        String toDo = taskView.getText().toString();
        CheckBox checkbox = (CheckBox) view;
        //http://stackoverflow.com/questions/9093190/get-position-in-listview
        int position = tasks.getPositionForView(view);
        // get the proper object of the task
        ToDoItem taskItem = toDoItems.get(position);
        // change the status to done
        taskItem.setStatus("done");
        // update adaptors
        adapter.remove(toDo);
        adapterDone.add(toDo);
        adapter.notifyDataSetChanged();
        adapterDone.notifyDataSetChanged();
        // update the file with the new status of the task
        taskManager.updateFile(getApplicationContext());
        // set check of the checkbox back to false (since the replacing checkbox becomes checked)
        checkbox.setChecked(false);
    }

    // set the listeners for both listviews
    public void setListeners() {
        // when a done task is clicked on, it gets deleted
        doneView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // get page of movie by clicking on one of the movie names
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                TextView listView = (TextView) view;
                // get task name
                String listName = listView.getText().toString();
                // delete it from the list and update adapter
                taskManager.deleteTask(listPos, position);
                adapterDone.remove(listName);
                adapterDone.notifyDataSetChanged();
                // delete from file by updating it
                taskManager.updateFile(getApplicationContext());
            }
        });

        // when a to be done task is long clicked it gets deleted
        tasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long viewId) {
                // get proper task
                RelativeLayout layout = (RelativeLayout) view;
                TextView taskView = (TextView) layout.getChildAt(0);
                // get task's name
                String listName = taskView.getText().toString();
                // delete it from the list and update the adapter
                taskManager.deleteTask(listPos, position);
                adapter.remove(listName);
                adapter.notifyDataSetChanged();
                // delete from file by updating it
                taskManager.updateFile(getApplicationContext());
                return true;
            }
        });
    }

    // adds a task when the user wants to
    public void addTask(View view) {
        EditText taskField = (EditText) findViewById(R.id.toDo);
        // get name of task
        String task = taskField.getText().toString();
        // add task to the list and update adapter
        taskManager.addTask(task, listPos);
        adapter.add(task);
        adapter.notifyDataSetChanged();
        // update the file with the new task
        taskManager.updateFile(getApplicationContext());
    }
}

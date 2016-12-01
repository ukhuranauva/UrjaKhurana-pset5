package com.example.urja.urjakhurana_pset5;

import java.util.ArrayList;

/* This class is for a ToDoList object. A ToDoList object signifies a list with all of its tasks.
* The object has a title, which is the name of the list and describes what the list is for. It
* also has a list of the tasks of the list, which are the ToDoItems, that signify a task. */

public class ToDoList {

    // initialize variables
    private ArrayList<ToDoItem> toDoItems = new ArrayList<>();
    private String title;

    // constructor
    public ToDoList(String title, ArrayList<ToDoItem> toDoItems) {
        this.title = title;
        this.toDoItems = toDoItems;
    }

    // getter for the to-do list
    public ArrayList<ToDoItem> getTaskList() {
        return toDoItems;
    }

    // getter for the name of the to-do list
    public String getTitle() {
        return title;
    }
}

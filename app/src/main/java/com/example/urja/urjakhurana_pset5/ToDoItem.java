package com.example.urja.urjakhurana_pset5;

/* This class is to manage a ToDoItem object. It signifies a task, which has a name and a status.
* The title is the name of the task which describes what has to be done. The status of the task is
* whether the task has to be done yet or is already done. When the has not been done yet, the status
* will be "tbd" and when it is already completed, the status will be "done".*/

public class ToDoItem {

    // initializes the variables
    private String title;
    private String status;

    // constructor
    public ToDoItem(String title, String status) {
        this.title = title;
        this.status = status;
    }

    // getter for the naem of a task
    public String getTitle() {
        return title;
    }

    // getter for the status of a task
    public String getStatus() {
        return status;
    }

    // setter for the status of a task
    public void setStatus(String newStatus) {
        status = newStatus;
    }
}

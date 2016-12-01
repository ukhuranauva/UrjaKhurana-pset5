package com.example.urja.urjakhurana_pset5;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/* The ToDoManager is a Singleton class which manages all of the information in the files. It
 * has an ArrayList which consists of all the to-do lists of the user. The manager reads the files
 * and writes/updates the files when needed. The methods in this class can be used by all the
 * activities. */

public class ToDoManager {

    // initialize the variables
    private static ToDoManager ourInstance = new ToDoManager();
    private static ArrayList<ToDoList> toDoLists;

    // instance getter
    public static ToDoManager getInstance() {
        return ourInstance;
    }

    // constructor of the to do lists
    private ToDoManager() {
        toDoLists = new ArrayList<>();
    }

    /* sets the toDoLists with the content in the file. since the file consists of seperators to be
    * able to identify the lists and their respective tasks, the string gets split per step to get
    * the right values. then all of the values get added to the toDoLists of the ToDoManager in a
    * proper way */
    public void readToDos(String fileContent) {
        // clears the old data in the Arraylist
        toDoLists.clear();
        Log.d("hello", fileContent);
        // splits all the different lists into seperate lists with "+" seperator
        String[] savedLists = fileContent.split("\\+");
        Log.d("string", Arrays.toString(savedLists));
        // for each list, create the ToDoList object with its ToDoItems which are the tasks
        for(int i = 0; i < savedLists.length; i++ ) {
            // splits the listName from the tasks
            String[] toDoList = savedLists[i].split(":");
            String listName = toDoList[0];
            ArrayList<ToDoItem> toDos = new ArrayList<>();
            // if the list does not contain any toDos, create the list with its name and no tasks
            if(toDoList.length > 1) {
                String listTasks = toDoList[1];
                // split on "*" to get all the different tasks
                String[] items = listTasks.split("\\*");
                // for each task, create a ToDoItem object and add it to the list of tasks
                for(int j = 0; j < items.length; j++) {
                    // split on $ to get the name and status of the task
                    String[] nameAndStatus = items[j].split("\\$");
                    Log.d("string", Arrays.toString(nameAndStatus));
                    String title = nameAndStatus[0];
                    String status = nameAndStatus[1];
                    // create object of the task and add it to list of task
                    ToDoItem item = new ToDoItem(title, status);
                    toDos.add(item);
                }
            }
            // creates list that contains its name and the tasks and adds it to the list of lists
            ToDoList newList = new ToDoList(listName, toDos);
            toDoLists.add(newList);
        }
    }

    // getter for the list of to-do lists
    public ArrayList<ToDoList> getList() {
        return toDoLists;
    }

    // deletes a list from the list of to-do lists
    public void deleteList(int index) {
        toDoLists.remove(index);
    }

    // deletes a tisk from a given to do list
    public void deleteTask(int indexList, int indexTask) {
        // gets the specific to do list
        ToDoList toDoList = toDoLists.get(indexList);
        ArrayList<ToDoItem> taskList = toDoList.getTaskList();
        // deletes the task from the tasklist
        taskList.remove(indexTask);
    }

    // given the name of the task and the index of the list, adds the task to the list
    public void addTask(String task, int index) {
        // get the proper tasklist
        ToDoList toDoList = toDoLists.get(index);
        ArrayList<ToDoItem> taskList = toDoList.getTaskList();
        // create new task with status "tbd" since its a new task and add it to the list
        ToDoItem newTask = new ToDoItem(task, "tbd");
        taskList.add(newTask);
    }

    // given the context, update the file with the updated elements(like deleted or added task)
    public void updateFile(Context context) {
        // get the updated list
        ArrayList<ToDoList> updatedList = getList();
        String newLists = "";
        // for each list, get the proper seperators etc
        for(int i = 0; i < updatedList.size(); i++) {
            // get list and its title
            ToDoList toDoList = updatedList.get(i);
            ArrayList<ToDoItem> tasks = toDoList.getTaskList();
            String listName = toDoList.getTitle();
            // add title and the seperator for the tasks
            newLists += listName + ":";
            // for each task, add it to the string that will be saved to the file
            for(int j = 0; j < tasks.size(); j++) {
                ToDoItem task = tasks.get(j);
                String taskName = task.getTitle();
                String taskStatus = task.getStatus();
                // add title, seperators and its status to the string
                newLists += taskName + "$" + taskStatus + "*";
            }
            // add seperator for in between lists
            newLists += "+";
        }
        Log.d("jlaskjfkl", newLists);
        // save the new data
        writeToFile(context, newLists);
    }

    // write the given string to the file, also given the context or else openfileoutput doesn't work
    public void writeToFile(Context context, String string) {
        try {
            FileOutputStream file = context.openFileOutput("toDoLists", Context.MODE_PRIVATE);
            // write data to file
            file.write(string.getBytes());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // read data from file, given the context of the application or else openfileinput doesn't work
    public String readFile(Context context) {
        String data = "";
        try {
            FileInputStream file = context.openFileInput("toDoLists");
            int c;
            // add all the characters in the file to the data string
            while( (c = file.read()) != -1){
                data += Character.toString((char)c);
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}

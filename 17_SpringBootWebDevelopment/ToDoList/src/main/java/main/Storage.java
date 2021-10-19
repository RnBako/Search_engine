package main;

import response.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Storage {
    private static int currentId = 1;
    private static HashMap<Integer, Task> tasks = new HashMap<>();

    public static int addTask(Task task) {
        int id = currentId++;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    public static int deleteTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
            return taskId;
        } else return -1;
    }

    public static int updateTask(int id, Task task) {
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
            return task.getId();
        } else return -1;
    }

    public static Task getTask(int taskId) {
        if (tasks.containsKey(taskId)) return tasks.get(taskId);
        return null;
    }

    public static List<Task> getAllTasks() {
        ArrayList<Task> todoList = new ArrayList<>(tasks.values());
        return todoList;
    }

    public static void deleteAllTasks () {
        tasks.clear();
    }
}

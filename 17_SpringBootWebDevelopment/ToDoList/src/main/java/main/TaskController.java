package main;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import response.Task;

import java.util.List;

@RestController
public class TaskController {
    @GetMapping("/tasks/")
    public List<Task> list() {
        return Storage.getAllTasks();
    }

    @PostMapping("/tasks/")
    public int add(Task task) {
        return Storage.addTask(task);
    }

    @PutMapping("/tasks/")
    public ResponseEntity putAll () {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(null);
    }

    @DeleteMapping("/tasks/")
    public ResponseEntity deleteAll () {
        Storage.deleteAllTasks();
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity get (@PathVariable int id) {
        Task task = Storage.getTask(id);
        if (task == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return new ResponseEntity(task, HttpStatus.OK);
    }

    @PostMapping("/tasks/{id}")
    public ResponseEntity addTask (@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(null);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity put(@PathVariable int id, Task task) {
        if (Storage.updateTask(id, task) < 0) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return new ResponseEntity(task, HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity delete (@PathVariable int id) {
        if (Storage.deleteTask(id) < 0) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}

package controller;

import repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import model.Task;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RestController
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/tasks/")
    public List<Task> list() {
        Iterable<Task> taskIterable = taskRepository.findAll();
        ArrayList<Task> tasks = new ArrayList<>();
        taskIterable.forEach(tasks::add);
        return tasks;
    }

    @PostMapping("/tasks/")
    public int add(Task task) {
        if (task.getFromDate() == null) task.setFromDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        if (task.getToDate() == null) task.setToDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Task newTask = taskRepository.save(task);
        return newTask.getId();
    }

    @DeleteMapping("/tasks/")
    public ResponseEntity deleteAll () {
        taskRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity get (@PathVariable int id) {
        Optional<Task> task = taskRepository.findById(id);
        if (!task.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return new ResponseEntity(task.get(), HttpStatus.OK);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity put(@PathVariable int id, Task task) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        if (task.getFromDate() == null) task.setFromDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        if (task.getToDate() == null) task.setToDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        taskRepository.save(task);
        return new ResponseEntity(task, HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity delete (@PathVariable int id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        taskRepository.delete(optionalTask.get());
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}

package com.todolist.controller

import com.todolist.data.model.TaskCreateRequest
import com.todolist.data.model.TaskDto
import com.todolist.data.model.TaskUpdateRequest
import com.todolist.service.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tasks")
class TaskController(private val taskService: TaskService) {

    @GetMapping("/all")
    fun getAllTasks(): ResponseEntity<List<TaskDto>> = ResponseEntity(taskService.getAllTasks(), HttpStatus.OK)

    @GetMapping("open-tasks")
    fun getAllOpenTasks(): ResponseEntity<List<TaskDto>> = ResponseEntity(taskService.getAllOpenTasks(), HttpStatus.OK)

    @GetMapping("closed-tasks")
    fun getAllClosedTasks(): ResponseEntity<List<TaskDto>> = ResponseEntity(taskService.getAllClosedTasks(), HttpStatus.OK)

    @GetMapping("/{id}")
    fun getTaskById(@PathVariable id: Long): ResponseEntity<TaskDto> = ResponseEntity(taskService.getTaskById(id), HttpStatus.OK)

    @PostMapping("/new")
    fun createTask(@RequestBody taskCreateRequest: TaskCreateRequest): ResponseEntity<TaskDto> = ResponseEntity(taskService.createTask(taskCreateRequest), HttpStatus.OK)

    @PatchMapping("/update/{id}")
    fun updateTask(@PathVariable id: Long, @RequestBody taskUpdateRequest: TaskUpdateRequest): ResponseEntity<TaskDto> = ResponseEntity(taskService.updateTask(id, taskUpdateRequest), HttpStatus.OK)

    @DeleteMapping("/delete/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<String> = ResponseEntity(taskService.deleteTask(id), HttpStatus.OK)


}
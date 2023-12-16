package com.todolist.service

import com.todolist.data.Task
import com.todolist.data.model.TaskCreateRequest
import com.todolist.data.model.TaskDto
import com.todolist.data.model.TaskUpdateRequest
import com.todolist.exception.BadRequestException
import com.todolist.exception.TaskNotFoundException
import com.todolist.repository.TaskRepository
import org.springframework.data.util.ReflectionUtils
import org.springframework.stereotype.Service
import java.lang.reflect.Field
import java.util.stream.Collectors
import kotlin.reflect.full.memberProperties

@Service
class TaskService(private val taskRepository: TaskRepository) {

    private fun convertEntityToDto(task: Task): TaskDto {
        return TaskDto(
                task.id,
                task.description,
                task.isReminderSet,
                task.isTaskOpen,
                task.createdOn,
                task.priority
        )
    }

    private fun assignNewValuesToEntity(task: Task, taskCreateRequest: TaskCreateRequest) {
        task.description = taskCreateRequest.description
        task.isReminderSet = taskCreateRequest.isReminderSet
        task.isTaskOpen = taskCreateRequest.isTaskOpen
        task.createdOn = taskCreateRequest.createdOn
        task.priority = taskCreateRequest.priority
    }

    private fun assignUpdatedValuesToEntity(task: Task, taskUpdateRequest: TaskUpdateRequest) {
        task.description = taskUpdateRequest.description
        task.isReminderSet = taskUpdateRequest.isReminderSet
        task.isTaskOpen = taskUpdateRequest.isTaskOpen
        task.priority = taskUpdateRequest.priority
    }


    private fun checkForTaskId(id: Long) {
        if (!taskRepository.existsById(id)) {
            throw TaskNotFoundException("Task with Id: $id does not exist")
        }
    }

    fun getAllTasks(): List<TaskDto> =
            taskRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList())

    fun getAllOpenTasks(): List<TaskDto> =
            taskRepository.queryAllOpenTasks().stream().map(this::convertEntityToDto).collect(Collectors.toList())

    fun getAllClosedTasks(): List<TaskDto> =
            taskRepository.queryAllClosedTasks().stream().map(this::convertEntityToDto).collect(Collectors.toList())

    fun getTaskById(id: Long): TaskDto {
        checkForTaskId(id)
        val task: Task = taskRepository.findTaskById(id)
        return convertEntityToDto(task)
    }

    fun createTask(taskCreateRequest: TaskCreateRequest): TaskDto {
        if (taskRepository.doesDescriptionExist(taskCreateRequest.description)) {
            throw BadRequestException("There is already a task with description: ${taskCreateRequest.description}")
        }
        val task = Task()
        assignNewValuesToEntity(task, taskCreateRequest)
        val newTask = taskRepository.save(task)
        return convertEntityToDto(newTask)
    }


    fun updateTask(id: Long, updateRequest: TaskUpdateRequest): TaskDto {
        checkForTaskId(id)
        val existingTask: Task = taskRepository.findTaskById(id)

        assignUpdatedValuesToEntity(existingTask, updateRequest)

        val updatedTask: Task = taskRepository.save(existingTask)
        return convertEntityToDto(updatedTask)
    }

    fun deleteTask(id: Long): String {
        checkForTaskId(id)
        taskRepository.deleteById(id)
        return "Task with id: $id has been deleted."
    }


}
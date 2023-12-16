package com.todolist.data.model

import java.time.LocalDateTime

data class TaskCreateRequest(
        val description: String,
        val isReminderSet: Boolean,
        val isTaskOpen: Boolean,
        val createdOn: LocalDateTime,
        val priority: Priority
)

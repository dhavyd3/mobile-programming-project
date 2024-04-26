package com.alienventures.tasks.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

import java.util.*


@Entity(tableName = "tasks")
data class Task (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val listName: String,
    val taskName: String,
    val attachment: String? = null, // Assuming we store a path or URI as a string
    val subtasks: String, // JSON representation of subtasks
    val tags: String, // JSON representation of tags
    val dueDate: Date? = null,
    val reminder: Date? = null,
    val priority: Int,
    val highlight: Boolean,
    val completed: Boolean,
    val createdAt: Date
)

// You will need a TypeConverter to tell Room how to persist custom types.
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}
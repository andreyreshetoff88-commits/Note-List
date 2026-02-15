package com.example.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.core.room.entitys.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(todo: TodoEntity)

    @Update
    suspend fun updateGroup(todo: TodoEntity)

    @Query("SELECT * FROM todos WHERE id = :todoId")
    fun getGroupById(todoId: String): Flow<TodoEntity?>

    @Delete
    suspend fun deleteGroup(todo: TodoEntity)
}
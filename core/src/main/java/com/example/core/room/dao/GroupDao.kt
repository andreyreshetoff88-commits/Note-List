package com.example.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.core.room.entitys.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupEntity)

    @Update
    suspend fun updateGroup(group: GroupEntity)

    @Query("SELECT * FROM group_table WHERE id = :groupId")
    fun getGroupById(groupId: String): Flow<GroupEntity?>

    @Delete
    suspend fun deleteGroup(group: GroupEntity)
}
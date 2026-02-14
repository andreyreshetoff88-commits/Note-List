package com.example.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.core.room.entitys.PurchaseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(purchase: PurchaseEntity)

    @Update
    suspend fun updateGroup(purchase: PurchaseEntity)

    @Query("SELECT * FROM purchases WHERE id = :purchaseId")
    fun getGroupById(purchaseId: String): Flow<PurchaseEntity?>

    @Delete
    suspend fun deleteGroup(purchase: PurchaseEntity)
}
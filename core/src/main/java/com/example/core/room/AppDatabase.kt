package com.example.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.core.room.dao.ChatMessageDao
import com.example.core.room.dao.GroupDao
import com.example.core.room.dao.PurchaseDao
import com.example.core.room.dao.TodoDao
import com.example.core.room.dao.UserDao
import com.example.core.room.entitys.ChatMessageEntity
import com.example.core.room.entitys.GroupEntity
import com.example.core.room.entitys.PurchaseEntity
import com.example.core.room.entitys.TodoEntity
import com.example.core.room.entitys.UserEntity

@Database(
    entities = [UserEntity::class, GroupEntity::class, PurchaseEntity::class, TodoEntity::class, ChatMessageEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun groupDao(): GroupDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun purchaseDao(): PurchaseDao
    abstract fun todoDao(): TodoDao
}

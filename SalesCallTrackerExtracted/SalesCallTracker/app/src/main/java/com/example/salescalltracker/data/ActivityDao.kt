package com.example.salescalltracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(activity: ActivityEntity)

    @Delete
    suspend fun delete(activity: ActivityEntity)

    @Query("SELECT * FROM activities ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activities WHERE personId = :personId ORDER BY timestamp DESC")
    fun observeForPerson(personId: String): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activities WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ActivityEntity?
}

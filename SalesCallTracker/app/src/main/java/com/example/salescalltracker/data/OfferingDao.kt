package com.example.salescalltracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OfferingDao {

    @Query("SELECT * FROM offerings ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<OfferingEntity>>

    @Query("""
        SELECT * FROM offerings
        WHERE isActive = 1
        ORDER BY createdAt DESC
    """)
    fun observeActive(): Flow<List<OfferingEntity>>

    @Query("""
        SELECT * FROM offerings
        WHERE ownerId = :ownerId
        ORDER BY createdAt DESC
    """)
    fun observeForOwner(ownerId: String): Flow<List<OfferingEntity>>

    @Query("""
        SELECT * FROM offerings
        WHERE type = :type
        AND isActive = 1
        ORDER BY createdAt DESC
    """)
    fun observeByType(type: String): Flow<List<OfferingEntity>>

    @Query("""
        SELECT * FROM offerings
        WHERE location = :location
        AND isActive = 1
        ORDER BY createdAt DESC
    """)
    fun observeByLocation(location: String): Flow<List<OfferingEntity>>

    @Query("SELECT * FROM offerings WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): OfferingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(offering: OfferingEntity)

    @Delete
    suspend fun delete(offering: OfferingEntity)
}

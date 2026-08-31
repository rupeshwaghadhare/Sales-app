package com.example.salescalltracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessProfileDao {

    @Query("SELECT * FROM business_profiles ORDER BY updatedAt DESC LIMIT 1")
    fun observeBusiness(): Flow<BusinessProfileEntity?>

    @Query("SELECT * FROM business_profiles ORDER BY updatedAt DESC LIMIT 1")
    suspend fun getBusiness(): BusinessProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(business: BusinessProfileEntity)

    @Delete
    suspend fun delete(business: BusinessProfileEntity)
}

package com.example.salescalltracker.data

import com.example.salescalltracker.model.Activity
import com.example.salescalltracker.model.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ActivityRepository {
    fun observePeople(): Flow<List<Person>>
    fun observeActivities(): Flow<List<Activity>>
    suspend fun upsertPerson(person: Person)
    suspend fun upsertActivity(activity: Activity)
    suspend fun getPersonById(id: String): Person?
    suspend fun getActivityById(id: String): Activity?
}

class RoomActivityRepository(
    private val database: AppDatabase,
) : ActivityRepository {

    override fun observePeople(): Flow<List<Person>> =
        database.personDao().observeAll().map { entities -> entities.map(PersonEntity::toDomain) }

    override fun observeActivities(): Flow<List<Activity>> =
        database.activityDao().observeAll().map { entities -> entities.map(ActivityEntity::toDomain) }

    override suspend fun upsertPerson(person: Person) {
        database.personDao().upsert(person.toEntity())
    }

    override suspend fun upsertActivity(activity: Activity) {
        database.activityDao().upsert(activity.toEntity())
    }

    override suspend fun getPersonById(id: String): Person? =
        database.personDao().getById(id)?.toDomain()

    override suspend fun getActivityById(id: String): Activity? =
        database.activityDao().getById(id)?.toDomain()
}

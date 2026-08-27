package com.example.salescalltracker.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.salescalltracker.model.Activity
import com.example.salescalltracker.model.ActivityType
import com.example.salescalltracker.model.Person
import com.example.salescalltracker.model.RelationshipType

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey val id: String,
    val name: String,
    val phoneNumber: String?,
    val relationshipType: String,
)

fun Person.toEntity(): PersonEntity = PersonEntity(
    id = id,
    name = name,
    phoneNumber = phoneNumber,
    relationshipType = relationshipType.name,
)

fun PersonEntity.toDomain(): Person = Person(
    id = id,
    name = name,
    phoneNumber = phoneNumber,
    relationshipType = RelationshipType.valueOf(relationshipType),
)

@Entity(
    tableName = "activities",
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index(value = ["personId"])],
)
data class ActivityEntity(
    @PrimaryKey val id: String,
    val personId: String?,
    val type: String,
    val title: String,
    val description: String,
    val timestamp: Long,
    val completed: Boolean,
    val followUpDate: Long?,
)

fun Activity.toEntity(): ActivityEntity = ActivityEntity(
    id = id,
    personId = personId,
    type = type.name,
    title = title,
    description = description,
    timestamp = timestamp,
    completed = completed,
    followUpDate = followUpDate,
)

fun ActivityEntity.toDomain(): Activity = Activity(
    id = id,
    personId = personId,
    type = ActivityType.valueOf(type),
    title = title,
    description = description,
    timestamp = timestamp,
    completed = completed,
    followUpDate = followUpDate,
)

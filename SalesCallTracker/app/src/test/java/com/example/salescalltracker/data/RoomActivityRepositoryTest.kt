package com.example.salescalltracker.data

import com.example.salescalltracker.model.Activity
import com.example.salescalltracker.model.ActivityType
import com.example.salescalltracker.model.Person
import com.example.salescalltracker.model.RelationshipType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RoomActivityRepositoryTest {

    @Test
    fun activityAndPersonEntityMappings_roundTrip() {
        val person = Person(
            id = "person-1",
            name = "Aisha Khan",
            phoneNumber = "+15550012345",
            relationshipType = RelationshipType.CUSTOMER,
        )

        val personEntity = person.toEntity()
        val restoredPerson = personEntity.toDomain()

        assertEquals(person.id, restoredPerson.id)
        assertEquals(person.name, restoredPerson.name)
        assertEquals(person.phoneNumber, restoredPerson.phoneNumber)
        assertEquals(person.relationshipType, restoredPerson.relationshipType)

        val activity = Activity(
            id = "activity-1",
            personId = person.id,
            type = ActivityType.CALL,
            title = "Discovery call",
            description = "Discuss new scope",
            timestamp = 1710000000000L,
            completed = false,
            followUpDate = 1710003600000L,
        )

        val activityEntity = activity.toEntity()
        val restoredActivity = activityEntity.toDomain()

        assertEquals(activity.id, restoredActivity.id)
        assertEquals(activity.personId, restoredActivity.personId)
        assertEquals(activity.type, restoredActivity.type)
        assertEquals(activity.title, restoredActivity.title)
        assertEquals(activity.description, restoredActivity.description)
        assertEquals(activity.timestamp, restoredActivity.timestamp)
        assertEquals(activity.completed, restoredActivity.completed)
        assertEquals(activity.followUpDate, restoredActivity.followUpDate)
        assertTrue(restoredActivity.personId == person.id)
    }
}
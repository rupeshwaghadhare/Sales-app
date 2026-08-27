package com.example.salescalltracker.data

import com.example.salescalltracker.model.Activity
import com.example.salescalltracker.model.ActivityType
import com.example.salescalltracker.model.Person
import com.example.salescalltracker.model.RelationshipType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ActivityRepositoryTest {

    @Test
    fun relationshipTypesIncludeRequiredValues() {
        val required = setOf(
            RelationshipType.FAMILY,
            RelationshipType.RELATIVE,
            RelationshipType.FRIEND,
            RelationshipType.CUSTOMER,
            RelationshipType.CLIENT,
            RelationshipType.PROSPECT,
            RelationshipType.BUSINESS_PARTNER,
            RelationshipType.COLLEAGUE,
            RelationshipType.STUDENT,
            RelationshipType.TEACHER,
            RelationshipType.OTHER,
        )

        assertEquals(required, RelationshipType.values().toSet())
    }

    @Test
    fun activityAndPersonModelsStoreCoreFields() {
        val person = Person(
            id = "person-1",
            name = "Aisha Khan",
            phoneNumber = "+15550012345",
            relationshipType = RelationshipType.CUSTOMER,
        )

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

        assertEquals("person-1", person.id)
        assertEquals("activity-1", activity.id)
        assertEquals(ActivityType.CALL, activity.type)
        assertEquals(RelationshipType.CUSTOMER, person.relationshipType)
        assertTrue(activity.personId != null)
        assertTrue(activity.followUpDate != null)
    }
}

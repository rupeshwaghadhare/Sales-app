package com.example.salescalltracker.model

enum class RelationshipType {
    FAMILY,
    RELATIVE,
    FRIEND,
    CUSTOMER,
    CLIENT,
    PROSPECT,
    BUSINESS_PARTNER,
    COLLEAGUE,
    STUDENT,
    TEACHER,
    SERVICE_PROVIDER,
    FREELANCER,
    CONSULTANT,
    OTHER
}

enum class ActivityType {
    CALL,
    NOTE,
    TASK,
    MEETING,
    FOLLOW_UP,
    WHATSAPP,
    OTHER
}

data class Person(
    val id: String,
    val name: String,
    val phoneNumber: String? = null,
    val relationshipTypes: Set<RelationshipType> = emptySet(),
) {
    val relationshipType: RelationshipType
        get() = relationshipTypes.firstOrNull() ?: RelationshipType.OTHER
}

data class Activity(
    val id: String,
    val personId: String? = null,
    val type: ActivityType,
    val title: String,
    val description: String = "",
    val timestamp: Long,
    val completed: Boolean = false,
    val followUpDate: Long? = null
)

package com.example.salescalltracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        PersonEntity::class,
        ActivityEntity::class,
        ConversationEntity::class,
        ConversationMemberEntity::class,
        ChatMessageEntity::class,
        BusinessProfileEntity::class,
        OfferingEntity::class,
        MediaAttachmentEntity::class
    ],
    version = 7,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun personDao(): PersonDao
    abstract fun activityDao(): ActivityDao
    abstract fun conversationDao(): ConversationDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun conversationMemberDao(): ConversationMemberDao
    abstract fun businessProfileDao(): BusinessProfileDao
    abstract fun offeringDao(): OfferingDao
    abstract fun mediaAttachmentDao(): MediaAttachmentDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE persons ADD COLUMN relationshipTypes TEXT NOT NULL DEFAULT ''"
                )

                db.execSQL(
                    """
                    UPDATE persons
                    SET relationshipTypes =
                        CASE
                            WHEN relationshipType IS NOT NULL
                            AND relationshipType != ''
                            THEN relationshipType
                            ELSE ''
                        END
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS conversations (
                        id TEXT NOT NULL PRIMARY KEY,
                        personId TEXT,
                        name TEXT NOT NULL DEFAULT '',
                        type TEXT NOT NULL DEFAULT 'DIRECT',
                        lastMessage TEXT NOT NULL,
                        lastMessageTimestamp INTEGER NOT NULL,
                        unreadCount INTEGER NOT NULL,
                        isPinned INTEGER NOT NULL,
                        isMuted INTEGER NOT NULL,
                        isArchived INTEGER NOT NULL,
                        FOREIGN KEY(personId)
                            REFERENCES persons(id)
                            ON DELETE SET NULL
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    index_conversations_personId
                    ON conversations(personId)
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    index_conversations_type
                    ON conversations(type)
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS chat_messages (
                        id TEXT NOT NULL PRIMARY KEY,
                        conversationId TEXT NOT NULL,
                        senderType TEXT NOT NULL,
                        messageType TEXT NOT NULL,
                        text TEXT NOT NULL,
                        timestamp INTEGER NOT NULL,
                        isRead INTEGER NOT NULL,
                        attachmentUri TEXT,
                        FOREIGN KEY(conversationId)
                            REFERENCES conversations(id)
                            ON DELETE CASCADE
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    index_chat_messages_conversationId
                    ON chat_messages(conversationId)
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    index_chat_messages_timestamp
                    ON chat_messages(timestamp)
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {

            override fun migrate(db: SupportSQLiteDatabase) {

                // Existing conversations are direct chats.
                db.execSQL(
                    """
                    UPDATE conversations
                    SET type = 'DIRECT'
                    """.trimIndent()
                )

                // Existing conversations use the person's ID
                // until the UI supplies a custom name.
                db.execSQL(
                    """
                    UPDATE conversations
                    SET name = COALESCE(personId, '')
                    WHERE name = ''
                    """.trimIndent()
                )

                // Remove the old unique constraint.
                db.execSQL(
                    """
                    DROP INDEX IF EXISTS index_conversations_personId
                    """.trimIndent()
                )

                // New indexes.
                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    index_conversations_personId
                    ON conversations(personId)
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    index_conversations_type
                    ON conversations(type)
                    """.trimIndent()
                )

                // Members table.
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS conversation_members (
                        conversationId TEXT NOT NULL,
                        personId TEXT NOT NULL,
                        role TEXT NOT NULL DEFAULT 'MEMBER',
                        joinedAt INTEGER NOT NULL,
                        PRIMARY KEY(conversationId, personId),
                        FOREIGN KEY(conversationId)
                            REFERENCES conversations(id)
                            ON DELETE CASCADE,
                        FOREIGN KEY(personId)
                            REFERENCES persons(id)
                            ON DELETE CASCADE
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    index_conversation_members_conversationId
                    ON conversation_members(conversationId)
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    index_conversation_members_personId
                    ON conversation_members(personId)
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS business_profiles (
                        id TEXT NOT NULL PRIMARY KEY,
                        businessName TEXT NOT NULL,
                        category TEXT NOT NULL,
                        phone TEXT,
                        whatsapp TEXT,
                        location TEXT,
                        description TEXT,
                        logoUri TEXT,
                        websiteUrl TEXT,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS offerings (
                        id TEXT NOT NULL PRIMARY KEY,
                        ownerId TEXT NOT NULL,
                        workspaceId TEXT,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL,
                        type TEXT NOT NULL,
                        price REAL,
                        currency TEXT NOT NULL,
                        location TEXT,
                        imageUri TEXT,
                        contact TEXT,
                        websiteUrl TEXT,
                        isActive INTEGER NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_offerings_ownerId ON offerings(ownerId)"
                )

                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_offerings_workspaceId ON offerings(workspaceId)"
                )

                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_offerings_type ON offerings(type)"
                )

                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_offerings_location ON offerings(location)"
                )

                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_offerings_isActive ON offerings(isActive)"
                )
            }
        }


          private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS media_attachments (
                        id TEXT NOT NULL PRIMARY KEY,
                        workspaceId TEXT,
                        ownerId TEXT NOT NULL,
                        ownerType TEXT NOT NULL,
                        type TEXT NOT NULL,
                        uri TEXT,
                        title TEXT,
                        description TEXT,
                        sortOrder INTEGER NOT NULL,
                        createdAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    index_media_attachments_ownerId
                    ON media_attachments(ownerId)
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    index_media_attachments_ownerType
                    ON media_attachments(ownerType)
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    index_media_attachments_workspaceId
                    ON media_attachments(workspaceId)
                    """.trimIndent()
                )
            }
        }
      fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {

                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sales_activity_db",
                )
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6,
                        MIGRATION_6_7
                    )
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
    }
}






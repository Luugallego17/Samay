package com.samay.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.samay.app.data.kit.Kit
import com.samay.app.data.kit.KitDao
import com.samay.app.data.kit.KitType
import com.samay.app.data.contact.Contact
import com.samay.app.data.contact.ContactDao

@Database(
    entities = [Kit::class, Contact::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun kitDao(): KitDao
    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "samay.db"
                )
                .fallbackToDestructiveMigration() // Dev only, drop tables if version changes
                .build().also { INSTANCE = it }
            }
    }
}

class Converters {
    @TypeConverter fun fromKitType(value: KitType): String = value.name
    @TypeConverter fun toKitType(value: String): KitType = KitType.valueOf(value)
}

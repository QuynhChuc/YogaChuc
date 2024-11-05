package com.example.yogaappadmin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [YogaClass::class], version = 4)
abstract class YogaDatabase : RoomDatabase() {

    abstract fun yogaDao(): YogaDao

    companion object {
        @Volatile
        private var INSTANCE: YogaDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                try {
                    database.execSQL("ALTER TABLE Chucyoga_class ADD COLUMN teacher TEXT NOT NULL DEFAULT 'Unknown'")
                } catch (e: Exception) {
                    // Ghi lại lỗi hoặc xử lý theo nhu cầu
                }
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                val cursor = database.query("SELECT name FROM sqlite_master WHERE type='table' AND name='Chucyoga_class'")
                if (cursor.moveToFirst()) {
                    val tableInfo = database.query("PRAGMA table_info(Chucyoga_class)")
                    val columnNames = mutableListOf<String>()
                    while (tableInfo.moveToNext()) {
                        columnNames.add(tableInfo.getString(1)) // 1 là chỉ số cho tên cột
                    }
                    tableInfo.close()

                    try {
                        if (!columnNames.contains("duration")) {
                            database.execSQL("ALTER TABLE Chucyoga_class ADD COLUMN duration INTEGER NOT NULL DEFAULT 0")
                        }
                        if (!columnNames.contains("price")) {
                            database.execSQL("ALTER TABLE Chucyoga_class ADD COLUMN price REAL NOT NULL DEFAULT 0.0")
                        }
                        if (!columnNames.contains("type")) {
                            database.execSQL("ALTER TABLE Chucyoga_class ADD COLUMN type TEXT NOT NULL DEFAULT ''")
                        }
                        if (!columnNames.contains("description")) {
                            database.execSQL("ALTER TABLE Chucyoga_class ADD COLUMN description TEXT")
                        }
                    } catch (e: Exception) {
                        // Ghi lại lỗi hoặc xử lý theo nhu cầu
                    }
                }
                cursor.close()
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Không cần thay đổi gì cho phiên bản này
            }
        }

        fun getDatabase(context: Context): YogaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    YogaDatabase::class.java,
                    "yoga_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

package com.al4apps.mycomposeapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.al4apps.mycomposeapp.data.dbmodels.FundDbModel
import com.al4apps.mycomposeapp.data.dbmodels.FundMemberDbModel
import com.al4apps.mycomposeapp.data.dbmodels.MemberToFund

@Database(
    entities = [
        FundMemberDbModel::class,
        FundDbModel::class,
    ],
    version = AppDatabase.DB_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun fundsDao(): FundsDao
    abstract fun fundItemsDao(): FundMembersDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "app_db"
    }
}
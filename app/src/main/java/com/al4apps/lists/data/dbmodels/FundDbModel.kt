package com.al4apps.lists.data.dbmodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = FundDbModel.TABLE_NAME)
data class FundDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID_NAME) val id: Int? = null,
    @ColumnInfo(name = NAME) var name: String,
    @ColumnInfo(name = TO_RAISE) var toRaise: Long?,
    @ColumnInfo(name = TIMESTAMP) var timestamp: Long
) {
    companion object {
        const val TABLE_NAME = "fund"
        const val ID_NAME = "id"
        const val NAME = "name"
        const val TO_RAISE = "to_raise"
        const val TIMESTAMP = "timestamp"
    }
}
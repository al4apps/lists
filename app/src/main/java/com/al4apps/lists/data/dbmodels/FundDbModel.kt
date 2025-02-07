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

@Entity(tableName = FundOptionsDb.TABLE_NAME)
data class FundOptionsDb(
    @PrimaryKey
    @ColumnInfo(name = FUND_ID)
    val fundId: Int,
    @ColumnInfo(name = NEED_TO_DIVIDE)
    val needToDivide: Boolean
) {
    companion object {
        const val TABLE_NAME = "fund_options"
        const val FUND_ID = "fund_id"
        const val NEED_TO_DIVIDE = "need_to_divide"
    }
}
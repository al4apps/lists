package com.al4apps.lists.data.dbmodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = MemberToFund.TABLE_NAME)
data class MemberToFund(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Int? = null,
    @ColumnInfo(name = MEMBER_ID)
    val memberId: Int,
    @ColumnInfo(name = FUND_ID)
    val fundId: Int,
) {
    companion object {
        const val TABLE_NAME = "member_to_fund"
        const val ID = "id"
        const val MEMBER_ID = "member_id"
        const val FUND_ID = "fund_id"
    }
}
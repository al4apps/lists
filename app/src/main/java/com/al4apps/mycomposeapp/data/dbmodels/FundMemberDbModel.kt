package com.al4apps.mycomposeapp.data.dbmodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.al4apps.mycomposeapp.domain.models.FundMember

@Entity(tableName = FundMemberDbModel.TABLE_NAME)
data class FundMemberDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID_NAME)
    val id: Int? = null,
    @ColumnInfo(name = NAME)
    val name: String,
    @ColumnInfo(name = FUND_ID_NAME)
    val fundId: Int,
    @ColumnInfo(name = SUM_NAME)
    val sum: Long,
    @ColumnInfo(name = COMMENT_NAME)
    var comment: String?,
    @ColumnInfo(name = TIMESTAMP)
    var timestamp: Long
) {
    companion object {
        const val TABLE_NAME = "fund_member"
        const val ID_NAME = "id"
        const val FUND_ID_NAME = "fund_id"
        const val NAME = "name"
        const val SUM_NAME = "sum"
        const val COMMENT_NAME = "comment"
        const val TIMESTAMP = "timestamp"
    }
}

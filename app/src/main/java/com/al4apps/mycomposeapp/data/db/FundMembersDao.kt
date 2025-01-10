package com.al4apps.mycomposeapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.al4apps.mycomposeapp.data.dbmodels.FundDbModel
import com.al4apps.mycomposeapp.data.dbmodels.FundMemberDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FundMembersDao {
    @Query("SELECT * FROM ${FundMemberDbModel.TABLE_NAME} WHERE ${FundMemberDbModel.FUND_ID_NAME} = :fundId")
    suspend fun getAllFundMembers(fundId: Int): List<FundMemberDbModel>

    @Query("SELECT * FROM ${FundMemberDbModel.TABLE_NAME} WHERE ${FundMemberDbModel.ID_NAME} = :id")
    suspend fun getFundMemberById(id: Int): FundMemberDbModel

    @Query("SELECT * FROM ${FundMemberDbModel.TABLE_NAME} WHERE ${FundMemberDbModel.FUND_ID_NAME} = :fundId")
    fun allFundMembers(fundId: Int): Flow<List<FundMemberDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFundMember(fundMember: FundMemberDbModel)

    @Query("DELETE FROM ${FundMemberDbModel.TABLE_NAME} WHERE ${FundMemberDbModel.ID_NAME} = :fundMemberId")
    suspend fun deleteFundMember(fundMemberId: Int)


}
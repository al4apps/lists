package com.al4apps.lists.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.al4apps.lists.data.dbmodels.FundDbModel
import com.al4apps.lists.data.dbmodels.FundMemberDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FundsDao {
    @Query("SELECT * FROM ${FundDbModel.TABLE_NAME}")
    suspend fun getAllFunds(): List<FundDbModel>

    @Query("SELECT * FROM ${FundDbModel.TABLE_NAME} WHERE ${FundDbModel.ID_NAME} = :id")
    suspend fun getFundById(id: Int): FundDbModel

    @Query("SELECT * FROM ${FundDbModel.TABLE_NAME} WHERE ${FundDbModel.ID_NAME} = :id")
    fun fundByIdFlow(id: Int): Flow<FundDbModel>

    @Query("SELECT * FROM ${FundDbModel.TABLE_NAME} ORDER BY ${FundDbModel.TIMESTAMP} DESC")
    fun allFunds(): Flow<List<FundDbModel>>

    @Transaction
    suspend fun deleteFund(fundId: Int) {
        deleteFundDb(fundId)
        deleteFundMembersByFundId(fundId)
    }

    @Query("DELETE FROM ${FundDbModel.TABLE_NAME} WHERE ${FundDbModel.ID_NAME} = :fundId")
    suspend fun deleteFundDb(fundId: Int)


    @Query("DELETE FROM ${FundMemberDbModel.TABLE_NAME} WHERE ${FundMemberDbModel.FUND_ID_NAME} = :fundId")
    suspend fun deleteFundMembersByFundId(fundId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFund(fund: FundDbModel): Long

}
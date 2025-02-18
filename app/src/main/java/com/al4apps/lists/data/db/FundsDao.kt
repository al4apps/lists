package com.al4apps.lists.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.al4apps.lists.data.dbmodels.FundDbModel
import com.al4apps.lists.data.dbmodels.FundMemberDbModel
import com.al4apps.lists.data.dbmodels.FundOptionsDb
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
        deleteFundOptions(fundId)
        deleteFundMembersByFundId(fundId)
    }

    @Query("DELETE FROM ${FundDbModel.TABLE_NAME} WHERE ${FundDbModel.ID_NAME} = :fundId")
    suspend fun deleteFundDb(fundId: Int)

    @Query("DELETE FROM ${FundOptionsDb.TABLE_NAME} WHERE ${FundOptionsDb.FUND_ID} = :fundId")
    suspend fun deleteFundOptions(fundId: Int)

    @Query("DELETE FROM ${FundMemberDbModel.TABLE_NAME} WHERE ${FundMemberDbModel.FUND_ID_NAME} = :fundId")
    suspend fun deleteFundMembersByFundId(fundId: Int)

    @Query("SELECT * FROM ${FundOptionsDb.TABLE_NAME} WHERE ${FundOptionsDb.FUND_ID} = :fundId")
    suspend fun getFundOptions(fundId: Int): FundOptionsDb

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFund(fund: FundDbModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFundOptions(fundOptions: FundOptionsDb)

}
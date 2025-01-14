package com.al4apps.lists.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.al4apps.lists.data.dbmodels.FundDbModel
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFund(fund: FundDbModel): Long

    @Query("DELETE FROM ${FundDbModel.TABLE_NAME} WHERE ${FundDbModel.ID_NAME} = :fundId")
    suspend fun deleteFund(fundId: Int)
}
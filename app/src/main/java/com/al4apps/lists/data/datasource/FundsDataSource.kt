package com.al4apps.lists.data.datasource

import com.al4apps.lists.data.dbmodels.FundDbModel
import kotlinx.coroutines.flow.Flow

interface FundsDataSource {
    suspend fun getAllFunds(): List<FundDbModel>
    suspend fun getFundById(id: Int): FundDbModel
    fun allFunds(): Flow<List<FundDbModel>>
    suspend fun addFund(fund: FundDbModel)
    suspend fun updateFund(fund: FundDbModel)
    suspend fun deleteFund(fundId: Int)
}
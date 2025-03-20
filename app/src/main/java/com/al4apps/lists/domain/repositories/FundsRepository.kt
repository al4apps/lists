package com.al4apps.lists.domain.repositories

import com.al4apps.lists.domain.models.FundModel
import kotlinx.coroutines.flow.Flow

interface FundsRepository {
    suspend fun getAllFunds(): List<FundModel>
    suspend fun getFundById(id: Int): FundModel
    fun fundByIdFlow(id: Int): Flow<FundModel>
    fun allFunds(): Flow<List<FundModel>>
    suspend fun addNewFund(fund: FundModel): Int
    suspend fun updateFund(fund: FundModel)
    suspend fun deleteFund(fundId: Int)
    suspend fun getFundRaisedSum(fundId: Int): Long
}
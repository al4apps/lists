package com.al4apps.lists.data.repositories

import com.al4apps.lists.data.db.FundMembersDao
import com.al4apps.lists.data.db.FundsDao
import com.al4apps.lists.data.dbmodels.FundDbModel
import com.al4apps.lists.domain.models.FundModel
import com.al4apps.lists.domain.repositories.FundsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FundsRepositoryImpl(
    private val fundsDao: FundsDao,
    private val membersDao: FundMembersDao
) : FundsRepository {

    private val noIdThrowable = IllegalArgumentException("Model from db without id")

    override suspend fun getAllFunds(): List<FundModel> {
        return fundsDao.getAllFunds().map { fund ->
            val raised = getFundRaisedSum(fund.id ?: throw noIdThrowable)
            fund.toFundModel(raised)
        }
    }

    override suspend fun getFundById(id: Int): FundModel {
        val raised = getFundRaisedSum(id)
        return fundsDao.getFundById(id).toFundModel(raised)
    }

    override fun allFunds(): Flow<List<FundModel>> {
        return fundsDao.allFunds().map {
            it.map { fund ->
                val raised = getFundRaisedSum(fund.id ?: throw noIdThrowable)
                fund.toFundModel(raised)
            }
        }
    }

    override suspend fun addNewFund(fund: FundModel): Int {
        return fundsDao.addFund(fund.toNewDbModel()).toInt()
    }

    override suspend fun updateFund(fund: FundModel) {
        fundsDao.addFund(fund.toDbModel())
    }

    override suspend fun deleteFund(fundId: Int) {
        fundsDao.deleteFund(fundId)
    }

    override suspend fun getFundRaisedSum(fundId: Int): Long {
        return membersDao.getAllFundMembers(fundId).sumOf { it.sum }
    }


    private fun FundModel.toDbModel(): FundDbModel {
        return FundDbModel(
            id = id,
            name = name,
            toRaise = toRaise,
            timestamp = timestamp
        )
    }

    private fun FundModel.toNewDbModel(): FundDbModel {
        return FundDbModel(
            name = name,
            toRaise = toRaise,
            timestamp = timestamp
        )
    }

    private fun FundDbModel.toFundModel(raised: Long): FundModel {
        return FundModel(
            id = id ?: throw noIdThrowable,
            name = name,
            toRaise = toRaise,
            raised = raised,
            timestamp = timestamp
        )
    }
}
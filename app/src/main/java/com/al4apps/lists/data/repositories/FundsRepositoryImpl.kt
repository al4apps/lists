package com.al4apps.lists.data.repositories

import com.al4apps.lists.data.db.FundMembersDao
import com.al4apps.lists.data.db.FundsDao
import com.al4apps.lists.data.dbmodels.FundDbModel
import com.al4apps.lists.data.dbmodels.FundOptionsDb
import com.al4apps.lists.domain.models.FundModel
import com.al4apps.lists.domain.models.FundOptionsModel
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

    override suspend fun getFundOptionsById(id: Int): FundOptionsModel {
        val members = membersDao.getAllFundMembers(id)
        val optionsDb = fundsDao.getFundOptions(id)
        return FundOptionsModel(
            optionsDb.fundId,
            members.size,
            members.sumOf { it.sum },
            optionsDb.needToDivide
        )
    }

    override fun fundByIdFlow(id: Int): Flow<FundModel> {
        return fundsDao.fundByIdFlow(id).map {
            val raised = getFundRaisedSum(id)
            it.toFundModel(raised)
        }
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

    override suspend fun addNewFundWithOptions(fund: FundModel, options: FundOptionsModel) {
        fundsDao.addFund(fund.toNewDbModel())
        fundsDao.addFundOptions(options.toFundOptionsDb())
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

    override suspend fun updateFundOptions(fundOptions: FundOptionsModel) {
        fundsDao.addFundOptions(fundOptions.toFundOptionsDb())
    }

    private fun FundOptionsModel.toFundOptionsDb() =
        FundOptionsDb(
            fundId = fundId,
            needToDivide = needToDivide,
        )


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
package com.al4apps.mycomposeapp.data.repositories

import com.al4apps.mycomposeapp.data.db.FundMembersDao
import com.al4apps.mycomposeapp.data.dbmodels.FundMemberDbModel
import com.al4apps.mycomposeapp.domain.models.FundMemberModel
import com.al4apps.mycomposeapp.domain.repositories.FundMembersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FundMembersRepositoryImpl(
    private val membersDao: FundMembersDao,
) : FundMembersRepository {
    override suspend fun getAllFundMembers(fundId: Int): List<FundMemberModel> {
        return membersDao.getAllFundMembers(fundId).map { fundMember ->
            fundMember.toFundMemberModel()
        }
    }

    override suspend fun getFundMemberById(id: Int): FundMemberModel {
        return membersDao.getFundMemberById(id).toFundMemberModel()
    }

    override fun allFundMembers(fundId: Int): Flow<List<FundMemberModel>> {
        return membersDao.allFundMembers(fundId).map {
            it.map { fundMember ->
                fundMember.toFundMemberModel()
            }
        }
    }

    override suspend fun addNewFundMember(fundMember: FundMemberModel) {
        membersDao.addFundMember(fundMember.toNewDbModel())
    }

    override suspend fun updateFundMember(fundMember: FundMemberModel) {
        membersDao.addFundMember(fundMember.toDbModel())
    }

    override suspend fun deleteFundMember(memberId: Int) {
        membersDao.deleteFundMember(memberId)
    }

    private fun FundMemberDbModel.toFundMemberModel(): FundMemberModel {
        return FundMemberModel(
            id = id ?: throw IllegalArgumentException("Model from db without id"),
            fundId = fundId,
            name = name,
            sum = sum,
            comment = comment,
            timestamp = timestamp
        )
    }

    private fun FundMemberModel.toDbModel(): FundMemberDbModel {
        return FundMemberDbModel(
            id = id,
            fundId = fundId,
            name = name,
            sum = sum,
            comment = comment,
            timestamp = timestamp
        )
    }

    private fun FundMemberModel.toNewDbModel(): FundMemberDbModel {
        return FundMemberDbModel(
            fundId = fundId,
            name = name,
            sum = sum,
            comment = comment,
            timestamp = timestamp
        )
    }
}

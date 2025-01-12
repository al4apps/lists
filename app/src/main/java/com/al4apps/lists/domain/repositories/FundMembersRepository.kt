package com.al4apps.lists.domain.repositories

import com.al4apps.lists.domain.models.FundMemberModel
import kotlinx.coroutines.flow.Flow

interface FundMembersRepository {
    suspend fun getAllFundMembers(fundId: Int): List<FundMemberModel>
    suspend fun getFundMemberById(id: Int): FundMemberModel
    fun allFundMembers(fundId: Int): Flow<List<FundMemberModel>>
    suspend fun addNewFundMember(fundMember: FundMemberModel)
    suspend fun updateFundMember(fundMember: FundMemberModel)
    suspend fun deleteFundMember(memberId: Int)

}
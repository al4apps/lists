package com.al4apps.lists.domain.usecases

import com.al4apps.lists.domain.models.FundMemberModel
import com.al4apps.lists.domain.repositories.FundMembersRepository
import kotlinx.coroutines.flow.Flow

class GetFundMembersUseCase(
    private val fundMembersRepository: FundMembersRepository
) {
    fun flow(fundId: Int): Flow<List<FundMemberModel>> {
        return fundMembersRepository.allFundMembers(fundId)
    }

    suspend operator fun invoke(fundId: Int): List<FundMemberModel> {
        return fundMembersRepository.getAllFundMembers(fundId)
    }
}


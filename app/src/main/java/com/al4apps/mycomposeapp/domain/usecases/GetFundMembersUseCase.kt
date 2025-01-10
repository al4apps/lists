package com.al4apps.mycomposeapp.domain.usecases

import com.al4apps.mycomposeapp.data.repositories.FundMembersRepositoryImpl
import com.al4apps.mycomposeapp.domain.models.FundMemberModel
import kotlinx.coroutines.flow.Flow

class GetFundMembersUseCase(
    private val fundMembersRepository: FundMembersRepositoryImpl
) {
    fun flow(fundId: Int): Flow<List<FundMemberModel>> {
        return fundMembersRepository.allFundMembers(fundId)
    }

    suspend operator fun invoke(fundId: Int): List<FundMemberModel> {
        return fundMembersRepository.getAllFundMembers(fundId)
    }
}


package com.al4apps.mycomposeapp.domain.usecases

import com.al4apps.mycomposeapp.data.repositories.FundMembersRepositoryImpl
import com.al4apps.mycomposeapp.domain.models.FundMemberModel

class UpdateFundMemberUseCase(
    private val fundMemberRepository: FundMembersRepositoryImpl
) {
    suspend fun update(fundMember: FundMemberModel) {
        fundMemberRepository.updateFundMember(fundMember)
    }

    suspend fun delete(memberId: Int) {
        fundMemberRepository.deleteFundMember(memberId)
    }

}
package com.al4apps.lists.domain.usecases

import com.al4apps.lists.data.repositories.FundMembersRepositoryImpl
import com.al4apps.lists.domain.models.FundMemberModel

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
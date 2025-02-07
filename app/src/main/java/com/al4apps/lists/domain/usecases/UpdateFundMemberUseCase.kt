package com.al4apps.lists.domain.usecases

import com.al4apps.lists.domain.models.FundMemberModel
import com.al4apps.lists.domain.repositories.FundMembersRepository

class UpdateFundMemberUseCase(
    private val fundMemberRepository: FundMembersRepository
) {
    suspend fun update(fundMember: FundMemberModel) {
        fundMemberRepository.updateFundMember(fundMember)
    }

    suspend fun delete(memberId: Int) {
        fundMemberRepository.deleteFundMember(memberId)
    }

}
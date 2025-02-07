package com.al4apps.lists.domain.usecases

import com.al4apps.lists.domain.models.FundMemberModel
import com.al4apps.lists.domain.repositories.FundMembersRepository

class AddNewFundMemberUseCase(
    private val fundMemberRepository: FundMembersRepository
) {
    suspend fun add(fundMember: FundMemberModel) {
        fundMemberRepository.addNewFundMember(fundMember)
    }

}
package com.al4apps.lists.domain.usecases

import com.al4apps.lists.data.repositories.FundMembersRepositoryImpl
import com.al4apps.lists.domain.models.FundMemberModel

class AddNewFundMemberUseCase(
    private val fundMemberRepository: FundMembersRepositoryImpl
) {
    suspend fun add(fundMember: FundMemberModel) {
        fundMemberRepository.addNewFundMember(fundMember)
    }

}
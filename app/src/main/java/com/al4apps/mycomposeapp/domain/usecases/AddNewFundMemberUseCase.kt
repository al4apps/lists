package com.al4apps.mycomposeapp.domain.usecases

import com.al4apps.mycomposeapp.data.repositories.FundMembersRepositoryImpl
import com.al4apps.mycomposeapp.domain.models.FundMemberModel

class AddNewFundMemberUseCase(
    private val fundMemberRepository: FundMembersRepositoryImpl
) {
    suspend fun add(fundMember: FundMemberModel) {
        fundMemberRepository.addNewFundMember(fundMember)
    }

}
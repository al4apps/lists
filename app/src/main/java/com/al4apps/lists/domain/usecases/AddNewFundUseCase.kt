package com.al4apps.lists.domain.usecases

import com.al4apps.lists.domain.models.FundModel
import com.al4apps.lists.domain.repositories.FundsRepository

class AddNewFundUseCase(
    private val fundsRepository: FundsRepository
) {
    suspend operator fun invoke(fund: FundModel): Int {
        return fundsRepository.addNewFund(fund)
    }
}
package com.al4apps.lists.domain.usecases

import com.al4apps.lists.data.repositories.FundsRepositoryImpl
import com.al4apps.lists.domain.models.FundModel

class AddNewFundUseCase(
    private val fundsRepository: FundsRepositoryImpl
) {
    suspend operator fun invoke(fund: FundModel): Int {
        return fundsRepository.addNewFund(fund)
    }
}
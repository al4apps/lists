package com.al4apps.mycomposeapp.domain.usecases

import com.al4apps.mycomposeapp.data.repositories.FundsRepositoryImpl
import com.al4apps.mycomposeapp.domain.models.FundModel

class AddNewFundUseCase(
    private val fundsRepository: FundsRepositoryImpl
) {
    suspend operator fun invoke(fund: FundModel): Int {
        return fundsRepository.addNewFund(fund)
    }
}
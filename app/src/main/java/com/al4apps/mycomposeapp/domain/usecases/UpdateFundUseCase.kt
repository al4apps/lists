package com.al4apps.mycomposeapp.domain.usecases

import com.al4apps.mycomposeapp.data.repositories.FundsRepositoryImpl
import com.al4apps.mycomposeapp.domain.models.FundModel

class UpdateFundUseCase(
    private val fundsRepository: FundsRepositoryImpl
) {
    suspend fun update(fund: FundModel) {
        fundsRepository.updateFund(fund)
    }
    suspend fun delete(fundId: Int) {
        fundsRepository.deleteFund(fundId)
    }
}
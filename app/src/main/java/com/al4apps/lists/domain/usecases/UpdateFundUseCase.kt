package com.al4apps.lists.domain.usecases

import com.al4apps.lists.data.repositories.FundsRepositoryImpl
import com.al4apps.lists.domain.models.FundModel

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
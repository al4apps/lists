package com.al4apps.lists.domain.usecases

import com.al4apps.lists.domain.models.FundModel
import com.al4apps.lists.domain.models.FundOptionsModel
import com.al4apps.lists.domain.repositories.FundsRepository

class UpdateFundUseCase(
    private val fundsRepository: FundsRepository
) {
    suspend fun update(fund: FundModel, options: FundOptionsModel) {
        fundsRepository.updateFund(fund)
        fundsRepository.updateFundOptions(options)
    }

    suspend fun update(fund: FundModel) {
        fundsRepository.updateFund(fund)
    }

    suspend fun delete(fundId: Int) {
        fundsRepository.deleteFund(fundId)
    }
}
package com.al4apps.lists.domain.usecases

import com.al4apps.lists.domain.models.FundOptionsModel
import com.al4apps.lists.domain.repositories.FundsRepository

class GetFundOptionsUseCase(
    private val fundsRepository: FundsRepository
) {
    suspend fun get(fundId: Int): FundOptionsModel {
        return fundsRepository.getFundOptionsById(fundId)
    }
}
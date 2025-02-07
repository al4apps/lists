package com.al4apps.lists.domain.usecases

import com.al4apps.lists.domain.repositories.FundsRepository

class GetFundUseCase(
    private val fundsRepository: FundsRepository
) {
    suspend fun get(fundId: Int) = fundsRepository.getFundById(fundId)

    fun flow(fundId: Int) = fundsRepository.fundByIdFlow(id = fundId)
}
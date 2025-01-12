package com.al4apps.lists.domain.usecases

import com.al4apps.lists.data.repositories.FundsRepositoryImpl

class GetFundUseCase(
    private val fundsRepository: FundsRepositoryImpl
) {
    suspend fun get(fundId: Int) = fundsRepository.getFundById(fundId)
}
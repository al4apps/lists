package com.al4apps.mycomposeapp.domain.usecases

import com.al4apps.mycomposeapp.data.repositories.FundsRepositoryImpl

class GetFundUseCase(
    private val fundsRepository: FundsRepositoryImpl
) {
    suspend fun get(fundId: Int) = fundsRepository.getFundById(fundId)
}
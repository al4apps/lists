package com.al4apps.lists.domain.usecases

import com.al4apps.lists.domain.models.FundModel
import com.al4apps.lists.domain.repositories.FundsRepository
import kotlinx.coroutines.flow.Flow

class GetAllFundsUseCase(
    private val fundsRepository: FundsRepository
) {
    suspend operator fun invoke(): List<FundModel> {
        return fundsRepository.getAllFunds()
    }

    fun flow(): Flow<List<FundModel>> {
        return fundsRepository.allFunds()
    }

}
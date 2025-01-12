package com.al4apps.lists.domain.usecases

import com.al4apps.lists.data.repositories.FundsRepositoryImpl
import com.al4apps.lists.domain.models.FundModel
import kotlinx.coroutines.flow.Flow

class GetAllFundsUseCase(
    private val fundsRepository: FundsRepositoryImpl
) {
    suspend operator fun invoke(): List<FundModel> {
        return fundsRepository.getAllFunds()
    }

    fun flow(): Flow<List<FundModel>> {
        return fundsRepository.allFunds()
    }

}
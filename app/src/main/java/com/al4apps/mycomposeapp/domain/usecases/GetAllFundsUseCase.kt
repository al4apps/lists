package com.al4apps.mycomposeapp.domain.usecases

import com.al4apps.mycomposeapp.data.repositories.FundsRepositoryImpl
import com.al4apps.mycomposeapp.domain.models.FundModel
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
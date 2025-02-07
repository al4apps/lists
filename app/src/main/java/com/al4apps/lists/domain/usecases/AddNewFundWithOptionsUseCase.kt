package com.al4apps.lists.domain.usecases

import com.al4apps.lists.domain.models.FundModel
import com.al4apps.lists.domain.repositories.FundsRepository

class AddNewFundWithOptionsUseCase(
    private val fundsRepository: FundsRepository
) {

    suspend fun add(fund: FundModel) {
        fundsRepository
    }
}
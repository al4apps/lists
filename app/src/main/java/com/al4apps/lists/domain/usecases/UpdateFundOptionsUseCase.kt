package com.al4apps.lists.domain.usecases

import com.al4apps.lists.domain.models.FundOptionsModel
import com.al4apps.lists.domain.repositories.FundsRepository

class UpdateFundOptionsUseCase(
    private val fundsRepository: FundsRepository
) {
    suspend operator fun invoke(fundOptions: FundOptionsModel) {
        fundsRepository.updateFundOptions(fundOptions)
    }
}
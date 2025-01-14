package com.al4apps.lists.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.al4apps.lists.domain.models.FundModel
import com.al4apps.lists.domain.usecases.AddNewFundUseCase
import com.al4apps.lists.domain.usecases.GetAllFundsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.random.Random
import kotlin.random.nextLong

class HomeViewModel(
    getAllFundsUseCase: GetAllFundsUseCase,
): ViewModel() {

    val models = getAllFundsUseCase.flow()

    private val isTileMode = MutableStateFlow(true)
    val isTileModeFlow = isTileMode.asStateFlow()

    fun switchTileMode() {
        isTileMode.value = !isTileMode.value
    }
}


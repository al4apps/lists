package com.al4apps.lists.presentation.home

import androidx.lifecycle.ViewModel
import com.al4apps.lists.domain.models.ListModel
import com.al4apps.lists.domain.usecases.GetAllFundsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    getAllFundsUseCase: GetAllFundsUseCase,
) : ViewModel() {

    val models: Flow<List<ListModel>> = getAllFundsUseCase.flow()

    private val isTileMode = MutableStateFlow(true)
    val isTileModeFlow = isTileMode.asStateFlow()

    fun switchTileMode() {
        isTileMode.value = !isTileMode.value
    }
}


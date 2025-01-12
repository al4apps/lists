package com.al4apps.lists.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.al4apps.lists.domain.models.FundModel
import com.al4apps.lists.domain.usecases.GetAllFundsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextLong

class HomeViewModel(
    getAllFundsUseCase: GetAllFundsUseCase
): ViewModel() {

    val models = getAllFundsUseCase.flow()

    private val isTileMode = MutableStateFlow(true)
    val isTileModeFlow = isTileMode.asStateFlow()

    private fun generateModels() {
        viewModelScope.launch {
            (1..50).map {
                val model = FundModel(
                    id = it,
                    name = "Сауна $it",
                    toRaise = Random.nextLong(50000L..100000) * 100,
                    raised = Random.nextLong(0L, until = 50000L) * 100,
                    timestamp = System.currentTimeMillis()
                )
            }
        }
    }

    fun switchTileMode() {
        isTileMode.value = !isTileMode.value
    }
}


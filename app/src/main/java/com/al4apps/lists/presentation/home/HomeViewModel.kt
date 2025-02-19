package com.al4apps.lists.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.al4apps.lists.domain.models.ListModel
import com.al4apps.lists.domain.usecases.GetAllFundsUseCase
import com.al4apps.lists.domain.usecases.UpdateFundUseCase
import com.al4apps.lists.presentation.models.ListsFilter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(
    getAllFundsUseCase: GetAllFundsUseCase,
    private val updateFundUseCase: UpdateFundUseCase,
) : ViewModel() {

    private val filter = MutableStateFlow<ListsFilter?>(null)

    @OptIn(FlowPreview::class)
    val models: Flow<List<ListModel>> = combine(
            filter.debounce(DEBOUNCE_TIMEOUT),
            getAllFundsUseCase.flow()
        ) { filter, list ->
            filter?.let {
                list.filter { it.name.contains(filter.query, ignoreCase = true) }
            } ?: list
        }

    private val isTileMode = MutableStateFlow(true)
    val isTileModeFlow = isTileMode.asStateFlow()

    private val isChoiceMode = MutableStateFlow(false)
    val isChoiceModeFlow = isChoiceMode.asStateFlow()

    private val _selectedIds = MutableStateFlow<List<Int>>(emptyList())
    val selectedIds = _selectedIds.asStateFlow()

    fun switchTileMode() {
        isTileMode.value = !isTileMode.value
    }

    fun switchChoiceMode(firstId: Int) {
        _selectedIds.value = listOf(firstId)
        isChoiceMode.value = !isChoiceMode.value
    }

    fun addToChoice(id: Int) {
        if (_selectedIds.value.contains(id)) {
            _selectedIds.value = _selectedIds.value.minus(id)
            if (_selectedIds.value.isEmpty()) isChoiceMode.value = false
        } else _selectedIds.value += listOf(id)
    }

    fun deleteSelectedFunds() {
        viewModelScope.launch {
            try {
                _selectedIds.value.forEach {
                    updateFundUseCase.delete(it)
                }
                _selectedIds.value = emptyList()
                isChoiceMode.value = false
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    fun searchLists(text: String) {
        filter.value = if (text.isBlank()) null
        else ListsFilter(query = text)
    }

    companion object {
        private const val DEBOUNCE_TIMEOUT = 150L
    }
}


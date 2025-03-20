package com.al4apps.lists.presentation.fund

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.al4apps.lists.domain.Constants.NEW_LIST_ID
import com.al4apps.lists.domain.models.EmptyFundItemModel
import com.al4apps.lists.domain.models.FundMemberModel
import com.al4apps.lists.domain.models.FundModel
import com.al4apps.lists.domain.models.ListItemModel
import com.al4apps.lists.domain.usecases.AddNewFundMemberUseCase
import com.al4apps.lists.domain.usecases.AddNewFundUseCase
import com.al4apps.lists.domain.usecases.GetFundMembersUseCase
import com.al4apps.lists.domain.usecases.GetFundUseCase
import com.al4apps.lists.domain.usecases.UpdateFundMemberUseCase
import com.al4apps.lists.domain.usecases.UpdateFundUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

open class FundViewModel(
    private val getFundUseCase: GetFundUseCase,
    private val getFundMembersUseCase: GetFundMembersUseCase,
    private val addNewFundMemberUseCase: AddNewFundMemberUseCase,
    private val updateFundMemberUseCase: UpdateFundMemberUseCase,
    private val updateFundUseCase: UpdateFundUseCase,
    private val addNewFundUseCase: AddNewFundUseCase,
) : ViewModel() {
    private var fundId: Int = NEW_LIST_ID

    private val _listModel = MutableStateFlow<FundModel?>(null)
    val listModel = _listModel.asStateFlow()

    private val _items = MutableStateFlow<List<ListItemModel>>(emptyList())
    val items = _items.asStateFlow()

    fun getFundInfo(id: Int) {
        fundId = id
        viewModelScope.launch {
            try {
                val fund = getFundUseCase.get(id)
                _listModel.value = fund
            } catch (e: Exception) {
                Timber.d(e)
                _listModel.value = null
            }
        }
    }

    fun updateFundModel(fundModel: FundModel) {
        viewModelScope.launch {
            try {
                updateFundUseCase.update(fundModel)
                getFundInfo(fundModel.id)
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    fun updateFundMember(fund: FundMemberModel) {
        viewModelScope.launch {
            try {
                updateFundMemberUseCase.update(fund)
                getFundInfo(fundId)
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    fun getFundItems(fundId: Int) {
        getFundMembersUseCase.flow(fundId)
            .catch { Timber.d(it) }
            .onEach { list ->
                _items.value = list.ifEmpty { listOf(EmptyFundItemModel) }
            }
            .launchIn(viewModelScope)
    }

    fun addNewFundMember(member: FundMemberModel) {
        viewModelScope.launch {
            try {
                addNewFundMemberUseCase.add(member.updateMemberWithFundId(fundId))
                getFundInfo(fundId)
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    fun fetchNewFund(fund: FundModel) {
        viewModelScope.launch {
            try {
                fundId = addNewFundUseCase.invoke(fund)
                getFundInfo(fundId)
                getFundItems(fundId)
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    private fun FundMemberModel.updateMemberWithFundId(newFundId: Int): FundMemberModel {
        return FundMemberModel(
            id = id,
            fundId = newFundId,
            name = name,
            sum = sum,
            comment = comment,
            timestamp = timestamp
        )
    }

    fun removeFund() {
        viewModelScope.launch {
            try {
                updateFundUseCase.delete(fundId)
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    fun addNewFund(fund: FundModel) {
        viewModelScope.launch {
            try {
                addNewFundUseCase.invoke(fund)
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }
}
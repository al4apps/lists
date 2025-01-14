package com.al4apps.lists.presentation.fund

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.al4apps.lists.domain.Constants.NEW_LIST_ID
import com.al4apps.lists.domain.models.EmptyFundItemModel
import com.al4apps.lists.domain.models.EmptyListModel
import com.al4apps.lists.domain.models.FundMemberModel
import com.al4apps.lists.domain.models.FundModel
import com.al4apps.lists.domain.models.ListItemModel
import com.al4apps.lists.domain.models.ListModel
import com.al4apps.lists.domain.usecases.AddNewFundMemberUseCase
import com.al4apps.lists.domain.usecases.AddNewFundUseCase
import com.al4apps.lists.domain.usecases.GetFundMembersUseCase
import com.al4apps.lists.domain.usecases.GetFundUseCase
import com.al4apps.lists.domain.usecases.UpdateFundMemberUseCase
import com.al4apps.lists.domain.usecases.UpdateFundUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

open class FundViewModel(
    private val getFundUseCase: GetFundUseCase,
    private val getFundMembersUseCase: GetFundMembersUseCase,
    private val addNewFundMemberUseCase: AddNewFundMemberUseCase,
    private val updateFundMemberUseCase: UpdateFundMemberUseCase,
    private val updateFundUseCase: UpdateFundUseCase,
    private val addNewFundUseCase: AddNewFundUseCase
) : ViewModel() {
    private var fundId: Int = NEW_LIST_ID
    private val emptyListModel = EmptyListModel()

    private val _listModel = MutableStateFlow<ListModel>(emptyListModel)
    val listModel = _listModel.asStateFlow()

    private val _items = MutableStateFlow<List<ListItemModel>>(emptyList())
    val items = _items.asStateFlow()

    fun getFundInfo(id: Int) {
        fundId = id
        viewModelScope.launch {
            try {
                _listModel.value = getFundUseCase.get(id)
            } catch (e: Exception) {
                Timber.d(e)
                _listModel.value = emptyListModel
            }
        }
    }

    fun updateFundModel(fund: FundModel) {
        viewModelScope.launch {
            try {
                updateFundUseCase.update(fund)
                getFundInfo(fund.id)
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
        viewModelScope.launch {
            try {
                getFundMembersUseCase.flow(fundId).collectLatest { list ->
                    Timber.d("zzz $list")
                    _items.value = list.ifEmpty { listOf(EmptyFundItemModel) }
                }
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
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

    fun fetchNewFund(baseName: String) {
        viewModelScope.launch {
            try {
                val fund = FundModel(
                    id = NEW_LIST_ID,
                    name = baseName,
                    toRaise = null,
                    raised = 0,
                    timestamp = System.currentTimeMillis()
                )
                fundId = addNewFundUseCase.invoke(fund)
                Timber.d("zzz fundId = $fundId")
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
}
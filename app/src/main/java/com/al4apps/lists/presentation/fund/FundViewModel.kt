package com.al4apps.lists.presentation.fund

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.al4apps.lists.domain.Constants.ID_NEW_ITEM
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
import kotlin.random.Random
import kotlin.random.nextLong

open class FundViewModel(
    private val getFundUseCase: GetFundUseCase,
    private val getFundMembersUseCase: GetFundMembersUseCase,
    private val addNewFundMemberUseCase: AddNewFundMemberUseCase,
    private val updateFundMemberUseCase: UpdateFundMemberUseCase,
    private val updateFundUseCase: UpdateFundUseCase,
    private val addNewFundUseCase: AddNewFundUseCase
) : ViewModel() {
    private var fundId: Int = NEW_LIST_ID
    private var newFund: FundModel? = null
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
                getFundItems(id)
            } catch (e: Exception) {
                Timber.d(e)
                _listModel.value = emptyListModel
            }
        }
    }

    fun prepareNewFund(fund: FundModel) {
        newFund = fund
        _items.value = listOf(EmptyFundItemModel)
    }

    fun updateFundModel(fund: FundModel) {
        viewModelScope.launch {
            try {
                updateFundUseCase.update(fund)
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    private fun getFundItems(fundId: Int) {
        viewModelScope.launch {
            try {
                getFundMembersUseCase.flow(fundId).collectLatest { list ->
                    _items.value = list.ifEmpty { list + EmptyFundItemModel }
                }
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    fun addNewFundMember(member: FundMemberModel) {
        viewModelScope.launch {
            if (fundId != NEW_LIST_ID) {
                addNewFundMemberUseCase.add(member.updateMemberWithFundId(fundId))
            } else {
                newFund?.let {
                    fundId = addNewFundUseCase.invoke(it)
                    addNewFundMemberUseCase.add(member.updateMemberWithFundId(fundId))
                    getFundItems(fundId)
                }
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

    fun generateList() {
        Timber.d("Тест $newFund")
        (1..20).forEach {
            val member = FundMemberModel(
                id = ID_NEW_ITEM,
                fundId = fundId,
                name = "Member $it",
                sum = Random.nextLong(100L..10000L) * 100L,
                comment = if (it % 2 == 0) "Comment $it" else null,
                timestamp = System.currentTimeMillis()
            )
            addNewFundMember(member)
        }
    }
}
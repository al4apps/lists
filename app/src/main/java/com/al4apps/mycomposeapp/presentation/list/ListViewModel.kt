package com.al4apps.mycomposeapp.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.al4apps.mycomposeapp.domain.Constants.ID_NEW_ITEM
import com.al4apps.mycomposeapp.domain.models.EmptyFundItemModel
import com.al4apps.mycomposeapp.domain.models.EmptyListModel
import com.al4apps.mycomposeapp.domain.models.FundMemberModel
import com.al4apps.mycomposeapp.domain.models.FundModel
import com.al4apps.mycomposeapp.domain.models.ListItemModel
import com.al4apps.mycomposeapp.domain.models.ListModel
import com.al4apps.mycomposeapp.domain.usecases.AddNewFundMemberUseCase
import com.al4apps.mycomposeapp.domain.usecases.AddNewFundUseCase
import com.al4apps.mycomposeapp.domain.usecases.GetFundMembersUseCase
import com.al4apps.mycomposeapp.domain.usecases.GetFundUseCase
import com.al4apps.mycomposeapp.domain.usecases.UpdateFundMemberUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.random.Random
import kotlin.random.nextLong

open class ListViewModel(
    private val getFundUseCase: GetFundUseCase,
    private val getFundMembersUseCase: GetFundMembersUseCase,
    private val addNewFundMemberUseCase: AddNewFundMemberUseCase,
    private val updateFundMemberUseCase: UpdateFundMemberUseCase,
    private val addNewFundUseCase: AddNewFundUseCase
) : ViewModel() {
    private var fundId: Int = 0

    private val _listModel = MutableStateFlow<ListModel>(EmptyListModel)
    val listModel = _listModel.asStateFlow()

    private val _items = MutableStateFlow<List<ListItemModel>>(listOf(EmptyFundItemModel))
    val items = _items.asStateFlow()

    private val _newFundItemState = MutableStateFlow<FundMemberModel?>(null)
    val newItemState = _newFundItemState.asStateFlow()

    fun getFundInfo(id: Int) {
        fundId = id
        viewModelScope.launch {
            try {
                _listModel.value = getFundUseCase.get(id)
                getFundItems(id)
            } catch (e: Exception) {
                Timber.d(e)
                _listModel.value = EmptyListModel
            }
        }
    }

    fun addNewFund(fund: FundModel) {
        viewModelScope.launch {
            try {
                val newFundId = addNewFundUseCase.invoke(fund)
                getFundInfo(newFundId)
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }

    }

    private fun getFundItems(fundId: Int) {
        viewModelScope.launch {
            try {
                getFundMembersUseCase.flow(fundId).collectLatest { list ->
                    _items.value = list + EmptyFundItemModel
                }
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    fun generateList() {
        viewModelScope.launch {
            (1..20).forEach {
                val member = FundMemberModel(
                    id = ID_NEW_ITEM,
                    fundId = fundId,
                    name = "Member $it",
                    sum = Random.nextLong(100L..10000L) * 100L,
                    comment = if (it % 2 == 0) "Comment $it" else null,
                    timestamp = System.currentTimeMillis()
                )
                addNewFundMemberUseCase.add(member)
            }
        }
    }

    fun saveNewFundItem(item: FundMemberModel) {
        _items.value =
            _items.value.filter { it !is EmptyFundItemModel } + listOf(item, EmptyFundItemModel)
    }
}
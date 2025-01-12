package com.al4apps.lists.presentation.fund

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.al4apps.lists.R
import com.al4apps.lists.domain.Constants.NEW_LIST_ID
import com.al4apps.lists.domain.models.EmptyFundItemModel
import com.al4apps.lists.domain.models.EmptyListModel
import com.al4apps.lists.domain.models.FundMemberModel
import com.al4apps.lists.domain.models.FundModel
import com.al4apps.lists.domain.models.ListModel
import com.al4apps.lists.presentation.home.AddItemFab
import com.al4apps.lists.presentation.home.RaisingText
import com.al4apps.lists.presentation.home.SumRaisedText
import com.al4apps.lists.presentation.home.SumToRaiseText
import com.al4apps.lists.presentation.home.raisedFraction
import com.al4apps.lists.ui.theme.fractionColor
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundScreen(
    navController: NavController, listId: Int, viewModel: FundViewModel = koinViewModel()
) {
    if (listId == NEW_LIST_ID) PrepareToNewFund(viewModel)
    else viewModel.getFundInfo(listId)

    val list = viewModel.listModel.collectAsState()
    val fund: FundModel? = if (list.value is FundModel) list.value as FundModel
    else null
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showAddMemberDialog by remember { mutableStateOf(false) }

    fund?.let { fundModel ->
        FundSettingsDialog(
            fundModel,
            showSettingsDialog,
            onDismiss = { showSettingsDialog = false },
            onConfirm = { updatedFund ->
                viewModel.updateFundModel(updatedFund)
                showSettingsDialog = false
            })
    }
    AddMemberDialog(
        showDialog = showAddMemberDialog,
        onAddClick = {
            viewModel.addNewFundMember(it)
            showAddMemberDialog = false
        },
        onDismiss = { showAddMemberDialog = false }
    )

    // Box для создания полупрозрачного слоя
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            floatingActionButton = {
                AddItemFab(modifier = Modifier.padding(bottom = 26.dp)) {
                    showAddMemberDialog = true
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            topBar = {
                val title = if (list.value is EmptyListModel) {
                    stringResource((list.value as EmptyListModel).nameStr)
                } else list.value.listName
                ListTopBar(
                    title = title,
                    navController = navController,
                    onSettingsClick = { showSettingsDialog = true })
            },
            content = { paddingValues ->

                FundContent(
                    paddingValues, viewModel, list.value
                )
            })

        if (showSettingsDialog || showAddMemberDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Gray.copy(alpha = 0.5f))
            ) { }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundContent(
    paddingValues: PaddingValues,
    viewModel: FundViewModel,
    fund: ListModel,
) {
    val listItems = viewModel.items.collectAsState()

    // content Box
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // List Column
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(listItems.value) { model ->
                ElevatedCard(
                    onClick = { }, modifier = Modifier.padding(vertical = 4.dp)

                ) {
                    val nextPosition = listItems.value.indexOf(model) + 1
                    when (model) {
                        is FundMemberModel -> FundItemLayout(
                            model, nextPosition
                        )

                        is EmptyFundItemModel -> {
                            EmptyFundItemLayout(nextPosition) {
                                viewModel.addNewFundMember(it)
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Info Row
        if (fund is FundModel) FundState(fund)
    }
}

@Composable
fun AddMemberDialog(
    showDialog: Boolean,
    onAddClick: (member: FundMemberModel) -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 8.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(R.string.add_new_member_dialog_title))
                    Spacer(modifier = Modifier.height(8.dp))
                    EmptyFundItemLayout { newMember ->
                        onAddClick(newMember)
                    }
                }
            }
        }
    }
}

@Composable
fun FundSettingsDialog(
    fund: FundModel,
    showDialog: Boolean,
    onConfirm: (fund: FundModel) -> Unit,
    onDismiss: () -> Unit,
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            var name by remember { mutableStateOf(fund.name) }
            var sum by remember { mutableStateOf(fund.toRaise?.toMoneyInTextFieldString() ?: "") }
            var isFormValid by remember {
                mutableStateOf(name != fund.name || !sum.isEqualSum(fund.toRaise))
            }
            ElevatedCard(modifier = Modifier.wrapContentSize()) {
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(text = stringResource(R.string.new_fund_name_field_label))
                    SimpleTextField(
                        name,
                        hint = stringResource(R.string.new_fund_name_field_hint),
                        modifier = Modifier
                            .widthIn(min = 80.dp)
                            .wrapContentWidth()
                    ) {
                        name = it
                        isFormValid = name != fund.name || !sum.isEqualSum(fund.toRaise)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(stringResource(R.string.fund_settings_need_to_raise_text))
                    SimpleTextField(
                        text = sum, hint = "0", keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Decimal
                        )
                    ) { value ->
                        sum = filterSumValue(value)
                        isFormValid = name != fund.name || !sum.isEqualSum(fund.toRaise)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(
                            onClick = {
                                val fundModel = FundModel(
                                    id = fund.id,
                                    name = name,
                                    toRaise = if (sum.isBlank() || sum.toCents() == 0L) null
                                    else sum.toCents(),
                                    raised = fund.raised,
                                    timestamp = System.currentTimeMillis()
                                )
                                onConfirm(fundModel)
                            },
                            enabled = isFormValid
                        ) {
                            Text(stringResource(R.string.settings_dialog_save_button_text))
                        }
                        Button(onClick = { onDismiss() }) {
                            Text(stringResource(R.string.settings_dialog_cancel_button_text))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTopBar(
//    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    navController: NavController,
    onSettingsClick: () -> Unit
) {
    TopAppBar(title = {
        Text(title)
    },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = stringResource(R.string.back_button_description),
                )
            }
        }, actions = {
            IconButton(onClick = { onSettingsClick() }) {
                Icon(
                    painter = painterResource(R.drawable.settings),
                    contentDescription = stringResource(R.string.settings_button_content_description)
                )
            }
        }
    )
}

@Composable
private fun PrepareToNewFund(viewModel: FundViewModel) {
    val fund = FundModel(
        id = NEW_LIST_ID,
        name = stringResource(R.string.new_list_base_name),
        toRaise = null,
        raised = 0,
        timestamp = System.currentTimeMillis()
    )
    viewModel.prepareNewFund(fund)
}

@Composable
fun FundState(fund: FundModel) {
    // Box for the background
    Box(
        modifier = Modifier
            .height(32.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .border(1.dp, fractionColor), contentAlignment = Alignment.CenterStart
    ) {
        // Box for the raised part
        Box(
            modifier = Modifier
                .fillMaxWidth(fund.raisedFraction())
                .background(color = fractionColor)
                .fillMaxHeight(),
        )

        // Box for the content
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            SumRaisedText(fund.raised)
            fund.toRaise?.let { toRaise ->
                RaisingText(stringResource(R.string.fund_item_to_raise_text))
                SumToRaiseText(toRaise, modifier = Modifier.padding(end = 8.dp))
            }
        }
    }
}

fun String.isEqualSum(sum: Long?): Boolean {
    return when {
        this.isBlank() && sum == null -> true
        sum != null && this.isNotBlank() -> this.toCents() == sum
        else -> false
    }
}
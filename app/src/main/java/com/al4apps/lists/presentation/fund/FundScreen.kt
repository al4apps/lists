package com.al4apps.lists.presentation.fund

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.al4apps.lists.R
import com.al4apps.lists.domain.Constants.NEW_LIST_ID
import com.al4apps.lists.domain.models.EmptyFundItemModel
import com.al4apps.lists.domain.models.FundMemberModel
import com.al4apps.lists.domain.models.FundModel
import com.al4apps.lists.domain.models.FundOptionsModel
import com.al4apps.lists.presentation.home.AddItemFab
import com.al4apps.lists.presentation.home.RaisingText
import com.al4apps.lists.presentation.home.SumRaisedText
import com.al4apps.lists.presentation.home.SumToRaiseText
import com.al4apps.lists.presentation.home.raisedFraction
import com.al4apps.lists.presentation.models.FundUi
import com.al4apps.lists.ui.theme.Typography
import com.al4apps.lists.ui.theme.fractionColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun FundScreen(
    navController: NavController, listId: Int, viewModel: FundViewModel = koinViewModel()
) {
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
    var showEditMemberDialog by rememberSaveable { mutableStateOf(false) }
    var isCreatingNewFund by rememberSaveable { mutableStateOf(false) }
    var fadeBackground by rememberSaveable { mutableStateOf(false) }

    val list = viewModel.listModel.collectAsState()

    LaunchedEffect(Unit) {
        if (listId.isNewList()) {
            isCreatingNewFund = true
            showSettingsDialog = true
        } else {
            viewModel.getFundInfo(listId)
            viewModel.getFundItems(listId)
        }
    }

    if (list.value != null || isCreatingNewFund) {
        FundSettingsDialog(
            fundUi = list.value ?: generateEmptyFundUi(),
            showDialog = showSettingsDialog,
            isCreating = isCreatingNewFund,
            onDismiss = {
                if (isCreatingNewFund) {
                    navController.popBackStack()
                }
                showSettingsDialog = false
                isCreatingNewFund = false
            },
            onConfirm = { fundUi ->
                if (isCreatingNewFund) viewModel.fetchNewFund(fundUi)
                else viewModel.updateFundModel(fundUi)
                showSettingsDialog = false
                isCreatingNewFund = false
            }
        )
    }

    EditMemberDialog(
        showDialog = showEditMemberDialog,
        onSaveClick = {
            viewModel.addNewFundMember(it)
            showEditMemberDialog = false
        },
        onDismiss = { showEditMemberDialog = false }
    )

    // Box для создания полупрозрачного слоя
    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(modifier = Modifier
            .fillMaxSize()
            .imePadding(), floatingActionButton = {
            AddItemFab(modifier = Modifier.padding(bottom = 26.dp)) {
                showEditMemberDialog = true
            }
        }, floatingActionButtonPosition = FabPosition.End, topBar = {
            val title = list.value?.fundModel?.name
                ?: if (isCreatingNewFund) stringResource(R.string.new_list_base_name)
                else ""
            ListTopBar(title = title,
                navController = navController,
                onSettingsClick = { showSettingsDialog = true })
        }, content = { paddingValues ->
            list.value?.let { fundUi ->
                FundContent(paddingValues, viewModel, fundUi) {
                    fadeBackground = it
                }
            }
        })

        if (showSettingsDialog || showEditMemberDialog || fadeBackground) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Gray.copy(alpha = 0.5f))
            ) { }
        }
    }
}

private fun Int.isNewList(): Boolean = this == NEW_LIST_ID

@Composable
fun FundContent(
    paddingValues: PaddingValues,
    viewModel: FundViewModel,
    fund: FundUi,
    onItemEdit: (showDialog: Boolean) -> Unit
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
                var showEditMemberDialog by remember { mutableStateOf(false) }

                ElevatedCard(
                    onClick = {
                        showEditMemberDialog = true
                        onItemEdit(true)
                    },
                    modifier = Modifier.padding(vertical = 4.dp)

                ) {
                    val nextPosition = listItems.value.indexOf(model) + 1
                    when (model) {
                        is FundMemberModel -> {
                            FundItemLayout(
                                model, nextPosition
                            )

                            EditMemberDialog(
                                member = model,
                                showDialog = showEditMemberDialog,
                                onSaveClick = {
                                    viewModel.updateFundMember(it)
                                    showEditMemberDialog = false
                                    onItemEdit(false)
                                },
                                onDismiss = {
                                    showEditMemberDialog = false
                                    onItemEdit(false)
                                },
                            )
                        }

                        is EmptyFundItemModel -> {
                            EditFundItemLayout(nextPosition,
                                onSaveClick = { viewModel.addNewFundMember(it) },
                                onCancelClick = {})
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Info Row
        FundState(fund.fundModel)
    }
}

@Composable
fun EditMemberDialog(
    member: FundMemberModel? = null,
    showDialog: Boolean,
    onSaveClick: (member: FundMemberModel) -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                if (member == null) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 8.dp)
                            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(R.string.add_new_member_dialog_title),
                            style = Typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        EditFundItemLayout(
                            onSaveClick = { newMember ->
                                onSaveClick(newMember)
                            }, onCancelClick = onDismiss
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 8.dp)
                            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.edit_member_dialog_title),
                            style = Typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        EditFundItemLayout(
                            member = member, onSaveClick = { newMember ->
                                onSaveClick(newMember)
                            }, onCancelClick = onDismiss
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FundSettingsDialog(
    fundUi: FundUi,
    showDialog: Boolean,
    isCreating: Boolean = false,
    onConfirm: (fundUi: FundUi) -> Unit,
    onDismiss: () -> Unit,
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnClickOutside = !isCreating
            )
        ) {
            val fund = fundUi.fundModel
            val fundOptions = fundUi.fundOptions
            val focusRequester = remember { FocusRequester() }
            var name by remember { mutableStateOf(fund.name) }
            var sum by remember {
                mutableStateOf(fund.toRaise?.toMoneyInTextFieldString() ?: "")
            }
            val membersCount by remember { mutableIntStateOf(fundOptions.membersCount) }
            val needToDivide by remember { mutableStateOf(fundOptions.needToDivide) }
            var isFormValid by remember {
                mutableStateOf(
                    (isCreating && name.isNotBlank()) || name != fund.name
                            || !sum.isEqualSum(fund.toRaise)
                )
            }

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            ElevatedCard(modifier = Modifier.wrapContentSize()) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isCreating) {
                        Text(
                            stringResource(R.string.dialog_new_fund_title),
                            style = Typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Column(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = stringResource(R.string.new_fund_name_field_label),
                            style = Typography.bodyMedium
                        )
                        AddSpaceVer(4)
                        SimpleTextField(
                            name,
                            hint = stringResource(R.string.new_fund_name_field_hint),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                        ) {
                            name = it
                            isFormValid =
                                (isCreating && name.isNotBlank()) || name != fund.name
                                        || !sum.isEqualSum(fund.toRaise)
                        }
                        AddSpaceVer(16)
                        Text(
                            text = stringResource(R.string.fund_settings_need_to_raise_text),
                            style = Typography.bodyMedium
                        )
                        AddSpaceVer(4)
                        SimpleTextField(
                            text = sum, hint = "0", keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Decimal
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) { value ->
                            sum = filterSumValue(value)
                            isFormValid =
                                (isCreating && name.isNotBlank()) || name != fund.name
                                        || !sum.isEqualSum(fund.toRaise)
                        }
                        AddSpaceVer(24)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Button(onClick = { onDismiss() }) {
                                Text(stringResource(R.string.dialog_cancel_button_text))
                            }
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
                                    val fundOptionsModel = FundOptionsModel(
                                        fundId = fund.id,
                                        membersCount = membersCount,
                                        raisedSum = fund.raised,
                                        needToDivide = needToDivide
                                    )
                                    onConfirm(FundUi(fundModel, fundOptionsModel))
                                }, enabled = isFormValid
                            ) {
                                Text(stringResource(R.string.dialog_save_button_text))
                            }
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
    title: String, navController: NavController, onSettingsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(title, style = Typography.titleLarge, fontWeight = FontWeight.Medium)
            }
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
        })
}

@Composable
fun FundState(fund: FundModel) {
    var showSumToCollect by remember { mutableStateOf(false) }
    // Box for the background
    Box(modifier = Modifier
        .height(32.dp)
        .fillMaxWidth()
        .clickable { showSumToCollect = !showSumToCollect }
        .padding(horizontal = 8.dp)
        .border(1.dp, fractionColor), contentAlignment = Alignment.CenterStart) {
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
                if (showSumToCollect) {
                    RaisingText(stringResource(R.string.fund_item_sum_to_collect_text))
                    SumToRaiseText(
                        maxOf(0L, toRaise - fund.raised), modifier = Modifier.padding(end = 8.dp)
                    )
                } else {
                    RaisingText(stringResource(R.string.fund_item_to_raise_text))
                    SumToRaiseText(toRaise, modifier = Modifier.padding(end = 8.dp))
                }
            }
        }
    }
}

@Composable
fun generateEmptyFundUi() = FundUi(
    FundModel(
        id = NEW_LIST_ID,
        name = stringResource(R.string.new_list_base_name),
        toRaise = null,
        raised = 0L,
        timestamp = System.currentTimeMillis()
    ),
    FundOptionsModel(
        fundId = NEW_LIST_ID,
        membersCount = 0,
        raisedSum = 0,
        needToDivide = false
    )
)

fun String.isEqualSum(sum: Long?): Boolean {
    return when {
        this.isBlank() && sum == null -> true
        sum != null && this.isNotBlank() -> this.toCents() == sum
        else -> false
    }
}
package com.al4apps.mycomposeapp.presentation.list

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.al4apps.mycomposeapp.R
import com.al4apps.mycomposeapp.domain.Constants.NEW_LIST_ID
import com.al4apps.mycomposeapp.domain.models.EmptyFundItemModel
import com.al4apps.mycomposeapp.domain.models.FundMemberModel
import com.al4apps.mycomposeapp.domain.models.FundModel
import com.al4apps.mycomposeapp.presentation.home.AddItemFab
import com.al4apps.mycomposeapp.presentation.items.EmptyFundItemLayout
import com.al4apps.mycomposeapp.presentation.items.FundItemLayout
import com.al4apps.mycomposeapp.presentation.items.FundRowLayout
import com.al4apps.mycomposeapp.presentation.items.fractionColor
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    navController: NavController,
    listId: Int,
    viewModel: ListViewModel = koinViewModel()
) {
    if (listId == NEW_LIST_ID) viewModel.addNewFund(generateNewFund())
    else viewModel.getFundInfo(listId)

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val fund = viewModel.listModel.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        floatingActionButton = {
            AddItemFab {
                viewModel.generateList()
            }
        },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.End,
        topBar = { ListTopBar(scrollBehavior, fund.value.listName, navController) }
    ) { paddingValues ->
        if (fund.value is FundModel) {
            FundContent(
                paddingValues,
                scrollBehavior,
                viewModel,
                (fund.value as FundModel)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundContent(
    paddingValues: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    viewModel: ListViewModel,
    fund: FundModel
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
                .padding(horizontal = 8.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(listItems.value) { model ->
                ElevatedCard(
                    onClick = { },
                    modifier = Modifier.padding(vertical = 4.dp)

                ) {
                    val nextPosition = listItems.value.indexOf(model) + 1
                    when (model) {
                        is FundMemberModel -> FundItemLayout(
                            model,
                            nextPosition
                        )

                        is EmptyFundItemModel -> {
                            EmptyFundItemLayout(nextPosition, fund.id) {
                                viewModel.saveNewFundItem(it)
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Info Row
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .border(1.dp, fractionColor)
        ) {
            FundRowLayout(fund, false)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    navController: NavController
) {
    TopAppBar(
        title = {
            Text(title)
        },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = stringResource(R.string.back_button_description),
                )
            }
        },
    )
}

@Composable
private fun generateNewFund(): FundModel {
    return FundModel(
        id = NEW_LIST_ID,
        name = stringResource(R.string.new_list_base_name),
        toRaise = null,
        raised = 0,
        timestamp = System.currentTimeMillis()
    )
}
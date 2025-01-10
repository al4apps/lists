package com.al4apps.mycomposeapp.presentation.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.al4apps.mycomposeapp.R
import com.al4apps.mycomposeapp.domain.Constants.NEW_LIST_ID
import com.al4apps.mycomposeapp.domain.models.FundModel
import com.al4apps.mycomposeapp.navigation.AppScreens
import com.al4apps.mycomposeapp.presentation.items.FundRowLayout
import com.al4apps.mycomposeapp.presentation.items.FundTileLayout
import com.al4apps.mycomposeapp.presentation.items.fractionColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = koinViewModel()) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = { AddItemFab {
            navController.navigate(AppScreens.List.withArgs(NEW_LIST_ID))
        } },
        floatingActionButtonPosition = FabPosition.End,
        content = { paddingValues ->
            HomeContent(paddingValues, navController, viewModel)
        }
    )
}

@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: HomeViewModel
) {
    val models = viewModel.models.collectAsState(initial = emptyList())
    val isTileMode = viewModel.isTileModeFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchField(Modifier.weight(1f)) {}
            SwitcherIcon(isTileMode.value) { viewModel.switchTileMode() }
        }
        ListOfLists(models, isTileMode.value) {
            navController.navigate(AppScreens.List.withArgs(it))
        }
    }
}

@Composable
private fun ListOfLists(
    list: State<List<FundModel>>,
    isTileMode: Boolean,
    onClick: (id: Int) -> Unit
) {
    if (isTileMode) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
        ) {
            items(list.value) { fundModel ->
                ElevatedCard(
                    modifier = Modifier
                        .padding(8.dp),
                    onClick = {
                        onClick(fundModel.id)
                    }) {
                    FundTileLayout(fundModel)
                }
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
        ) {
            items(list.value) { fundModel ->
                ElevatedCard(
                    modifier = Modifier
                        .padding(8.dp)
                        .border(1.dp, fractionColor),
                    onClick = { onClick(fundModel.id) }
                ) {
                    FundRowLayout(fundModel)
                }
            }
        }
    }
}

@Composable
fun AddItemFab(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.plus),
            contentDescription = null
        )
    }
}

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    onQueryChanged: (String) -> Unit
) {
    var textState by remember {
        mutableStateOf("")
    }
    val hint = stringResource(id = R.string.home_search_field_hint)
    TextField(
        value = textState,
        onValueChange = { text ->
            textState = text
            onQueryChanged(text)
        },
        placeholder = {
            Text(hint)
        },
        shape = RoundedCornerShape(10.dp),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = null,
                tint = Color.Gray
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .padding(vertical = 0.dp),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun SwitcherIcon(
    isTileMode: Boolean,
    onIconClick: () -> Unit,
) {
    val painterId = if (isTileMode) R.drawable.rows
    else R.drawable.tile
    Icon(
        painter = painterResource(painterId),
        contentDescription = null,
        modifier = Modifier
            .wrapContentWidth()
            .padding(start = 12.dp)
            .clickable {
                onIconClick()
            }
    )
}

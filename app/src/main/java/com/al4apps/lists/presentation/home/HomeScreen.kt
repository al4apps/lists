package com.al4apps.lists.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.al4apps.lists.R
import com.al4apps.lists.domain.Constants.NEW_LIST_ID
import com.al4apps.lists.domain.models.FundModel
import com.al4apps.lists.domain.models.ListModel
import com.al4apps.lists.navigation.AppScreens
import com.al4apps.lists.presentation.fund.AddSpaceVer
import com.al4apps.lists.ui.theme.Typography
import com.al4apps.lists.ui.theme.buttonHeight
import com.al4apps.lists.ui.theme.searchHintStyle
import com.al4apps.lists.ui.theme.textFieldHeight
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = koinViewModel()) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            AddItemFab(modifier = Modifier.padding(bottom = 24.dp)) {
                navController.navigate(AppScreens.List.withArgs(NEW_LIST_ID))
            }
        },
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
    val isChoiceMode = viewModel.isChoiceModeFlow.collectAsState()
    val selectedIds = viewModel.selectedIds.collectAsState()
    val showDeletionDialog = rememberSaveable {
        mutableStateOf(false)
    }

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
            SearchField(Modifier.weight(0.9f)) { text ->
                viewModel.searchLists(text)
            }
            SwitcherIcon(isTileMode.value, isChoiceMode.value, Modifier.weight(0.1f)) {
                if (!isChoiceMode.value) viewModel.switchTileMode()
                else showDeletionDialog.value = true
            }
        }
        ListOfLists(
            list = models.value,
            isTileMode = isTileMode.value,
            isChoiceMode = isChoiceMode.value,
            selectedIds = selectedIds.value,
            onClick = { id ->
                if (!isChoiceMode.value) navController.navigate(AppScreens.List.withArgs(id))
                else viewModel.addToChoice(id)
            },
            onLongPress = { id ->
                if (!isChoiceMode.value) viewModel.switchChoiceMode(id)
            }
        )

        DeleteConfirmationDialog(
            showDialog = showDeletionDialog.value,
            onDismiss = { showDeletionDialog.value = false },
            onDeleteClick = {
                viewModel.deleteSelectedFunds()
                showDeletionDialog.value = false
            }
        )
    }
}

@Composable
private fun ListOfLists(
    list: List<ListModel>,
    isTileMode: Boolean,
    isChoiceMode: Boolean,
    selectedIds: List<Int>,
    onClick: (id: Int) -> Unit,
    onLongPress: (id: Int) -> Unit,
) {
    if (isTileMode) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
        ) {
            items(list) { model ->
                if (model is FundModel) {
                    val isSelected = isChoiceMode && selectedIds.contains(model.id)
                    ElevatedCard(
                        modifier = Modifier
                            .padding(8.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = { onClick(model.id) },
                                    onLongPress = { onLongPress(model.id) }
                                )
                            }) {
                        // Box для визуальной обработки onLongPress
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.background(
                                color = if (isSelected) Color.LightGray
                                else Color.Unspecified
                            )
                        ) {
                            FundTileLayout(model)
                        }
                    }
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
            items(list) { model ->
                if (model is FundModel) {
                    val isSelected = isChoiceMode && selectedIds.contains(model.id)

                    ElevatedCard(
                        modifier = Modifier
                            .padding(8.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = { onClick(model.id) },
                                    onLongPress = { onLongPress(model.id) }
                                )
                            },
                    ) {
                        // Box для визуальной обработки onLongPress
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.background(
                                color = if (isSelected) Color.LightGray
                                else Color.Unspecified
                            )
                        ) {
                            FundRowLayout(model)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddItemFab(modifier: Modifier = Modifier, onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.plus),
            contentDescription = null
        )
    }
}

@Composable
fun DeleteConfirmationDialog(
    showDialog: Boolean,
    onDeleteClick: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(R.string.home_confirm_deletion_dialog_text),
                        style = Typography.titleMedium
                    )
                    AddSpaceVer(24)
                    Row {
                        OutlinedButton(
                            modifier = Modifier
                                .weight(1f)
                                .height(buttonHeight),
                            onClick = { onDismiss() }
                        ) {
                            Text(stringResource(R.string.dialog_cancel_button_text))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { onDeleteClick() },
                            Modifier
                                .weight(1f)
                                .height(buttonHeight)
                        ) {
                            Text(stringResource(R.string.dialog_delete_button_text))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    onQueryChanged: (String) -> Unit
) {
    var textState by rememberSaveable {
        mutableStateOf("")
    }
    val hint = stringResource(id = R.string.home_search_field_hint)
    BasicTextField(
        value = textState,
        onValueChange = { input ->
            textState = input
            onQueryChanged(input)
        },
        modifier = modifier.height(textFieldHeight),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrectEnabled = true,
            showKeyboardOnFocus = true,
            capitalization = KeyboardCapitalization.Words
        ),
        textStyle = Typography.bodyMedium,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .background(Color.Transparent, RoundedCornerShape(20.dp))
                    .border(2.dp, Color.Gray, RoundedCornerShape(20.dp))
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (textState.isEmpty()) {
                            Text(hint, style = searchHintStyle)
                        }
                        innerTextField()
                    }
                }
            }
        },
    )
//    TextField(
//        value = textState,
//        onValueChange = { text ->
//            textState = text
//            onQueryChanged(text)
//        },
//        placeholder = {
//            Text(hint)
//        },
//        shape = RoundedCornerShape(10.dp),
//        leadingIcon = {
//            Icon(
//                painter = painterResource(id = R.drawable.search),
//                contentDescription = null,
//                tint = Color.Gray
//            )
//        },
//        modifier = modifier
//            .fillMaxWidth()
//            .height(54.dp)
//            .padding(vertical = 0.dp),
//        singleLine = true,
//        colors = TextFieldDefaults.colors(
//            focusedIndicatorColor = Color.Transparent,
//            unfocusedIndicatorColor = Color.Transparent,
//            disabledIndicatorColor = Color.Transparent,
//            errorIndicatorColor = Color.Transparent
//        )
//    )
}

@Composable
fun SwitcherIcon(
    isTileMode: Boolean,
    isChoiceMode: Boolean,
    modifier: Modifier = Modifier,
    onIconClick: () -> Unit,
) {
    val painterId = if (isChoiceMode) R.drawable.trash
    else {
        if (isTileMode) R.drawable.rows
        else R.drawable.tile
    }
    Icon(
        painter = painterResource(painterId),
        contentDescription = null,
        tint = Color.DarkGray,
        modifier = modifier
            .fillMaxWidth()
            .size(24.dp)
            .padding(start = 8.dp)
            .clickable {
                onIconClick()
            }
    )
}

package com.al4apps.lists.presentation.fund

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.al4apps.lists.R
import com.al4apps.lists.domain.Constants
import com.al4apps.lists.domain.models.FundMemberModel
import com.al4apps.lists.ui.theme.Typography
import com.al4apps.lists.ui.theme.buttonHeight
import com.al4apps.lists.ui.theme.greenText
import com.al4apps.lists.ui.theme.textFieldHeight

@Composable
fun FundItemLayout(item: FundMemberModel, position: Int) {
    val sum = remember { mutableLongStateOf(item.sum) }
    // Main row
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(vertical = 8.dp)
            .height(IntrinsicSize.Max)
    ) {
        // Position number box
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            Text("$position.", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
        // Name and comment box
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 6.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Column {
                Text(
                    item.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                if (item.comment != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        item.comment.toString(),
                        fontSize = 11.sp,
                        color = Color.DarkGray,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
        // Sum box
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = 4.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            val color = if (sum.longValue > 0) greenText else Color.Red
            Text(
                item.sum.toMoneyString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = color
            )
        }
        Box(
            modifier = Modifier
                .width(20.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.dots_ver),
                contentDescription = "menu"
            )
        }
    }
}

@Composable
fun EditFundItemLayout(
    position: Int? = null,
    member: FundMemberModel? = null,
    onSaveClick: (item: FundMemberModel) -> Unit,
    onCancelClick: () -> Unit
) {
    var nameText by remember { mutableStateOf(member?.name ?: "") }
    var commentText by remember { mutableStateOf(member?.comment ?: "") }
    var sumText by remember {
        mutableStateOf(member?.sum?.toMoneyInTextFieldString() ?: "")
    }
    val nameHint = stringResource(id = R.string.new_fund_item_name_field_hint)
    val commentHint = stringResource(id = R.string.new_fund_item_comment_field_hint)
    val sumHint = stringResource(id = R.string.new_fund_item_sum_field_hint)
    var isFormValid by remember {
        mutableStateOf(nameText.isNotBlank() && sumText.isNotBlank())
    }
    // Main row
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(vertical = 8.dp)
            .height(intrinsicSize = IntrinsicSize.Min),
    ) {
        // Position number box
        position?.let {
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .fillMaxHeight()
                    .padding(top = 8.dp, end = 6.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    "$position.",
                    style = Typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        // Name, comment, sum, button column
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row {
                Column(modifier = Modifier.weight(1f)) {
                    SimpleTextField(
                        nameText,
                        nameHint,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        nameText = it
                        isFormValid = nameText.isNotBlank() && sumText.isNotBlank()
                    }
                }
                // Sum
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 4.dp)
                ) {
                    SimpleTextField(
                        sumText,
                        sumHint,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    ) { newValue ->
                        sumText = filterSumValue(newValue)
                        isFormValid = sumText.isNotBlank() && nameText.isNotBlank()
                    }

                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                SimpleTextField(
                    commentText,
                    commentHint,
                    modifier = Modifier
                        .fillMaxWidth()
                ) { commentText = it }

            }
            AddSpaceVer(24)
            if (member != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedButton(
                        onClick = { onCancelClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .height(buttonHeight)
                    ) {
                        Text(stringResource(R.string.dialog_cancel_button_text))
                    }
                    AddSpaceHor(16)
                    Button(
                        onClick = {
                            onSaveClick(
                                FundMemberModel(
                                    id = member.id,
                                    fundId = member.fundId,
                                    name = nameText,
                                    sum = sumText.toCents(),
                                    comment = commentText.ifBlank { null },
                                    System.currentTimeMillis()
                                )
                            )
                        },
                        enabled = isFormValid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .height(buttonHeight)
                    ) {
                        Text(stringResource(R.string.dialog_save_button_text))
                    }
                }
            } else {
                Button(
                    onClick = {
                        onSaveClick(
                            FundMemberModel(
                                id = Constants.ID_NEW_ITEM,
                                fundId = Constants.NEW_LIST_ID,
                                name = nameText,
                                sum = sumText.toCents(),
                                comment = commentText.ifBlank { null },
                                System.currentTimeMillis()
                            )
                        )
                    },
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(buttonHeight)
                ) {
                    Text(stringResource(R.string.add_new_item_button_text))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun EmptyFundItemModelPreview() {
    EditFundItemLayout(
        member = FundMemberModel(0, 0, "Test", 50000L, comment = "", 11561313L),
        onSaveClick = {},
        onCancelClick = {}
    )
}

fun filterSumValue(newValue: String): String {
    var dotsCount = 0
    var numAfterDot = 0
    val filteredValue = newValue
        .replace('.', ',')
        .filterIndexed { index, char ->
            if (char == ',') {
                dotsCount += 1
            } else if (dotsCount > 0) {
                numAfterDot += 1
            }
            (char.isDigit() || (char == ',' && dotsCount < 2 && index > 0)) && numAfterDot < 3
        }
    return filteredValue
}

@Composable
fun SimpleTextField(
    text: String,
    hint: String,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = Typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = text,
        onValueChange = { onValueChange(it) },
        modifier = modifier.height(textFieldHeight),
        singleLine = singleLine,
        keyboardOptions = keyboardOptions.copy(
            autoCorrectEnabled = true,
            showKeyboardOnFocus = true,
            capitalization = KeyboardCapitalization.Words
        ),
        textStyle = textStyle,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .background(Color.Transparent, RoundedCornerShape(20.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (text.isEmpty()) {
                    Text(hint, style = textStyle, color = Color.Gray)
                }
                innerTextField()
            }
        },
    )
}

@Composable
fun AddSpaceVer(dp: Int, modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.height(dp.dp))
}

@Composable
fun AddSpaceHor(dp: Int, modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.width(dp.dp))
}

fun String.toCents(): Long {
    require(value = this.isNotBlank())
    return if (this.contains(',')) {
        this.filter { it != ',' }.toLong()
    } else this.toLong() * 100
}

fun Long.toMoneyInTextFieldString(): String {
    val cents = (this % 100)
    val number = (this / 100)
    return "$number${if (cents != 0L) ",$cents" else ""}"
}

@SuppressLint("DefaultLocale")
fun Long.toMoneyString(addPlus: Boolean = true): String {
    val symbol = if (this > 0L && addPlus) "+" else ""
    val cents = (this % 100)
    val number = (this / 100)
    val main = String.format("%,d", number).replace(',', ' ')
    return "$symbol$main${if (cents != 0L) ",$cents" else ""}"
}
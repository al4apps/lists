package com.al4apps.mycomposeapp.presentation.items

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.al4apps.mycomposeapp.R
import com.al4apps.mycomposeapp.domain.models.FundMemberModel

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
fun EmptyFundItemLayout(position: Int, fundId: Int, onAddClick: (item: FundMemberModel) -> Unit) {
    var nameText by remember { mutableStateOf("") }
    var commentText by remember { mutableStateOf("") }
    var sumText by remember { mutableStateOf("") }
    val nameHint = stringResource(id = R.string.new_fund_item_name_field_hint)
    val commentHint = stringResource(id = R.string.new_fund_item_comment_field_hint)
    val sumHint = stringResource(id = R.string.new_fund_item_sum_field_hint)
    var isFormValid by remember {
        mutableStateOf(nameText.isNotBlank() && sumText.isNotBlank())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 6.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                SimpleTextField(
                    nameText,
                    nameHint,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    nameText = it
                    isFormValid = nameText.isNotBlank() && sumText.isNotBlank()
                }
                Spacer(modifier = Modifier.height(4.dp))
                SimpleTextField(
                    commentText,
                    commentHint,
                    modifier = Modifier.fillMaxWidth()
                ) { commentText = it }
            }
            // Sum box
            Column(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(end = 4.dp, start = 4.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                SimpleTextField(
                    sumText,
                    sumHint,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                ) { newValue ->
                    var dotsCount = 0
                    val filteredValue = newValue.filterIndexed { index, char ->
                        if (char == '.') dotsCount += 1
                        char.isDigit() || (char == '.' && dotsCount < 2 && index > 0)
                    }
                    sumText = filteredValue
                    isFormValid = sumText.isNotBlank() && nameText.isNotBlank()
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Button(
            onClick = {
                onAddClick(
                    FundMemberModel(
                        id = 0,
                        fundId = fundId,
                        name = nameText,
                        sum = sumText.toLong(),
                        comment = commentText,
                        System.currentTimeMillis()
                    )
                )
            },
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text(stringResource(id = R.string.add_new_item_button_text))
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun SimpleTextField(
    text: String,
    hint: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = { onValueChange(it) },
        modifier = modifier,
        placeholder = { Text(hint, style = textStyle) },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = keyboardOptions.copy(
            autoCorrectEnabled = true,
            showKeyboardOnFocus = true,
            capitalization = KeyboardCapitalization.Words
        ),
        textStyle = textStyle,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        )
    )
}

@SuppressLint("DefaultLocale")
fun Long.toMoneyString(addPlus: Boolean = true): String {
    val symbol = if (this > 0L && addPlus) "+" else ""
    val cents = (this % 100)
    val number = (this / 100)
    val main = String.format("%,d", number).replace(',', ' ')
    return "$symbol$main${if (cents != 0L) ".$cents" else ""}"
}
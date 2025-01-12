package com.al4apps.lists.presentation.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.al4apps.lists.R
import com.al4apps.lists.domain.models.FundModel
import com.al4apps.lists.presentation.fund.toMoneyString
import com.al4apps.lists.ui.theme.fractionColor
import com.al4apps.lists.ui.theme.greenText2


@Composable
fun FundTileLayout(fund: FundModel) {

    // Box for the background
    Box(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {

        // Box for the raised part
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = fractionColor)
                .fillMaxHeight(fund.raisedFraction()),
        )

        // Box for the content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Column for the text
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                NameText(fund.name)
                Spacer(modifier = Modifier.height(8.dp))
                RaisingText(stringResource(R.string.fund_item_raised_text))
                SumRaisedText(fund.raised)
                fund.toRaise?.let { toRaise ->
                    Spacer(modifier = Modifier.height(4.dp))
                    RaisingText(stringResource(R.string.fund_item_to_raise_text))
                    SumToRaiseText(toRaise, Modifier.padding(start = 8.dp))
                }
            }

        }

    }
}

@Composable
fun FundRowLayout(fund: FundModel, showName: Boolean = true) {
    // Box for the background
    Box(
        modifier = Modifier
            .height(intrinsicSize = IntrinsicSize.Min)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        // Box for the raised part
        Box(
            modifier = Modifier
                .fillMaxWidth(fund.raisedFraction())
                .background(color = fractionColor)
                .fillMaxHeight(),
        )

        // Box for the content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            Column( // Column for the text
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                if (showName) {
                    NameText(fund.name)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                RaisedTextRow(fund.raised)
                fund.toRaise?.let { toRaise ->
                    Spacer(modifier = Modifier.height(4.dp))
                    ToRaiseTextRow(toRaise)
                }
            }
        }
    }
}

@Composable
fun RaisedTextRow(raisedSum: Long) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RaisingText(stringResource(R.string.fund_item_raised_text))
        SumRaisedText(raisedSum)
    }
}

@Composable
fun SumRaisedText(raisedSum: Long) {
    Text(
        modifier = Modifier.padding(start = 8.dp),
        text = raisedSum.toMoneyString(false),
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = if (raisedSum > 0) greenText2
        else Color.Unspecified
    )
}

@Composable
fun ToRaiseTextRow(toRaise: Long) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RaisingText(stringResource(R.string.fund_item_to_raise_text))
        SumToRaiseText(toRaise, Modifier.padding(start = 8.dp))
    }
}

@Composable
fun SumToRaiseText(toRaise: Long, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = toRaise.toMoneyString(false),
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = Color.Red
    )
}

@Composable
fun RaisingText(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        fontSize = 12.sp,
        color = Color.DarkGray,
        modifier = modifier
    )
}

@Composable
private fun NameText(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
    )
}

fun FundModel.raisedFraction(): Float {
    return toRaise?.let {
        minOf(1f, (raised / it.toFloat()))
    } ?: 0f
}


@Composable
@Preview(showBackground = true)
fun ListPreview() {
    val model = FundModel(
        id = 1,
        name = "Сауна 11.01.2025",
        toRaise = 5000000,
        raised = 2000000,
        timestamp = System.currentTimeMillis()
    )
    FundRowLayout(model)
}
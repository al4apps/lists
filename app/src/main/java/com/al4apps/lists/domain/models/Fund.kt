package com.al4apps.lists.domain.models

import androidx.annotation.StringRes
import com.al4apps.lists.R

interface Fund {
    val id: Int
    var name: String
    var toRaise: Long?
    var raised: Long
    var timestamp: Long
}

data class FundModel(
    override val id: Int,
    override var name: String,
    override var toRaise: Long?,
    override var raised: Long,
    override var timestamp: Long
) : Fund, ListModel()

interface FundOptions {
    val fundId: Int
    var membersCount: Int
    var raisedSum: Long
    var needToDivide: Boolean
}

data class FundOptionsModel(
    override val fundId: Int,
    override var membersCount: Int,
    override var raisedSum: Long,
    override var needToDivide: Boolean = false,
) : FundOptions

data class EmptyListModel(
    @StringRes
    val nameStr: Int = R.string.new_list_base_name
) : ListModel()

sealed class ListModel()
package com.al4apps.mycomposeapp.domain.models

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
) : Fund, ListModel(name)

data object EmptyListModel : ListModel("")

sealed class ListModel(val listName: String)
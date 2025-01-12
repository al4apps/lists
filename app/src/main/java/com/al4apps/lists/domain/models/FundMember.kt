package com.al4apps.lists.domain.models

interface FundMember {
    val id: Int
    val fundId: Int
    val name: String
    val sum: Long
    var comment: String?
    var timestamp: Long
}

data class FundMemberModel(
    override val id: Int,
    override val fundId: Int,
    override val name: String,
    override val sum: Long,
    override var comment: String? = null,
    override var timestamp: Long
) : FundMember, ListItemModel()

data object EmptyFundItemModel : ListItemModel()

sealed class ListItemModel()
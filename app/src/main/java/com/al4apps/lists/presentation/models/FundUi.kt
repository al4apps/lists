package com.al4apps.lists.presentation.models

import com.al4apps.lists.domain.models.FundModel
import com.al4apps.lists.domain.models.FundOptionsModel

data class FundUi(
    val fundModel: FundModel,
    val fundOptions: FundOptionsModel
)

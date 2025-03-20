package com.al4apps.lists.di

import androidx.room.Room
import com.al4apps.lists.data.db.AppDatabase
import com.al4apps.lists.data.db.FundMembersDao
import com.al4apps.lists.data.db.FundsDao
import com.al4apps.lists.data.repositories.FundMembersRepositoryImpl
import com.al4apps.lists.data.repositories.FundsRepositoryImpl
import com.al4apps.lists.domain.repositories.FundMembersRepository
import com.al4apps.lists.domain.repositories.FundsRepository
import com.al4apps.lists.domain.usecases.AddNewFundMemberUseCase
import com.al4apps.lists.domain.usecases.AddNewFundUseCase
import com.al4apps.lists.domain.usecases.GetAllFundsUseCase
import com.al4apps.lists.domain.usecases.GetFundMembersUseCase
import com.al4apps.lists.domain.usecases.GetFundUseCase
import com.al4apps.lists.domain.usecases.UpdateFundMemberUseCase
import com.al4apps.lists.domain.usecases.UpdateFundUseCase
import com.al4apps.lists.presentation.home.HomeViewModel
import com.al4apps.lists.presentation.fund.FundViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<HomeViewModel> {
        HomeViewModel(getAllFundsUseCase = get(), updateFundUseCase = get())
    }
    viewModel<FundViewModel> {
        FundViewModel(
            getFundUseCase = get(),
            getFundMembersUseCase = get(),
            addNewFundMemberUseCase = get(),
            updateFundMemberUseCase = get(),
            updateFundUseCase = get(),
            addNewFundUseCase = get(),
        )
    }

}

val useCasesModule = module {
    factory<GetAllFundsUseCase> {
        GetAllFundsUseCase(fundsRepository = get())
    }
    factory<AddNewFundUseCase> {
        AddNewFundUseCase(fundsRepository = get())
    }
    factory<UpdateFundUseCase> {
        UpdateFundUseCase(fundsRepository = get())
    }
    factory<GetFundUseCase> {
        GetFundUseCase(fundsRepository = get())
    }
    factory<GetFundMembersUseCase> {
        GetFundMembersUseCase(fundMembersRepository = get())
    }
    factory<AddNewFundMemberUseCase> {
        AddNewFundMemberUseCase(fundMemberRepository = get())
    }
    factory<UpdateFundMemberUseCase> {
        UpdateFundMemberUseCase(fundMemberRepository = get())
    }
}

val dbModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single<FundsDao> {
        get<AppDatabase>().fundsDao()
    }
    single<FundMembersDao> {
        get<AppDatabase>().fundItemsDao()
    }

}

val dataModule = module {

    single<FundsRepository> {
        FundsRepositoryImpl(fundsDao = get(), membersDao = get())
    }

    single<FundMembersRepository> {
        FundMembersRepositoryImpl(membersDao = get())
    }
}


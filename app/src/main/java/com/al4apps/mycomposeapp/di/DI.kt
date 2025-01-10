package com.al4apps.mycomposeapp.di

import androidx.room.Room
import com.al4apps.mycomposeapp.data.db.AppDatabase
import com.al4apps.mycomposeapp.data.db.FundMembersDao
import com.al4apps.mycomposeapp.data.db.FundsDao
import com.al4apps.mycomposeapp.data.repositories.FundMembersRepositoryImpl
import com.al4apps.mycomposeapp.data.repositories.FundsRepositoryImpl
import com.al4apps.mycomposeapp.domain.repositories.FundMembersRepository
import com.al4apps.mycomposeapp.domain.usecases.AddNewFundMemberUseCase
import com.al4apps.mycomposeapp.domain.usecases.AddNewFundUseCase
import com.al4apps.mycomposeapp.domain.usecases.GetAllFundsUseCase
import com.al4apps.mycomposeapp.domain.usecases.GetFundMembersUseCase
import com.al4apps.mycomposeapp.domain.usecases.GetFundUseCase
import com.al4apps.mycomposeapp.domain.usecases.UpdateFundMemberUseCase
import com.al4apps.mycomposeapp.domain.usecases.UpdateFundUseCase
import com.al4apps.mycomposeapp.presentation.home.HomeViewModel
import com.al4apps.mycomposeapp.presentation.list.ListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<HomeViewModel> {
        HomeViewModel(getAllFundsUseCase = get())
    }
    viewModel<ListViewModel> {
        ListViewModel(
            getFundUseCase = get(),
            getFundMembersUseCase = get(),
            addNewFundMemberUseCase = get(),
            updateFundMemberUseCase = get(),
            addNewFundUseCase = get()
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

    single<FundsRepositoryImpl> {
        FundsRepositoryImpl(fundsDao = get(), membersDao = get())
    }

    single<FundMembersRepositoryImpl> {
        FundMembersRepositoryImpl(membersDao = get())
    }
}

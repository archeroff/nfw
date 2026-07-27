package com.weekchecker.di

import com.weekchecker.data.calculator.WeekCalculator
import com.weekchecker.data.repository.WeekRepositoryImpl
import com.weekchecker.domain.repository.WeekRepository
import com.weekchecker.domain.usecase.GetCurrentWeekUseCase
import com.weekchecker.presentation.screen.WeekViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf(::WeekCalculator)
    singleOf(::WeekRepositoryImpl) bind WeekRepository::class
    factoryOf(::GetCurrentWeekUseCase)
    viewModelOf(::WeekViewModel)
}

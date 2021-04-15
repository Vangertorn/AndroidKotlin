package com.example.myapplication

import android.app.Application
import com.example.myapplication.database.DatabaseConstructor
import com.example.myapplication.database.PlannerDatabase
import com.example.myapplication.screen.main.MainViewModel
import com.example.myapplication.screen.note_details.NoteDetailsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.android.viewmodel.dsl.viewModel

class PlannerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlannerApp)
            modules(listOf(viewModels, barnModels))
        }
    }

    private val viewModels = module {
        viewModel { MainViewModel(get()) }
        viewModel { NoteDetailsViewModel(get()) }
    }

    private val barnModels = module {
        single { DatabaseConstructor.create(get()) }
        factory { get<PlannerDatabase>().notesDao() }
    }
}
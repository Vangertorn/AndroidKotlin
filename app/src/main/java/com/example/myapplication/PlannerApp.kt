package com.example.myapplication

import android.app.Application
import com.example.myapplication.cloud.CloudInterface
import com.example.myapplication.database.DatabaseConstructor
import com.example.myapplication.database.PlannerDatabase
import com.example.myapplication.datastore.AppSettings
import com.example.myapplication.repository.CloudRepository
import com.example.myapplication.repository.NotesRepository
import com.example.myapplication.repository.UsersRepository
import com.example.myapplication.screen.enter_fragment.EnterViewModel
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
            modules(listOf(viewModel, barnModel, repositoryModel, cloudModel))
        }
    }

    private val viewModel = module {
        viewModel { MainViewModel(get(), get(), get()) }
        viewModel { NoteDetailsViewModel(get()) }
        viewModel { EnterViewModel(get()) }
    }

    private val barnModel = module {
        single { DatabaseConstructor.create(get()) }
        factory { get<PlannerDatabase>().notesDao() }
        factory { get<PlannerDatabase>().usersDao() }
        single { AppSettings(get()) }
    }
    private val repositoryModel = module {
        factory { UsersRepository(get(), get(), get()) }
        factory { NotesRepository(get(), get()) }
        factory { CloudRepository(get(), get(), get()) }
    }
    private val cloudModel = module {
        factory { CloudInterface.get() }
    }

}
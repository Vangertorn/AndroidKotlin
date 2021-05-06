package com.example.myapplication

import android.app.Application
import com.example.myapplication.cloud.CloudInterface
import com.example.myapplication.database.DatabaseConstructor
import com.example.myapplication.database.PlannerDatabase
import com.example.myapplication.datastore.AppSettings
import com.example.myapplication.repository.CloudRepository
import com.example.myapplication.repository.NotesRepository
import com.example.myapplication.repository.UsersRepository
import com.example.myapplication.screen.enter.LoginViewModel
import com.example.myapplication.screen.main.MainViewModel
import com.example.myapplication.screen.note_details.NoteDetailsViewModel
import com.example.myapplication.screen.setting.SettingViewModel
import com.example.myapplication.screen.sign_up.SingUpViewModel
import com.example.myapplication.screen.start.StartViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @ExperimentalCoroutinesApi
    private val viewModel = module {
        viewModel { MainViewModel(get(), get(), get()) }
        viewModel { NoteDetailsViewModel(get()) }
        viewModel { LoginViewModel(get()) }
        viewModel { StartViewModel(get()) }
        viewModel { SettingViewModel(get(),get()) }
        viewModel { SingUpViewModel(get()) }
    }

    private val barnModel = module {
        single { DatabaseConstructor.create(get()) }
        factory { get<PlannerDatabase>().notesDao() }
        factory { get<PlannerDatabase>().usersDao() }
        single { AppSettings(get()) }
    }
    private val repositoryModel = module {
        factory { UsersRepository(get(), get(), get()) }
        factory { NotesRepository(get(), get(), get()) }
        factory { CloudRepository(get(), get(), get()) }
    }
    private val cloudModel = module {
        factory { CloudInterface.get() }
    }

}
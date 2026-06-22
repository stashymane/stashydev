import model.AppState
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import viewmodel.AppViewmodel
import viewmodel.HomeScreenViewmodel

val KoinModule = module {
    viewModelOf(::AppViewmodel)
    viewModelOf(::HomeScreenViewmodel)
    single { AppState() }
}

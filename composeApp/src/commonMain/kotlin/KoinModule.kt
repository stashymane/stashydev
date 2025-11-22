import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ui.vm.AppViewmodel
import ui.vm.HomeScreenViewmodel

val KoinModule = module {
    viewModelOf(::AppViewmodel)
    viewModelOf(::HomeScreenViewmodel)
}

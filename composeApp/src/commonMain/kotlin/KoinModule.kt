import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import vm.AppViewmodel
import vm.HomeScreenViewmodel

val KoinModule = module {
    viewModelOf(::AppViewmodel)
    viewModelOf(::HomeScreenViewmodel)
}

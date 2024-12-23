import model.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import vm.HomeScreenViewmodel

val KoinModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::HomeScreenViewmodel)
}

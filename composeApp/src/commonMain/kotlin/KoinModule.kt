import model.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val KoinModule = module {
    viewModelOf(::MainViewModel)
}

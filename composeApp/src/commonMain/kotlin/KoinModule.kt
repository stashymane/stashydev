import data.ProjectsRepository
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import viewmodel.HomeScreenViewmodel
import viewmodel.ProjectsViewmodel

val KoinModule = module {
    single { ProjectsRepository() }

    viewModelOf(::HomeScreenViewmodel)
    viewModelOf(::ProjectsViewmodel)
}

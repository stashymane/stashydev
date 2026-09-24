import data.AboutRepository
import data.ProjectsRepository
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import screens.about.AboutViewmodel
import screens.home.HomeScreenViewmodel
import screens.projects.ProjectsViewmodel

val KoinModule = module {
    single { ProjectsRepository() }
    single { AboutRepository() }

    viewModelOf(::HomeScreenViewmodel)
    viewModelOf(::ProjectsViewmodel)
    viewModelOf(::AboutViewmodel)
}

package associations.di

import associations.data.repository.AssociationsRepository
import associations.data.repository.AssociationsRepositoryImpl
import associations.ui.main.AppViewModel
import org.koin.dsl.module

val dataSourceModule = module {

}

val repositoryModule = module {
    single<AssociationsRepository> { AssociationsRepositoryImpl() }
}

val viewModelModule = module {
    single<AppViewModel> { AppViewModel(get()) }
}

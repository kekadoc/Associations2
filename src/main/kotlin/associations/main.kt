import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import associations.data.mock.Mock
import associations.di.dataSourceModule
import associations.di.repositoryModule
import associations.di.viewModelModule
import associations.ui.main.content.MainContent
import org.koin.core.context.startKoin

fun main() = application {
    val koinApplication = startKoin {
        modules(
            dataSourceModule,
            repositoryModule,
            viewModelModule
        )
    }
    Mock.DefaultElement
    Window(onCloseRequest = ::exitApplication) {
        MainContent(koinApplication.koin.get())
    }
}

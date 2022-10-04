package associations.data.mock

import associations.data.model.Element
import associations.data.repository.AssociationsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object Mock : KoinComponent {
    val DefaultElement: Element by lazy {
        runBlocking {
            val target = "Карьер"
            val repository = get<AssociationsRepository>()
            repository.apply {
                this.setPropertyValue(target = target, property = "Ископаемые","Золото", "Серебро", "Медь", "Олово").collect()
                this.setPropertyValue(target = target, property = "Расположение","Россия").collect()
                this.setPropertyValue(target = target, property = "Глубина залегания",">10км").collect()
                this.setPropertyValue(target = target, property = "Метод добычи","Копать", "Ломать").collect()
            }
            repository.get(target).first()
        }
    }
}
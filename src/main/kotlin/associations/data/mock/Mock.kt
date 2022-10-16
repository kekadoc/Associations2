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
            val repository = get<AssociationsRepository>()
            repository.apply {
                
                with("поиск полезных ископаемых") {
                    "Ископаемые" and listOf("Золото", "Медь", "Серебро")
                    "Расположение" and listOf("Красноярский край", "Хабаровский край")
                    "Глубина залегания" and listOf("10м-10км", "10м-20км", "1-2км")
                    "Метод добычи" and listOf("Карьерный", "Шахтный", "Скважинный")
                }
                with("Золото") {
                    "Плотность" and listOf("19,32 г/см³")
                    "Расположение" and listOf("Красноярский край", "Хабаровский край")
                    "Глубина залегания" and listOf("1-2км")
                }
                with("Серебро") {
                    "Плотность" and listOf("10,49 г/см³")
                    "Расположение" and listOf("Красноярский край", "Хабаровский край")
                    "Глубина залегания" and listOf("10м-10км")
                }
                with("Медь") {
                    "Плотность" and listOf("8,96 г/см³")
                    "Расположение" and listOf("Красноярский край")
                    "Глубина залегания" and listOf("10м-20км")
                }
                with("Красноярский край") {
                    "Принадлежность" and listOf("Российская федерация")
                }
                with("Хабаровский край") {
                    "Принадлежность" and listOf("Российская федерация")
                }
                with("Карьерный") {
                    "Максимальная глубина" and listOf("500м")
                }
                with("Шахтный") {
                    "Максимальная глубина" and listOf("1326м")
                }
                with("Скважинный") {
                    "Максимальная глубина" and listOf("5000м")
                }
                with("1-2км") {
                    "Способ добычи" and listOf("Шахтный")
                    "Залегает" and listOf("Золото")
                }
                with("10м-10км") {
                    "Способ добычи" and listOf("Карьерный", "Шахтный")
                    "Залегает" and listOf("Серебро")
                }
                with("10м-20км") {
                    "Способ добычи" and listOf("Карьерный", "Шахтный", "Скважинный")
                    "Залегает" and listOf("Медь")
                }
                with("Российская федерация") {
                    "Субъекты" and listOf("Красноярский край", "Хабаровский край")
                }
            }

            repository.get("поиск полезных ископаемых").first()
        }
    }
}

private suspend fun AssociationsRepository.with(target: String, block: suspend BuildingScope.() -> Unit) {
    block(BuildingScopeImpl(this, target))
}

interface BuildingScope {
    suspend infix fun String.and(values: Collection<String>)
}

class BuildingScopeImpl(
    private val repository: AssociationsRepository,
    private val target: String
) : BuildingScope {
    override suspend fun String.and(values: Collection<String>) {
        repository.setPropertyValue(target, this, *values.toTypedArray()).collect()
    }
}
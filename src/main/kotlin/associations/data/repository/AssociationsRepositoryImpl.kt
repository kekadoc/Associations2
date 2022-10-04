package associations.data.repository

import associations.data.model.Element
import kotlinx.coroutines.flow.*

class AssociationsRepositoryImpl : AssociationsRepository {
    
    private val elements: MutableMap<String, Element> = mutableMapOf()
    
    override fun get(value: String): Flow<Element> {
        return flow {
            val element = elements.getOrPut(value) { Element(value) }
            emit(element)
        }
    }
    
    override fun getAll(vararg values: String): Flow<Set<Element>> {
        return flow {
            val elements = values.toSet().map { value -> elements.getOrPut(value) { Element(value) } }.toSet()
            emit(elements)
        }
    }
    
    override fun setPropertyValue(target: String, property: String, value: String): Flow<Element> {
        return setPropertyValue(target = target, property = property, values = arrayOf(value))
    }
    
    override fun setPropertyValue(target: String, property: String, vararg values: String): Flow<Element> {
        return get(target).map { element ->
            element.copy(
                parts = element.parts.toMutableMap().apply {
                    put(property, getOrDefault(property, mutableSetOf()).toMutableSet().apply { addAll(values) })
                }
            )
        }.onEach { newElement ->
            elements[target] = newElement
        }
    }
    
    override fun deletePropertyValue(target: String, property: String, value: String): Flow<Element> {
        return deletePropertyValue(target = target, property = property, values = arrayOf(value))
    }
    
    override fun deletePropertyValue(target: String, property: String, vararg values: String): Flow<Element> {
        return get(target).map { element ->
            element.copy(
                parts = element.parts.toMutableMap().apply {
                    put(property, getOrDefault(property, mutableSetOf()).toMutableSet().apply { removeAll(values) })
                }
            )
        }.onEach { newElement ->
            elements[target] = newElement
        }
    }
    
    override fun deleteProperty(target: String, property: String): Flow<Element> {
        return get(target).map { element ->
            element.copy(
                parts = element.parts.toMutableMap().apply { remove(property) }
            )
        }.onEach { newElement ->
            elements[target] = newElement
        }
    }
    
}
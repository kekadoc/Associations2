package associations.data.repository

import associations.common.extensions.flowOf
import associations.data.model.Element
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class AssociationsRepositoryImpl : AssociationsRepository {
    
    private val elements: MutableMap<String, Element> = mutableMapOf()
    
    override fun get(value: String): Flow<Element> = flowOf {
        elements.getOrPut(value) { Element(value) }
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
    
    
    
    
    override fun getAll(vararg values: String): Flow<Set<Element>> = flowOf {
        values.toSet()
            .map { value -> elements.getOrPut(value) { Element(value) } }
            .toSet()
    }
    
    override fun deletePropertyValue(target: String, property: String, value: String): Flow<Element> {
        return deletePropertyValue(target = target, property = property, values = arrayOf(value))
    }
    
    override fun setPropertyValue(target: String, property: String, value: String): Flow<Element> {
        return setPropertyValue(target = target, property = property, values = arrayOf(value))
    }
    
}
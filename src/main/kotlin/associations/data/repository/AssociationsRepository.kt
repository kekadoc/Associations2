package associations.data.repository

import associations.data.model.Element
import kotlinx.coroutines.flow.Flow

interface AssociationsRepository {
    
    fun get(value: String): Flow<Element>
    
    fun getAll(vararg values: String): Flow<Set<Element>>
    
    fun setPropertyValue(target: String, property: String, value: String): Flow<Element>
    
    fun setPropertyValue(target: String, property: String, vararg values: String): Flow<Element>
    
    fun deletePropertyValue(target: String, property: String, value: String): Flow<Element>
    
    fun deletePropertyValue(target: String, property: String, vararg values: String): Flow<Element>
    
    fun deleteProperty(target: String, property: String): Flow<Element>
    
}
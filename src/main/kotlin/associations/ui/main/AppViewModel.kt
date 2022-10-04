package associations.ui.main

import associations.common.ViewModel
import associations.data.repository.AssociationsRepository
import associations.ui.main.model.mvi.AppSideEffect
import associations.ui.main.model.mvi.AppViewState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce

class AppViewModel(
    private val repository: AssociationsRepository
) : ViewModel(), ContainerHost<AppViewState, AppSideEffect> {
    
    override val container: Container<AppViewState, AppSideEffect> = viewModelScope.container(AppViewState()) {
        search()
    }
    
    fun onValueChanged(value: String) = intent {
        reduce { state.copy(searchValue = value) }
    }
    
    fun searchValue(value: String) = intent {
        reduce { state.copy(isSearchLoading = true, searchValue = value) }
        val element = repository.get(value).first()
        reduce { state.copy(isSearchLoading = false, currentElement = element) }
    }
    
    fun search() = intent {
        if (state.searchValue.isEmpty()) return@intent
        reduce { state.copy(isSearchLoading = true) }
        val element = repository.get(state.searchValue).first()
        reduce { state.copy(isSearchLoading = false, currentElement = element) }
    }
    
    fun addPropertyAction() = intent {
        postSideEffect(AppSideEffect.ShowAddingProperty)
    }
    
    fun addPropertyValueAction(property: String) = intent {
        postSideEffect(AppSideEffect.ShowAddingPropertyValue(property, null))
    }
    
    fun setPropertyValueAction(property: String, value: String) = intent {
        postSideEffect(AppSideEffect.ShowAddingPropertyValue(property, value))
    }
    
    fun setPropertyValue(property: String, value: String) = intent {
        val targetElement = state.currentElement ?: return@intent
        reduce { state.copy(isElementModificationLoading = true) }
        val newElement = repository.setPropertyValue(
            target = targetElement.value,
            property = property,
            value = value
        ).first()
        reduce { state.copy(isElementModificationLoading = false, currentElement = newElement) }
    }
    
    fun deletePropertyValue(property: String, value: String) = intent {
        val targetElement = state.currentElement ?: return@intent
        reduce { state.copy(isElementModificationLoading = true) }
        val newElement = repository.deletePropertyValue(
            target = targetElement.value,
            property = property,
            value = value
        ).first()
        reduce { state.copy(isElementModificationLoading = false, currentElement = newElement) }
    }
    
    fun deleteProperty(property: String) = intent {
        val targetElement = state.currentElement ?: return@intent
        reduce { state.copy(isElementModificationLoading = true) }
        val newElement = repository.deleteProperty(
            target = targetElement.value,
            property = property
        ).first()
        reduce { state.copy(isElementModificationLoading = false, currentElement = newElement) }
    }
    
}
package associations.ui.main.model.mvi

sealed class AppSideEffect {
    object ShowAddingProperty : AppSideEffect()
    data class ShowAddingPropertyValue(val property: String, val value: String?) : AppSideEffect()
}
package associations.ui.main.model.mvi

import associations.data.mock.Mock
import associations.data.model.Element

data class AppViewState(
    val isSearchLoading: Boolean = false,
    val isElementModificationLoading: Boolean = false,
    val searchValue: String = Mock.DefaultElement.value,
    val currentElement: Element? = null
)
package associations.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ViewModel {
    val viewModelScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
}
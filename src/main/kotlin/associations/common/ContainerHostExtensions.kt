package associations.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import org.orbitmvi.orbit.ContainerHost

@Composable
fun <STATE : Any, SIDE_EFFECT : Any> ContainerHost<STATE, SIDE_EFFECT>.collectSideEffect(
    sideEffect: (suspend (sideEffect: SIDE_EFFECT) -> Unit)
) {
    val sideEffectFlow = container.sideEffectFlow
    
    LaunchedEffect(sideEffectFlow) {
        sideEffectFlow.collect { sideEffect.invoke(it) }
    }
}

@Composable
fun <STATE : Any, SIDE_EFFECT : Any> ContainerHost<STATE, SIDE_EFFECT>.collectState(
    state: (suspend (state: STATE) -> Unit)
) {
    val stateFlow = container.stateFlow
    LaunchedEffect(stateFlow) {
        stateFlow.collect { state(it) }
    }
}


@Composable
fun <STATE : Any, SIDE_EFFECT : Any> ContainerHost<STATE, SIDE_EFFECT>.collectAsState(): State<STATE> {
    val stateFlow = container.stateFlow
    val initialValue = stateFlow.value
    return stateFlow.collectAsState(initialValue)
}
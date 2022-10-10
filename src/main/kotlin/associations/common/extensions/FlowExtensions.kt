package associations.common.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> flowOf(block: suspend () -> T): Flow<T> {
    return flow {
        emit(block())
    }
}
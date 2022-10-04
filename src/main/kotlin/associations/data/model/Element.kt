package associations.data.model

data class Element(
    val value: String,
    val parts: Map<String, Set<String>> = emptyMap()
)
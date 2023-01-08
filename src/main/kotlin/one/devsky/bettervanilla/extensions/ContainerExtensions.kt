package one.devsky.bettervanilla.extensions

infix fun <I, O> Collection<I>.convert(process: I.() -> O): Collection<O> {
    val out = mutableListOf<O>()
    forEach {
        out.add(process(it))
    }
    return out
}

inline infix fun <I, reified O> Array<out I>.convert(process: I.() -> O): Array<out O> {
    val out = mutableListOf<O>()
    forEach {
        out.add(process(it))
    }
    return out.toTypedArray()
}

infix fun <A, B, C, D> Map<A, B>.convert(process: Pair<A, B>.() -> Pair<C, D>): Map<C, D> {
    val out = mutableListOf<Pair<C, D>>()
    forEach {
        out.add(process(it.toPair()))
    }
    return out.toMap()
}

fun Collection<String>.containsIgnoreCase(
    element: String
): Boolean = any { it.equals(element, true) }

fun <T> MutableCollection<T>.clear(onRemove: (T) -> Unit) {
    toMutableList().forEach {
        remove(it)
        onRemove(it)
    }
}

fun Array<String>.containsIgnoreCase(
    element: String
): Boolean = any { it.equals(element, true) }

fun <V> Map<String, V>.containsKeyIgnoreCase(
    key: String
): Boolean = keys.containsIgnoreCase(key)

fun <V> Map<String, V>.getIgnoreCase(
    key: String
): V? = entries.find { it.key.equals(key, true) }?.value

fun <K, V> MutableMap<K, V>.clear(onRemove: (K, V) -> Unit) {
    keys.toMutableSet().forEach { onRemove(it, remove(it)!!) }
}

inline infix fun <T> Collection<T>.withForEach(process: T.() -> Unit) = forEach { process(it) }

inline infix fun <T> Array<T>.withForEach(process: T.() -> Unit) = forEach { process(it) }
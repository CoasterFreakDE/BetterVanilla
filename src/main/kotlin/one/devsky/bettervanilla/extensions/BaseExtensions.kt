package one.devsky.bettervanilla.extensions

fun <O> Any?.forceCast() = this as O

fun <O> Any?.forceNullableCast() = this as O?

inline infix fun <I, O> I.with(process: I.() -> O) = process(this)
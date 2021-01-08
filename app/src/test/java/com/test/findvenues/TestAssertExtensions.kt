package com.test.findvenues

import io.reactivex.rxjava3.observers.TestObserver

fun <T> TestObserver<T>.assertLastValue(predicate: (T) -> Boolean): TestObserver<T> {
    if (values().isEmpty()) throw AssertionError("Asserting last value for a stream with no values!")

    assertValueAt(values().size - 1, predicate)
    return this
}
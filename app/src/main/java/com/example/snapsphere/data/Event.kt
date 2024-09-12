package com.example.snapsphere.data

// it is generic class to handle exceptions and show user toast according to the exception
// the benefit of using this is that it can be used through out the app
open class Event<out T>(
    private val content: T
) {
    var hasContentBeenHandled = false
        private set

    fun handleContent(): T? {
        return if (hasContentBeenHandled) {
            null
        } else {
            hasContentBeenHandled = true
            content
        }
    }
}
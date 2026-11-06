package com.example.leakcanarydemo

/**
 * Application-wide singleton (lives as long as the process).
 *
 * INTENTIONAL MEMORY LEAK: listeners are added but never removed.
 * Anything registered here – e.g. an Activity – stays strongly reachable from a GC root
 * (the static INSTANCE field of this object) even after it has been destroyed.
 */
object LeakyRegistry {

    interface Listener {
        fun onEvent(message: String)
    }

    private val listeners = mutableListOf<Listener>()

    val listenerCount: Int get() = listeners.size

    fun register(listener: Listener) {
        listeners += listener
    }

    @Suppress("unused") // deliberately never called – that's the bug
    fun unregister(listener: Listener) {
        listeners -= listener
    }

    fun notifyAll(message: String) {
        listeners.forEach { it.onEvent(message) }
    }
}

package nl.xanderdekeijzer.rides.utils

import kotlin.random.Random

class RandomId {
    companion object {
        private val random = Random(System.currentTimeMillis())
        fun next(): Int = random.nextInt()
    }
}

class IncrementId {
    companion object {
        private var counter = 0
        fun next(): Int = counter++
    }
    private var counter = 0
    fun next(): Int = counter++
}

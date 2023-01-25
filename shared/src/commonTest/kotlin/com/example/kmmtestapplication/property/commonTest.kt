package com.example.kmmtestapplication.property

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ComputedTest {

    private fun delayBlocking(timeMillis: Long = 10) = runBlocking { delay(timeMillis) }

    private lateinit var scope: CoroutineScope

    @BeforeTest
    fun createScope() {
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @AfterTest
    fun cancelScope() {
        scope.cancel()
    }

    @Test
    fun `returns computed from initial states`() {
        val obj = object : Any() {
            val state1: MutableStateFlow<Int> = MutableStateFlow(1)
            val state2: MutableStateFlow<Int> = MutableStateFlow(2)
            val computedFlow = computed(state1, state2) { val1, val2 -> val1 + val2 }
        }

        assertEquals(3, obj.computedFlow.value)
    }

    @Test
    fun `updates computed value after state changed`() {
        val obj = object : Any() {
            val state1: MutableStateFlow<Int> = MutableStateFlow(1)
            val computedFlow = computed(state1) { it }
        }

        repeat(3) { obj.state1.value++ }

        delayBlocking()
        assertEquals(4, obj.computedFlow.value)
    }

    @Test
    fun `updates computed value after two states changed`() {
        val obj = object : Any() {
            val state1: MutableStateFlow<Int> = MutableStateFlow(1)
            val state2: MutableStateFlow<Int> = MutableStateFlow(2)
            val computedFlow = computed(state1, state2) { val1, val2 -> val1 + val2 }
        }

        repeat(3) {
            delayBlocking()
            obj.state1.value++
        }
        repeat(2) {
            delayBlocking()
            obj.state2.value++
        }

        delayBlocking()
        assertEquals(8, obj.computedFlow.value)
    }

    @Test
    fun `updates computed value after three states changed`() {
        val obj = object : Any() {
            val state1: MutableStateFlow<Int> = MutableStateFlow(1)
            val state2: MutableStateFlow<Int> = MutableStateFlow(2)
            val state3: MutableStateFlow<Int> = MutableStateFlow(3)
            val computedFlow =
                computed(state1, state2, state3) { val1, val2, val3 -> val1 + val2 + val3 }
        }

        repeat(3) {
            delayBlocking()
            obj.state1.value++
        }
        repeat(2) {
            delayBlocking()
            obj.state2.value++
        }
        repeat(1) {
            delayBlocking()
            obj.state3.value++
        }

        delayBlocking()
        assertEquals(12, obj.computedFlow.value)
    }

    @Test
    fun `updates computed value after another computed`() {
        val obj = object : Any() {
            val state: MutableStateFlow<Int> = MutableStateFlow(1)
            val computed1 = computed(state) { it }
            val computed2 = computed(computed1) { it + 3 }
        }

        repeat(3) { obj.state.value++ }

        delayBlocking()
        assertEquals(7, obj.computed2.value)
    }

    @Test
    fun `updates computed value in binding`() {
        val obj = object : Any() {
            val state: MutableStateFlow<Int> = MutableStateFlow(0)
            val computed = computed(state) { it }
        }
        val values = mutableListOf<Int>()

        scope.launch {
            obj.computed.collect {
                values.add(it)
            }
        }

        repeat(3) {
            delayBlocking()
            obj.state.value++
        }

        assertEquals(listOf(0, 1, 2, 3), values)
    }
}
package com.example.wetharapplication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainDispatcherRule (val dispathcer : TestCoroutineDispatcher = TestCoroutineDispatcher())
    : TestWatcher() , TestCoroutineScope by TestCoroutineScope(dispathcer)  {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(dispathcer)
    }

    override fun finished(description: Description) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }

}
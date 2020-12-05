package com.techyourchance.coroutines.exercises.exercise6

import com.techyourchance.coroutines.common.ThreadInfoLogger
import com.techyourchance.coroutines.common.ThreadInfoLogger.logThreadInfo
import kotlinx.coroutines.*

class Exercise6BenchmarkUseCase(private val postBenchmarkResultsEndpoint: PostBenchmarkResultsEndpoint) {

    suspend fun executeBenchmark(benchmarkDurationSeconds: Int) = withContext(Dispatchers.Default) {
        logThreadInfo("benchmark started")

        val stopTimeNano = System.nanoTime() + benchmarkDurationSeconds * 1_000_000_000L

        var iterationsCount: Long = 0
        while (System.nanoTime() < stopTimeNano && isActive) {
            iterationsCount++
        }

        ensureActive()
        runInterruptible {
            postBenchmarkResultsEndpoint.postBenchmarkResults(benchmarkDurationSeconds, iterationsCount)
        }
        logThreadInfo("benchmark results posted to the server")

        logThreadInfo("returning")
        iterationsCount
    }

}
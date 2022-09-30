package calculations.parallel.loops

class ParallelLoopState(private val parallelLoop: ParallelLoop) {
    val isStarted = parallelLoop.isStarted
    val isCompleted = parallelLoop.isCompleted
    val isExecuting = parallelLoop.isExecuting

    fun waitResult() : ParallelLoopState{
        parallelLoop.join()
        return this
    }
}
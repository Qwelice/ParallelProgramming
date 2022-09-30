package calculations.parallel

import calculations.parallel.loops.Loop
import calculations.parallel.loops.ParallelLoop
import calculations.parallel.loops.ParallelLoopState

class Parallel {
    companion object{
        fun parallelFor(startInclusive: Int, endExclusive: Int, action: (Int, Loop) -> Unit) : ParallelLoopState {
            val pLoop = ParallelLoop()
            pLoop.constructLoop(startInclusive, endExclusive, action)
            pLoop.start()
            return pLoop.state
        }
    }
}
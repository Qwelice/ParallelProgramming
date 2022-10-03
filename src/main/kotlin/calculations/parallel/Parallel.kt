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

        fun <T> parallelReduce(collection: List<T>, accumulator: (T, T) -> T) : ParallelLoopState{
            val pLoop = ParallelLoop()
            if(collection.size >= 2){
                pLoop.constructLoop(0, collection.size){i, l ->
                    if(l.accumulator == null){
                        l.accumulator = collection[i]
                    } else{
                        l.accumulator = accumulator(l.accumulator as T, collection[i])
                    }
                }
                pLoop.start()
            }
            return pLoop.state
        }
    }
}
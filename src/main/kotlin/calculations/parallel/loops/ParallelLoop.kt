package calculations.parallel.loops

import java.lang.IllegalArgumentException

class ParallelLoop {
    val loops: List<Loop>
        get() = _loops
    val state = ParallelLoopState(this)
    val isStarted: Boolean
        get() = _isStarted
    val isCompleted: Boolean
        get(){
            var completed = 0
            loops.forEach { if(it.isCompleted) completed ++ }
            return completed == loops.size
        }
    val isExecuting: Boolean
        get() {
            var executing = 0
            loops.forEach { if(it.isExecuting) executing ++ }
            return executing > 0
        }
    private val _loops = mutableListOf<Loop>()
    private var _isStarted = false

    fun constructLoop(startInclusive: Int, endExclusive: Int, action: (Int, Loop) -> Unit) : ParallelLoopState{
        val steps = endExclusive - startInclusive
        if(steps <= 0) throw IllegalArgumentException("wrong diapason")
        val pCount = Runtime.getRuntime().availableProcessors()
        val ds = steps / pCount
        val residual = steps % pCount
        val tCount = if(ds == 0) residual else pCount
        val s = steps / tCount
        repeat(tCount){
            appendLoop(
                Loop(it * s + startInclusive,
                    (it + 1) * s + startInclusive + if(it == pCount - 1) residual else 0,
                    action)
            )
        }
        return state
    }

    fun start(){
        if(!isStarted){
            _isStarted = true
            loops.forEach{
                if(!it.isStarted){
                    it.start()
                }
            }
        }
    }

    fun join(){
        loops.forEach { it.join() }
    }

    fun appendLoop(loop: Loop){
        _loops.add(loop)
    }
}
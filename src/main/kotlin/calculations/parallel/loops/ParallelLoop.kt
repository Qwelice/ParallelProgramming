package calculations.parallel.loops

import java.lang.IllegalArgumentException

class ParallelLoop {

    companion object{
        private val PROCESSORS_COUNT = Runtime.getRuntime().availableProcessors()
        private val isParallelAvailable = PROCESSORS_COUNT > 1
    }
    private val _loops = mutableListOf<Loop>()
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
    private var _isStarted = false

    fun constructLoop(startInclusive: Int, endExclusive: Int, action: (Int, Loop) -> Unit) : ParallelLoopState{
        val steps = endExclusive - startInclusive
        if(steps <= 0) throw IllegalArgumentException("wrong diapason")
        val ds = steps / PROCESSORS_COUNT
        val residual = steps % PROCESSORS_COUNT
        val tCount = if(ds == 0) residual else PROCESSORS_COUNT
        val s = steps / tCount
        repeat(tCount){
            appendLoop(
                Loop(it * s + startInclusive,
                    (it + 1) * s + startInclusive + if(it == PROCESSORS_COUNT - 1) residual else 0,
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
package calculations.parallel.loops

class Loop(private val startInclusive: Int, private val endExclusive: Int, private val action: (Int, Loop) -> Unit) : Thread() {
    val currentIteration: Int
        get() = _iteration
    val totalIterations: Int
    val isStarted: Boolean
        get() = _isStarted
    val isCompleted: Boolean
        get() = _isCompleted
    val isExecuting: Boolean
        get() = _isExecuting

    private val steps: Int = endExclusive - startInclusive

    private var _iteration = 0
    private var _isStarted = false
    private var _isCompleted = false
    private var _isExecuting = false

    init {
        if(steps <= 0) throw IllegalArgumentException("wrong diapason")
        totalIterations = steps
    }

    constructor(startInclusive: Int, endExclusive: Int,
                isDaemon: Boolean, action: (Int, Loop) -> Unit) : this(startInclusive, endExclusive, action){
                this.isDaemon = isDaemon
    }

    override fun run() {
        _isStarted = true
        _isCompleted = false
        _isExecuting = true
        for(i in startInclusive until endExclusive){
            _iteration = i
            action(i, this)
        }
        _isCompleted = true
        _isExecuting = false
    }
}
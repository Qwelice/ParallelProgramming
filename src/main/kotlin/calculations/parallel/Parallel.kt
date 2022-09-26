package calculations.parallel

import kotlin.concurrent.thread

class Parallel {
    companion object{
        fun parallelFor(startInclusive: Int, endExclusive: Int, vararg actions: (Int) -> Unit){
            val steps = endExclusive - startInclusive
            val tCount = getOptimalThreadsCount(steps)
            val ds = steps / tCount
            val residual = steps % tCount
            val threads = mutableListOf<Thread>()
            repeat(tCount){
                thread(start = true, isDaemon = false){
                    for(i in it * ds + startInclusive until
                            (it + 1) * ds + startInclusive + if(it == tCount - 1) residual else 0){
                        actions.forEach {
                            it(i)
                        }
                    }
                }.apply {
                    threads.add(this)
                }
            }
            threads.forEach { it.join() }
        }

        private fun getOptimalThreadsCount(steps: Int) : Int{
            if(steps <= 0) throw IllegalArgumentException("wrong steps count")
            val pCount = Runtime.getRuntime().availableProcessors()
            val ds = steps / pCount
            val residual = steps % pCount
            return if(ds == 0) residual else pCount
        }

        private fun getOptimalThreadsCount(startInclusive: Int, endExclusive: Int) : Int{
            val steps = endExclusive - startInclusive
            return getOptimalThreadsCount(steps)
        }
    }
}
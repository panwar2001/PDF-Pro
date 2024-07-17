//https://gist.github.com/panwar2001/c72e6ad04d369a65874aea9aa6f916e2
class Engine{
    fun start(){
        println("engine start")
    }
}
class Car {
    private val engine = Engine()

    fun start() {
        engine.start()
    }
}

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("Hello!")
        }
    }
}
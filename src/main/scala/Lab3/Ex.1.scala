import com.cra.figaro.algorithm.factored._
import com.cra.figaro.language._
import com.cra.figaro.library.compound._
import com.cra.figaro.library.compound.If


object HelloWorld 
{
    Universe.createNew()
    val sunnyToday = Flip(0.2)
    val sideOfBed = Flip(0.5)

    val greeting = If(sideOfBed,
    greetingToday,
    Constant("Oh no, not again"))

    val greetingToday = If(sunnyToday,
    Select(0.6->"Hello, world!",0.4->"Howdy, universe!"),
    Select(0.2->"Hello, world!",0.8->"Oh no, not again"))
    
    val sunnyTomorrow= If(sunnyToday,Flip(0.8), Flip(0.05))
    
    val greetingTomorrow = If(sunnyTomorrow,
    Select(0.6->"Hello, world!",0.4->"Howdy, universe!"),
    Select(0.2->"Hello, world!",0.8->"Oh no, not again"))

    def main(args: Array[String])
    {
        println(VariableElimination.probability(greetingToday, "Hello, world!"))
        
        sideOfBed.observe(true)
        println(VariableElimination.probability(greeting, "Hello, world!"))
    }
}

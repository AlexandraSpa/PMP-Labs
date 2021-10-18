import com.cra.figaro.algorithm.factored._
import com.cra.figaro.language._
import com.cra.figaro.library.compound._
import com.cra.figaro.library.compound.If


object HelloWorldOriginal
{
    Universe.createNew()
    val sunnyToday = Flip(0.2)

    val greetingToday = If(sunnyToday,
    Select(0.6->"Hello, world!",0.4->"Howdy, universe!"),
    Select(0.2->"Hello, world!",0.8->"Oh no, not again"))
    
    val sunnyTomorrow= If(sunnyToday,Flip(0.8), Flip(0.05))
    
    val greetingTomorrow = If(sunnyTomorrow,
    Select(0.6->"Hello, world!",0.4->"Howdy, universe!"),
    Select(0.2->"Hello, world!",0.8->"Oh no, not again"))

    def infer() 
    {
        greetingToday.observe("Oh no, not again")
        val result = VariableElimination.probability(sunnyToday, true)
        val result2 = VariableElimination.probability(sunnyToday, false)
        println("If today's greeting is \"Oh no, not again\", today's weather is sunny with probability "
         + result + " and cloudy with probability " + result2)
    }

    def main(args: Array[String])
    {
        infer()
    }
}
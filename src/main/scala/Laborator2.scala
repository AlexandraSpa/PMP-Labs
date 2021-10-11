import com.cra.figaro.algorithm.factored._
import com.cra.figaro.language._
import com.cra.figaro.library.compound._


object Shopping 
{
    Universe.createNew()
    private val discount = Flip(0.5)
    private val need = Flip(0.5)
    private val shopping = CPD(discount, need,
                        (false, false) -> Flip(0.2),
                        (false, true) -> Flip(0.8),
                        (true, false) -> Flip(0.5),
                        (true, true) -> Flip(1))

    def main(args: Array[String])
    {
        shopping.observe(true)

        val alg = VariableElimination(discount, need)
        alg.start()
        println("Probability of need: " + alg.probability(need, true))
        alg.kill()
    }
}
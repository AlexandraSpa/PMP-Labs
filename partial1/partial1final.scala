/*
 * 
 */


import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.library.compound._
import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.algorithm.sampling.Importance
import com.cra.figaro.library.atomic.discrete._

object partial 
{
    Universe.createNew()
    val die1 = FromRange(1, 7)
    val die2 = FromRange(1, 7)
    val sum = Apply(die1, die2, (i1: Int, i2: Int) => i1 + i2)
    val winner = Apply(sum,
        (s: Int) => 
        if (s == 7 || s == 11)
            "Player one won"
        if (s == 2 || s == 3 || s == 12)
            "Player two won"
        else "Both won"

    def main(args: Array[String])
    {

        val alg = VariableElimination(sum)
        alg.start()
        println("Probability of sum = 11: " + alg.probability(sum, 11))
        alg.kill()
    }
}
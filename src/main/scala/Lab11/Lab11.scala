package Lab11

import com.cra.figaro.language._
import com.cra.figaro.library.compound._
import com.cra.figaro.library.atomic.continuous.Normal
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.algorithm.filtering.ParticleFilter
import com.cra.figaro.algorithm.factored._

object ex1 {
  def main(args: Array[String]) {

    //a.
    val POTUS = Flip(1.0/40000000.0)

    val LH = CPD(POTUS,
      true -> Flip(1.0/3.0),
      false -> Flip(1.0/10.0))

    LH.observe(true)
    println(VariableElimination.probability(POTUS, true))
    LH.unobserve()

    //b.
    val HU = CPD(POTUS,
      true -> Flip(3.0/20.0),
      false -> Flip(1.0/2000.0))
    HU.observe(true)
    println(VariableElimination.probability(POTUS, true))
    HU.unobserve()

    //c.
    LH.observe(true)
    HU.observe(true)
    println(VariableElimination.probability(POTUS, true))

  }
} 
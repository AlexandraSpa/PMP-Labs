package Lab8

import com.cra.figaro.library.atomic.continuous
import com.cra.figaro.library.collection.Container
import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.language._
import com.cra.figaro.library.compound._

object Ex1 {
  def main(args: Array[String]) {
    val length = 10

    val learned: Array[Element[Boolean]] =
    Array.fill(length)(Constant(false))

    val score: Array[Element[Boolean]] =
    Array.fill(length)(Constant(false))

    learned(0) = Flip(0.6)
    for { chapter <- 1 until length }
    {
      learned(chapter) = If(learned(chapter - 1), Flip(0.6), Flip(0.3))
    } // 

    for { chapter <- 0 until length }
    {
      score(chapter) = If(learned(chapter), Flip(0.7), Flip(0.3))
    } //

    score(0).observe(true)
    score(1).observe(true)
    score(2).observe(true)
    println("Probability that the student will pass the last test successfull: " + VariableElimination.probability(score(9), true))
  }
}
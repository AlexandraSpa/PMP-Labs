package Lab8

import com.cra.figaro.library.atomic.continuous
import com.cra.figaro.library.atomic.discrete._
import com.cra.figaro.library.collection.Container
import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.language._
import com.cra.figaro.library.compound._

object Ex2 {
  def main(args: Array[String]) {
    val length = 10

    val learned: Array[Element[Boolean]] =
    Array.fill(length)(Constant(false))

    val score: Array[Element[Int]] =
    Array.fill(length)(FromRange(1, 11))

    val test: Array[Element[Boolean]] =
    Array.fill(length)(Constant(false))

    learned(0) = Flip(0.6)
    for { chapter <- 1 until length }
    {
      learned(chapter) = If(learned(chapter - 1), Flip(0.6), Flip(0.3))
    } // 

    for { chapter <- 0 until length }
    {
      score(chapter) = If(learned(chapter), FromRange(5, 11), FromRange(1, 5))
    } //

    for { chapter <- 0 until length }
    {
       test(chapter) = Apply(score(chapter),
        (s: Int) => 
        if(s > 4) true
        else false
      )
       //If((score(chapter) > 4), Constant(true), Constant(false))
    } 

    score(0).observe(6)
    score(1).observe(7)
    score(2).observe(8)
    println("Probability that the student will pass the last test successfull: " + VariableElimination.probability(test(9), true))
    score(8).observe(4)
    println("Probability that the student will pass the last test successfull, given the score of test no. 9 = 4: " + VariableElimination.probability(test(9), true))
  }
}
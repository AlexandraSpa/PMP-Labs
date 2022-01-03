package Lab10

import com.cra.figaro.language._
import com.cra.figaro.library.compound.{If, CPD, RichCPD, OneOf, *, ^^}
import com.cra.figaro.library.atomic.continuous.Normal
import com.cra.figaro.algorithm.sampling.Importance
import com.cra.figaro.algorithm.filtering.ParticleFilter

object Transition {
  def main(args: Array[String]) {


    val months = 13
    val fraction = 0.3

    var investment: Array[Element[Double]] = Array.fill(months)(Constant(0.0))
    var profit: Array[Element[Double]] = Array.fill(months)(Constant(0.0))
    var capital: Array[Element[Double]] = Array.fill(months)(Constant(0.0))

    capital(0) = Constant(1200.0)

    def transition(investment: Double, profit: Double, capital: Double):
    (Element[(Double, Double, Double)]) = {
      val fraction = 0.3

      val newInvestment = Apply(Constant(capital), (cap: Double) => cap * fraction) // investeste cu 30 % din capitalul anterior
      
      val newProfit = Chain(newInvestment, Constant(capital), (inv: Double, cap: Double) =>
        if (inv >= 0.5 * cap) Select(0.1 -> (0.4 * cap), 0.3 -> (0.5 * cap), 0.6 -> (0.7 * cap)); // daca investitia este mai mare de 50 % din capital atunci va avea un profit de 70 % din capital cu probabilitatea de 0.6
        else if (inv >= 0.3 * cap) Select(0.2 -> (0.25 * cap), 0.6 -> (0.5 * cap), 0.2 -> (0.35 * cap)); // daca investitia este mai mare de 30 % din capital atunci va avea un profit de 50 % din capital cu probabilitatea de 0.6
        else Select(0.6 -> (0.3 * cap), 0.3 -> (0.2 * cap), 0.1 -> (0.1 * cap))) // daca investitia este mai mica de 30 % din capital atunci va avea un profit de 30 % din capital cu probabilitatea de 0.6
      
      val newCapital = Apply(newProfit, Constant(capital), newInvestment,
        (prof: Double, cap: Double, invest: Double) => cap + prof - invest)
      
      ^^(newInvestment, newProfit, newCapital)
    } 

    for { month <- 1 until months } {
      val newState = 
        Chain(^^(investment(month - 1), profit(month - 1), capital(month - 1)), (transition _).tupled)
      investment(month) = newState._1
      profit(month) = newState._2
      capital(month) = newState._3
    }

    println(Importance.probability(capital(10), (c: Double) => c > 1200.0))
  }
} 
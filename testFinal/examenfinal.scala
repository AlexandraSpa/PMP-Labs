package examen
import com.cra.figaro.language._
import com.cra.figaro.library.compound._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.library.atomic.discrete._
import com.cra.figaro.library.atomic.continuous
import com.cra.figaro.library.collection.Container
import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.language.{Select, Apply, Constant, Element, Chain, Universe}
import com.cra.figaro.algorithm.filtering.ParticleFilter
import com.cra.figaro.algorithm.factored._

object Grammy {

  class Autor {
    val popular = Flip(1.0/6.0)
  }

  class Album(val aut: Autor) {
    val calitate = Select(0.27 -> 'mica, 0.6 -> 'medie, 0.13 -> 'mare)      
  }
  class Nominalizare(val alb: Album) {
    val nominalizat : Element[Boolean]
    def isNominated: (Element[Boolean]) = {
      CPD( alb.calitate, alb.aut
      ('mica, false) -> Flip(0.003),
      ('mica, true) -> Flip(0.014),
      ('medie, false) -> Flip(0.016),
      ('medie, true) -> Flip( 0.043),
      ('mare, false) -> Flip(0.047),
      ('mare, true) -> Flip( 0.18))
    }
    val nominalizat = isNominated()
  }


  def main(args: Array[String]) {
    val autori = Array.fill(5)(Autor())
    val roll2 = Chain(numberOfSides2, ((i: Int) => FromRange(1, i + 1)))
    val albume = Array.fill(10)(Album(autori(1)))
    val nominalizari = Array.fil(10)(Nominalizare(albume(1)))

    for {iter <- 1 until 10} {
      val iter2 = FromRange(1, 6)
      albume(iter).aut = autori(iter2)
      nominalizari(iter).alb = albume(iter)
    }
    val alg = MetropolisHastings(100000, ProposalScheme.default, albume(1).nominalizat) alg.start() 
    println(alg.mean( albume(1).nominalizat))



  }
}
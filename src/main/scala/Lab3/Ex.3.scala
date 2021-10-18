import com.cra.figaro.algorithm.factored._
import com.cra.figaro.language._
import com.cra.figaro.library.compound._
import com.cra.figaro.library.compound.If


object Ex3
{
    Universe.createNew()
    val x = Flip(0.4)
    val y = Flip(0.4)
    val z = x
    val w = x === z
    
    val z2 = y
    val w2 = x === z2
    

    def main(args: Array[String])
    {
        println(VariableElimination.probability(w, true))
        /*
        * in program, z e definit ca fiind x, deci w va fi intotdeauna adevarat
        */
        println(VariableElimination.probability(w2, true))
        /*
        * in program, z2 e definit ca fiind y, deci w va fi adevarat doar daca x si y vor fi amandoua adevarate, respectiv amandoua false
          Facand un calcul, prob(w, true) = 0.4*0.4 + 0.6*0.6 = 0.52
        */
    }
}
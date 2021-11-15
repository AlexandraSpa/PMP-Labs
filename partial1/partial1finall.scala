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
        if(s == 7 || s == 11) "Player one won"
        else if(s == 2 || s == 3 || s == 12) "Player two won"
        else "Both won"
    )

    play(p1Wins: Element[Int], p2Wins: Element[Int], no: Int) : Element[Int] =
    {
        val newP1Wins = If(winner == "Player one won" || winner == "Both won", (p1Wins + 1), p1Wins)
        val newP2Wins = If(winner == "Player two won" || winner == "Both won", (p2Wins + 1), p2Wins)
        val p1WinsGame = Apply(newP1Points, newP2Points,
            (p1: Int, p2: Int) =>
            p1 >= 4 && p1 - p2 >= 2)
        val newno = no - 1
        If(newno == 0, theWinner, play(newP1Wins, newP2Wins, newno))
    }


    def main(args: Array[String])
    {

        val alg = VariableElimination(winner)
        alg.start()
        println("Probability of player one to win: " + alg.probability(winner, "Player one won"))
        println("Probability of player two to win: " + alg.probability(winner, "Player two won"))
        /*
        * 4. In urma rularii s a obtinut urmatorul rezultat: 
        * Probability of player one to win: 0.2222222222222222
        * Probability of player two to win: 0.1111111111111111
        * Jucatorul 1 are mai multe sanse de castig deoarece 
        * sumele 7 si 11 au mai multe combinatii posibile decat sumele aferente castigului celui de al doilea jucator.
        */

        alg.kill()
    }
}
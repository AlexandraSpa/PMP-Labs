package Lab4

import com.cra.figaro.library.atomic.discrete
import com.cra.figaro.language.Chain
import com.cra.figaro.library.compound.{RichCPD, OneOf, *}
import com.cra.figaro.language.{Flip, Constant, Apply}
import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.library.atomic.discrete.FromRange
import com.cra.figaro.library.compound.^^

object ex4modified {
    def main(args: Array[String]) {
        // To keep the code simple, I just make the cards an integer
        val cards = List(5, 4, 3, 2, 1)
        // The discrete uniform distribution chooses uniformly from a fixed
        // set of possibilities
        val player1Card = discrete.Uniform(cards:_*)
        val cards1 = cards.filter(_ != player1Card)
        val player1Card2 = discrete.Uniform(cards1:_*)
        val cards2 = cards1.filter(_ != player1Card2)
        val player2Card = discrete.Uniform(cards2:_*)
        val cards3 = cards2.filter(_ != player2Card)
        val player2Card2 = discrete.Uniform(cards3:_*)
        
        val player1Bet1 = RichCPD(player1Card, player1Card2, 
            // Player 1 is more likely to bet with a higher card,
            // but will sometimes bet with a lower card to bluff
            (OneOf(5, 4, 3), OneOf(5, 4, 3)) -> Flip(0.9),
            (*, OneOf(5,4,3)) -> Flip(0.8),
            (OneOf(5,4,3), *) -> Flip(0.8),
            (*,*) -> Flip(0.4)
        )
        
        val player2Bet = RichCPD(player2Card, player2Card2, player1Bet1,
            (OneOf(5, 4), OneOf(5, 4), *) -> Flip(1),
            (OneOf(5, 4), *, *) -> Flip(0.9),
            (*, OneOf(5, 4), *) -> Flip(0.9),
            (*,*, OneOf(false)) -> Flip(0.5),
            (*, *, *) -> Flip(0.1)
        )
        
        val player1Bet2 =
        Apply(player1Card, player1Card2, player1Bet1, player2Bet,
            (card: Int, card2: Int, bet11: Boolean, bet2: Boolean) =>
            // Player 1’s second bet is only relevant if she passed the
            // first time and player 2 bet
            !bet11 && bet2 && (card == 5 || card == 4 || card2 == 5 || card2 == 4)
        
        )
        // This element represents the gain to player 1 from the game. I have
        // made it an Element[Double] so I can query its mean.
        val player1Gain = Apply(^^(player1Card, player1Card2, player2Card, player2Card2), player1Bet1, player2Bet, player1Bet2,
            (pcards: (Int, Int, Int, Int), bet11: Boolean, bet2: Boolean, bet12: Boolean) =>
                if (!bet11 && !bet2) 0.0
                else if (bet11 && !bet2) 1.0
                else if (!bet11 && bet2 && !bet12) -1.0
                else if ((pcards._1 > pcards._3) && (pcards._1 > pcards._4) || (pcards._2 > pcards._3) && (pcards._2 > pcards._4)) 2.0
                else -2.0
        )

        player1Card.observe(4)
        player1Card2.observe(5)
        player1Bet1.observe(true)
        val alg1 = VariableElimination(player1Gain)
        alg1.start()
        alg1.stop()
        println("Expected gain for betting:" + alg1.mean(player1Gain))
    }
}
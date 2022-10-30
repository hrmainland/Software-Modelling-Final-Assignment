package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.Optional;

/**
 * class that contains the logic for checking if diamonds are ever placed on hears
 */
public class DiamondOnHeartRule implements GameRule {
    public DiamondOnHeartRule() {
    }

    /**
     * Checks to see if the current card-pile combination is valid given the rule represented.
     * Throws BrokeRuleException if invalid
     * @param card card in question
     * @param pile pile in question
     * @throws BrokeRuleException
     */
    @Override
    public void isValid(Optional<Card> card, Hand pile) throws BrokeRuleException {
        GameOfThrones.Suit cardSuit = (GameOfThrones.Suit) card.get().getSuit();
        if (pile.getNumberOfCards() == 1 && cardSuit.isMagic()){
            throw new BrokeRuleException("A magic card cannot be played on a character card");
        }
    }
}

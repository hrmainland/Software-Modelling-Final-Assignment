package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.Optional;

/**
 * method that implements that the first two cards must be hearts
 */
public class FirstTwoCharacterRule implements GameRule{
    /**
     * Checks to see if the current card-pile combination is valid given the rule represented.
     * Throws BrokeRuleException if invalid
     * @param card card in question
     * @param pile pile in question
     * @throws BrokeRuleException
     */
    @Override
    public void isValid(Optional<Card> card, Hand pile) throws BrokeRuleException {
        if (card.isEmpty()){return;}
        GameOfThrones.Suit cardSuit = (GameOfThrones.Suit) card.get().getSuit();
        if (pile.getNumberOfCards() == 0 && !cardSuit.isCharacter()){
            throw new BrokeRuleException("The first card played on each pile must be a character card");
        }
    }
}

package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.Optional;

/**
 * method that implements that the first two cards must be hearts
 */
public class FirstTwoCharacterRule implements GameRule{
    /**
     * interface method for rule checking
     * @param card card that is being played
     * @param pile pile that is being played to
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

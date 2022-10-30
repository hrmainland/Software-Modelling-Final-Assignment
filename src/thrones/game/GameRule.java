package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.Optional;

/**
 * interface for the gameRules to allow for the strategy pattern
 */
public interface GameRule {
    /**
     * interface method that checks if the play is valid
     * @param card
     * @param pile
     * @throws BrokeRuleException
     */
    void isValid(Optional<Card> card, Hand pile) throws BrokeRuleException;
}

package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.Optional;

public interface GameRule {
    void isValid(Optional<Card> card, Hand pile) throws BrokeRuleException;
}

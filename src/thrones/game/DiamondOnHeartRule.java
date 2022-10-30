package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.Optional;

public class DiamondOnHeartRule implements GameRule {
    public DiamondOnHeartRule() {
    }

    @Override
    public void isValid(Optional<Card> card, Hand pile) throws BrokeRuleException {
        GameOfThrones.Suit cardSuit = (GameOfThrones.Suit) card.get().getSuit();
        if (pile.getNumberOfCards() == 1 && cardSuit.isMagic()){
            throw new BrokeRuleException("A magic card cannot be played on a character card");
        }
    }
}

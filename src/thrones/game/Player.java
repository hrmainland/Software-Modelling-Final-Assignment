package thrones.game;
import ch.aplu.jcardgame.*;

import java.util.Optional;

/**
 * interface that allows GameOfThrones to call universal methods on the players
 */
public interface Player {
    void updateState(Hand hand, Hand[] piles, boolean newRound);
    Optional<Card> getBestCard();
    int getPile();
}

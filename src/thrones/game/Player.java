package thrones.game;
import ch.aplu.jcardgame.*;

import java.util.ArrayList;
import java.util.Optional;

public interface Player {
    void updateState(Hand hand, Hand[] piles, boolean newRound);
    Optional<Card> getBestCard();
    int getPile();
}

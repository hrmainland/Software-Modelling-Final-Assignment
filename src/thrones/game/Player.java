package thrones.game;
import ch.aplu.jcardgame.*;

import java.util.Optional;

public interface Player {
    public void updateState(Hand hand, Hand[] piles);
    public Optional<Card> getBestCard();
    public int getPile();
}

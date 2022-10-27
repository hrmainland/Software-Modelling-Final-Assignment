package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.Optional;

public class SmartPlayer implements Player {
    private final int playerIndex;
    private Optional<Card> bestCard;
    private int pile;

    public SmartPlayer(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    @Override
    public void updateState(Hand hand, Hand[] piles) {
//
    }

    @Override
    public Optional<Card> getBestCard() {
        return Optional.empty();
    }

    @Override
    public int getPile() {
        return 0;
    }
}

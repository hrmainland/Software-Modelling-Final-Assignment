package thrones.game;

import ch.aplu.jcardgame.*;

import java.util.Optional;

public class HumanPlayer implements Player{
    private Card bestCard;
    private int pile;

    public HumanPlayer() {

    }

    @Override
    public void updateState(Hand hand, Hand[] piles) {
        for (int i = 0; i < hand.getCardList().size(); i++) {
            Card card = hand.getCardList().get(i);
            System.out.println(card);
        }
    }

    @Override
    public Optional<Card> getBestCard() {
        return Optional.empty();
    }

    @Override
    public int getPile() {
        return 1;
    }
}

package thrones.game;

import ch.aplu.jcardgame.*;

import java.util.Optional;

public class HumanPlayer implements Player{
    private Optional<Card> bestCard;
    private int pile;
    private Hand hand;
    private int playerIndex;
    private boolean firstTwoCards = true;

    public HumanPlayer(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    @Override
    public void updateState(Hand hand, Hand[] piles) {
        this.hand = hand;
        if (getTotalCardsPlayed(piles) < 2){
            firstTwoCards = true;
        }
        firstTwoCards = false;
    }

    @Override
    public Optional<Card> getBestCard() {
        if (hand.isEmpty()) {
            bestCard = Optional.empty();
        } else {
            bestCard = null;
            hand.setTouchEnabled(true);
            do {
                if (bestCard == null) {
                    ch.aplu.jgamegrid.Actor.delay(100);
                    continue;
                }
                GameOfThrones.Suit suit = bestCard.isPresent() ? (GameOfThrones.Suit) bestCard.get().getSuit() : null;
                if (firstTwoCards && suit != null && suit.isCharacter() ||         // If we want character, can't pass and suit must be right
                        !firstTwoCards && (suit == null || !suit.isCharacter())) { // If we don't want character, can pass or suit must not be character
                    // if (suit != null && suit.isCharacter() == isCharacter) {
                    break;
                } else {
                    bestCard = null;
                    hand.setTouchEnabled(true);
                }
                ch.aplu.jgamegrid.Actor.delay(100);
            } while (true);
        }
        return bestCard;
    }

    public int getTotalCardsPlayed(Hand[] piles){
        int totalCardsPlayed = 0;
        for (Hand pile: piles) {
            totalCardsPlayed += pile.getCardList().size();
        }
        return totalCardsPlayed;
    }

    @Override
    public int getPile() {
        return 1;
    }
}

package thrones.game;

import ch.aplu.jcardgame.*;

import java.util.Optional;

/**
 * class that contains all the logic for the human player
 */

public class HumanPlayer implements Player{
    private final int NON_SELECTION_VALUE = -1;
    private Optional<Card> bestCard;
    private int pile = NON_SELECTION_VALUE;
    private Hand[] piles;
    private Hand hand;
    private int playerIndex;
    private boolean firstTwoCards = true;

    public HumanPlayer(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    // mehtod that processes all the input from the user
    private void setupListener(Hand hand, Hand[] piles){
        // Set up human player for interaction
        hand.addCardListener(new CardAdapter() {
            public void leftDoubleClicked(Card card) {
                bestCard = Optional.of(card);
                hand.setTouchEnabled(false);
            }
            public void rightClicked(Card card) {
                bestCard = Optional.empty(); // Don't care which card we right-clicked for player to pass
                hand.setTouchEnabled(false);
            }
        });

        for (int i = 0; i < piles.length; i++) {
            final Hand currentPile = piles[i];
            final int pileIndex = i;
            piles[i].addCardListener(new CardAdapter() {
                public void leftClicked(Card card) {
                    pile = pileIndex;
                    currentPile.setTouchEnabled(false);
                }
            });
        }
    }

    // method that resets the class
    @Override
    public void updateState(Hand hand, Hand[] piles, boolean newRound) {
        this.hand = hand;
        this.piles = piles;
        if (newRound){
            setupListener(hand, piles);
        }
        firstTwoCards = getTotalCardsPlayed(piles) < 2;
    }

    // logic behind selecting the card to play
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

    // method that determines the total number of cards played
    public int getTotalCardsPlayed(Hand[] piles){
        int totalCardsPlayed = 0;
        for (Hand pile: piles) {
            totalCardsPlayed += pile.getCardList().size();
        }
        return totalCardsPlayed;
    }

    // method to return the pile that the human wants to add to
    @Override
    public int getPile() {
        if (bestCard.isPresent()) {
            GameOfThrones.Suit bestCardSuit = (GameOfThrones.Suit) bestCard.get().getSuit();
            if (bestCardSuit.isCharacter()) {
                pile = playerIndex % 2;
                return pile;
            }
        }
        pile = NON_SELECTION_VALUE;
        for (Hand pile : piles) {
            pile.setTouchEnabled(true);
        }
        while(pile == NON_SELECTION_VALUE) {
            ch.aplu.jgamegrid.Actor.delay(100);
        }
        for (Hand pile : piles) {
            pile.setTouchEnabled(false);
        }
        return pile;
    }
}
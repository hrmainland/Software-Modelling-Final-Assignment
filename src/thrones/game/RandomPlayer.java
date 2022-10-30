package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * class that contains all the logic of randomPlayer
 */
public class RandomPlayer implements Player{
    private final int playerIndex;
    private Optional<Card> bestCard;
    private int pile;

    public RandomPlayer(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    /**
     * Driver code for updating best card and pile given the current state of the game.
     * @param hand current hand
     * @param piles current piles
     * @param newRound true if start of new round, false otherwise
     */
    @Override
    public void updateState(Hand hand, Hand[] piles, boolean newRound) {
//        play heart if one of first two cards
        int totalCardsPlayed = getTotalCardsPlayed(piles);
//        play card (is character depends on total cards played)
        updateBestCard(hand, totalCardsPlayed < 2);
        setPile(totalCardsPlayed);
        if (totalCardsPlayed >= 2) {
            validateMove(piles);
        }
    }

    /**
     * Returns total number of cards on current piles
     * @param piles piles to be checked
     * @return total no. of cards
     */
    public int getTotalCardsPlayed(Hand[] piles){
        int totalCardsPlayed = 0;
        for (Hand pile: piles) {
            totalCardsPlayed += pile.getCardList().size();
        }
        return totalCardsPlayed;
    }

    @Override
    public Optional<Card> getBestCard() {
        return bestCard;
    }

    @Override
    public int getPile() {
        return pile;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setBestCard(Optional<Card> bestCard) {
        this.bestCard = bestCard;
    }

    /**
     * Selects a random card (implementation consistent with original design)
     * @param hand current hand
     * @param isCharacter true if character must be played, false otherwise
     */
    private void updateBestCard(Hand hand, boolean isCharacter) {
        List<Card> shortListCards = new ArrayList<>();
        for (int i = 0; i < hand.getCardList().size(); i++) {
            Card card = hand.getCardList().get(i);
            GameOfThrones.Suit suit = (GameOfThrones.Suit) card.getSuit();
//            filter out character or non-character cards
            if (suit.isCharacter() == isCharacter) {
                shortListCards.add(card);
            }
        }
//        skip 0.25 of turns or if there are no valid cards
        if (shortListCards.isEmpty() || !isCharacter && GameOfThrones.getRandom().nextInt(3) == 0) {
            bestCard = Optional.empty();
//        select random card
        } else {
            bestCard = Optional.of(shortListCards.get(GameOfThrones.getRandom().nextInt(shortListCards.size())));
        }
    }

    /**
     * Sets the pile of the card to be played to a random pile
     * @param totalCardsPlayed total number of cards played in the current round
     */
    private void setPile(int totalCardsPlayed) {
        if (totalCardsPlayed < 2){
            pile = playerIndex % 2;
            return;
        }
        pile = GameOfThrones.getRandom().nextInt(2);
    }

    /**
     * Determines if the current best card and pile violate any rule and nullifies the best card if so
     * @param piles current piles
     */
    private void validateMove(Hand[] piles){
        if (bestCard.isPresent()){
            ArrayList<Card> pileCards = piles[pile].getCardList();
            GameOfThrones.Suit topSuit = (GameOfThrones.Suit) pileCards.get(pileCards.size() - 1).getSuit();
            GameOfThrones.Suit cardSuit = (GameOfThrones.Suit) bestCard.get().getSuit();
            if (topSuit.isCharacter() && cardSuit.isMagic()) {
                bestCard = Optional.empty();
            }
        }
    }

}

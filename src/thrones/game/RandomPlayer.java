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

    // universal method to update what the player would do
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

    // method to determine how many plays have been made
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

    // contains all the logic to get the card that the random player wants to play
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

    private void setPile(int totalCardsPlayed) {
        if (totalCardsPlayed < 2){
            pile = playerIndex % 2;
            return;
        }
        pile = GameOfThrones.getRandom().nextInt(2);
    }

    // determines if we have a legal move
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

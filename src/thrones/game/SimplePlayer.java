package thrones.game;

import ch.aplu.jcardgame.Hand;

import java.util.Optional;

/**
 * Player class that extends RandomPlayer and contains all the logic
 * of simple player.
 */
public class SimplePlayer extends RandomPlayer{
    private final int myPile;

    public SimplePlayer(int playerIndex) {
        super(playerIndex);
        myPile = super.getPlayerIndex() % 2;
    }

    /**
     * Driver code for updating best card and pile given the current state of the game.
     * @param hand current hand
     * @param piles current piles
     * @param newRound true if start of new round, false otherwise
     */
    public void updateState(Hand hand, Hand[] piles, boolean newRound) {
        super.updateState(hand, piles, newRound);
        if (super.getTotalCardsPlayed(piles) >=2) {
            dontHelpOpponent();
        }
    }

    /**
     * Checks if the chosen move helps the opponent and nullifies the chosen card if so
     */
    private void dontHelpOpponent(){
        if (super.getBestCard().isPresent()){
            GameOfThrones.Suit cardSuit = (GameOfThrones.Suit) super.getBestCard().get().getSuit();

            if ((cardSuit.isDefence() || cardSuit.isAttack()) && super.getPile() != myPile){
                super.setBestCard(Optional.empty());
            }
            if (cardSuit.isMagic() && super.getPile() == myPile){
                super.setBestCard(Optional.empty());
            }
        }
    }
}

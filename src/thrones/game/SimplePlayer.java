package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Optional;

/**
 * child class that extends the random player and contains all the logic
 * of simple player
 */
public class SimplePlayer extends RandomPlayer{
    private int myPile;
    //private final Hand[] default_pile = new Hand[1];

    public SimplePlayer(int playerIndex) {
        super(playerIndex);
        myPile = super.getPlayerIndex() % 2;
    }

    // adds simple player logic
    public void updateState(Hand hand, Hand[] piles, boolean newRound) {
        super.updateState(hand, piles, newRound);
        if (super.getTotalCardsPlayed(piles) >=2) {
            dontHelpOpponent();
        }
    }

    // function to calculate if the best move helps the opposition
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

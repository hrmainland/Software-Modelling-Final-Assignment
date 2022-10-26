package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Optional;

public class SimplePlayer extends RandomPlayer{
    private int myPile;

    public SimplePlayer(int playerIndex) {
        super(playerIndex);
        myPile = super.getPlayerIndex() % 2;
    }
    public void updateState(Hand hand, Hand[] piles) {
        super.updateState(hand, piles);
        if (super.getTotalCardsPlayed(piles) >=2) {
            dontHelpOpponent();
        }
    }

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

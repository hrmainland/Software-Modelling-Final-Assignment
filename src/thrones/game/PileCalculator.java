package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

/**
 * (Pure Fabrication) Responsible for updating the ranks of each pile in the game
 */


public class PileCalculator {
    //toAdd:
    private static PileCalculator instance;
    private PileCalculator() {
    }

    public static PileCalculator getInstance() {
        if (instance == null) {
            instance = new PileCalculator();
        }
        return instance;
    }

    /**
     * method that loops through and returns the pile ranks in an array
     */
    public ArrayList<int[]> updatePileRanks(Hand[] piles) {
        ArrayList<int[]> allRanks = new ArrayList<>();
        for (int j = 0; j < piles.length; j++) {
            int[] ranks = calculatePileRanks(j, piles);
            allRanks.add(ranks);
        }
        return allRanks;
    }

    /**
     * loops through the cards given and determins the attack and defence rank
     * @param cards an arraylist of the cards in the pile
     * @return attack and defence ranks
     */
    public int[] pileRanksByList(ArrayList<Card> cards){
        int attack = 0;
        int defence = 0;
        Card card;
        Card prevCard;
        GameOfThrones.Suit prevSuit = null;
        GameOfThrones.Suit suit;
        int rankValue = 0;
        int prevRankValue = 0;
        if (cards.size() == 0) {
            return new int[]{attack, defence};
        }
        for (int i=0; i<cards.size(); i++) {
            if (i!=0) {
                prevCard = cards.get(i-1);
                prevSuit = ((GameOfThrones.Suit) prevCard.getSuit());
                prevRankValue = ((GameOfThrones.Rank) prevCard.getRank()).getRankValue();
            }
            card = cards.get(i);
            rankValue = ((GameOfThrones.Rank) card.getRank()).getRankValue();
            // If ranks are the same, double the effect.
            rankValue = rankValue==prevRankValue ? 2*rankValue : rankValue;
            suit = ((GameOfThrones.Suit) card.getSuit());
            if (suit.isCharacter()) {
                attack += rankValue;
                defence += rankValue;
            } else if (suit.isAttack()) {
                attack += rankValue;
            } else if (suit.isDefence()) {
                defence += rankValue;
            } else if (suit.isMagic()) {
                // Diamond should never be the bottom card
                assert(i!=0);
                // Diamond cannot be played after a heart
                assert(!prevSuit.isCharacter());
                int m = 2;
                while(prevSuit.isMagic()){
                    prevCard = cards.get(i-m);
                    prevSuit = ((GameOfThrones.Suit) prevCard.getSuit());
                    m += 1;
                }
                if (prevSuit.isAttack()) {
                    attack -= rankValue;
                } else {
                    defence -= rankValue;
                }
            }
        }
        if (attack < 0) {
            attack = 0;
        } if (defence < 0) {
            defence = 0;
        }
        return new int[]{attack, defence};
    }

    /**
     * calculates the pileRanks for the gameOfThrones class
     * @param pileIndex which pile it wants calculated
     * @param piles the piles from the game
     * @return the attack and defence ranks of the pile
     */
    public int[] calculatePileRanks(int pileIndex, Hand[] piles) {
        Hand currentPile = piles[pileIndex];
        int attack = 0;
        int defence = 0;
        if (currentPile.isEmpty()) {
            return new int[]{attack,defence};
        } else {
            ArrayList<Card> cards = currentPile.getCardList();
            return pileRanksByList(cards);
        }
    }
}
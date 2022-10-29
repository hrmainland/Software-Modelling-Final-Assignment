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

    public ArrayList<int[]> updatePileRanks(Hand[] piles) {
        ArrayList<int[]> allRanks = new ArrayList<>();
        for (int j = 0; j < piles.length; j++) {
            int[] ranks = calculatePileRanks(j, piles);
            allRanks.add(ranks);
        }
        return allRanks;
    }

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
                if (prevSuit.isAttack()) {
                    attack -= rankValue;
                } else {
                    defence -= rankValue;
                }
            }
            if (attack < 0) {
                attack = 0;
            } if (defence < 0) {
                defence = 0;
            }
        }
        return new int[]{attack, defence};
    }

    public int[] calculatePileRanks(int pileIndex, Hand[] piles) {
        Hand currentPile = piles[pileIndex];
        int attack = 0;
        int defence = 0;
        if (currentPile.isEmpty()) {
            return new int[]{attack,defence};
        } else {
            ArrayList<Card> cards = currentPile.getCardList();
            Card card;
            Card prevCard;
            GameOfThrones.Suit prevSuit = null;
            GameOfThrones.Suit suit;
            int rankValue = 0;
            int prevRankValue = 0;
            for (int i=0; i<cards.size(); i++) {
                if (i!=0) {
                    prevCard = currentPile.get(i-1);
                    prevSuit = ((GameOfThrones.Suit) prevCard.getSuit());
                    prevRankValue = ((GameOfThrones.Rank) prevCard.getRank()).getRankValue();
                }
                card = currentPile.get(i);
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
                    if (prevSuit.isAttack()) {
                        attack -= rankValue;
                    } else {
                        defence -= rankValue;
                    }
                }
                if (attack < 0) {
                    attack = 0;
                } if (defence < 0) {
                    defence = 0;
                }
            }
        }
        return new int[]{attack, defence};
    }
}

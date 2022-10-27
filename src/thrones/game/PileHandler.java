package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.TextActor;

import java.awt.*;
import java.util.ArrayList;

/**
 * (Pure Fabrication) Responsible for updating the ranks of each pile in the game
 */


public class PileHandler {
    //toAdd:
    private Hand[] piles;
    private final int ATTACK_RANK_INDEX = 0;
    private final int DEFENCE_RANK_INDEX = 1;
    private Actor[] pileTextActors = {null, null};


    /*private void updatePileRanks() {
        for (int j = 0; j < piles.length; j++) {
            int[] ranks = calculatePileRanks(j);
            updatePileRankState(j, ranks[ATTACK_RANK_INDEX], ranks[DEFENCE_RANK_INDEX]);
        }
    }*/

    private int[] calculatePileRanks(int pileIndex) {
        Hand currentPile = piles[pileIndex];
        int attack = 0;
        int defence = 0;
        //int i = currentPile.isEmpty() ? 0 : ((GameOfThrones.Rank) currentPile.get(0).getRank()).getRankValue();
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
            }
        }
        return new int[]{attack, defence};
    }
/*
    private void updatePileRankState(int pileIndex, int attackRank, int defenceRank) {
        TextActor currentPile = (TextActor) pileTextActors[pileIndex];
        removeActor(currentPile);
        String text = playerTeams[pileIndex] + " Attack: " + attackRank + " - Defence: " + defenceRank;
        pileTextActors[pileIndex] = new TextActor(text, Color.WHITE, bgColor, smallFont);
        addActor(pileTextActors[pileIndex], pileStatusLocations[pileIndex]);
    }

    private void resetPile() {
        if (piles != null) {
            for (Hand pile : piles) {
                pile.removeAll(true);
            }
        }
        piles = new Hand[2];
        for (int i = 0; i < 2; i++) {
            piles[i] = new Hand(deck);
            piles[i].setView(this, new RowLayout(pileLocations[i], 8 * pileWidth));
            piles[i].draw();
            final Hand currentPile = piles[i];
            final int pileIndex = i;
            piles[i].addCardListener(new CardAdapter() {
                public void leftClicked(Card card) {
                    selectedPileIndex = pileIndex;
                    currentPile.setTouchEnabled(false);
                }
            });
        }

        updatePileRanks();
    }

 */
}

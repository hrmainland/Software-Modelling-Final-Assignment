package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class SmartPlayer implements Player {
    private final int playerIndex;
    private Optional<Card> bestCard;
    private ArrayList<Card> magicCardsPlayed;
    private int pile;

    public SmartPlayer(int playerIndex) {
        this.playerIndex = playerIndex;
        magicCardsPlayed = new ArrayList<Card>();
    }

    @Override
    public void updateState(Hand hand, Hand[] piles) {
//        update magic cards played
        for (Hand pile : piles) {
            for (int i = 0; i < pile.getCardList().size(); i++) {
                Card card = pile.getCardList().get(i);
                GameOfThrones.Suit cardSuit = (GameOfThrones.Suit) card.getSuit();
                if (cardSuit.isMagic() && !magicCardsPlayed.contains(card)) {
                    magicCardsPlayed.add(card);
                }
            }
        }
//        get total cards played
        int totalCardsPlayed = getTotalCardsPlayed(piles);
        setCardAndPile(hand, piles,totalCardsPlayed < 2);
    }

    private void setCardAndPile(Hand hand, Hand[] piles, boolean isCharacter){
//        partition cards
        ArrayList<GameOfThrones.Suit> heartSuit = new ArrayList<>(Collections.singletonList(GameOfThrones.Suit.HEARTS));
        ArrayList<GameOfThrones.Suit> attackDefenceSuits = new ArrayList<>(Arrays.asList((GameOfThrones.Suit.SPADES),
                (GameOfThrones.Suit.CLUBS)));
        ArrayList<GameOfThrones.Suit> effectCardsSuits = new ArrayList<>(Arrays.asList((GameOfThrones.Suit.SPADES),
                (GameOfThrones.Suit.CLUBS), (GameOfThrones.Suit.DIAMONDS)));

        ArrayList<Card> hearts = getCardPartition(hand, heartSuit);
        ArrayList<Card> attackDefence = getCardPartition(hand, attackDefenceSuits);
        ArrayList<Card> effectCards = getCardPartition(hand, effectCardsSuits);

//        play heart if one of first two cards
        if (isCharacter){
            bestCard = Optional.of(hearts.get(GameOfThrones.random.nextInt(hearts.size())));
            pile = playerIndex % 2;
            return;
        }
//        check for attack/defence card with no matching diamond
//        get all ranks currently in played magic cards
        ArrayList<Integer> rankValuesPlayed = new ArrayList<>();
        int tenRanksPlayed = 0;
        for (Card playedCard : magicCardsPlayed) {
            GameOfThrones.Rank rank = (GameOfThrones.Rank) playedCard.getRank();
//            only add rank 10 if all 4 cards with that rank have been played
            int tenRankValue = GameOfThrones.Rank.TEN.getRankValue();
            int currentRankValue = rank.getRankValue();
            if (currentRankValue == tenRankValue) {
                tenRanksPlayed++;
                if (tenRanksPlayed == 4) {
                    rankValuesPlayed.add(rank.getRankValue());
                }
                continue;
            }
            rankValuesPlayed.add(rank.getRankValue());
        }
//        search all attack and defence cards in hand
        for (Card card : attackDefence) {
            GameOfThrones.Rank rank = (GameOfThrones.Rank) card.getRank();
            int rankValue = rank.getRankValue();
//            if corresponding diamond has already been played - play card
            if (rankValuesPlayed.contains(rankValue)) {
                bestCard = Optional.of(card);
//                play on your own pile as this is for attack and defence cards
                pile = playerIndex % 2;
                return;
            }
        }
        for (Card card : effectCards) {
            makeProspectivePiles(piles, card, hand);
        }
//        for each card check if playing would win game in current state
//        pass if neither of the above two conditions are met
    }

    @Override
    public Optional<Card> getBestCard() {
        return Optional.empty();
    }

    @Override
    public int getPile() {
        return 0;
    }

    public int getTotalCardsPlayed(Hand[] piles){
        int totalCardsPlayed = 0;
        for (Hand pile: piles) {
            totalCardsPlayed += pile.getCardList().size();
        }
        return totalCardsPlayed;
    }

    private ArrayList<Card> getCardPartition(Hand hand, ArrayList<GameOfThrones.Suit> suits){
        ArrayList<Card> partition = new ArrayList<>();
        for (int i = 0; i < hand.getCardList().size(); i++) {
            Card card = hand.getCardList().get(i);
            GameOfThrones.Suit suit = (GameOfThrones.Suit) card.getSuit();
            if (suits.contains(suit)){
                partition.add(card);
            }
        }
        return partition;
    }

    private void makeProspectivePiles(Hand[] piles, Card card, Hand hand){
        GameOfThrones.Suit suit = (GameOfThrones.Suit) card.getSuit();
        assert !suit.isCharacter() : "Heart cards should not be considered";
        if (suit.isMagic()) {
            card.transfer(piles[(playerIndex + 1) % 2], false);
        }
        else{
            card.transfer(piles[playerIndex % 2], false);
        }
//        calculate scores
        card.transfer(hand, false);
    }
}

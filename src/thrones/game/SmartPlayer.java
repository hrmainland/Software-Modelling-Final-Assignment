package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

/**
 * contains all the logic required for SmartPlayer
 */
public class SmartPlayer implements Player {
    private final int ATTACK_RANK_INDEX = 0;
    private final int DEFENCE_RANK_INDEX = 1;
    private final int ATTACK_WIN_INDEX = 0;
    private final int DEFENCE_WIN_INDEX = 1;


    private final int playerIndex;
    private Optional<Card> bestCard;
    private ArrayList<Card> magicCardsPlayed;
    private int pile;
    private PileCalculator pileCalculator = PileCalculator.getInstance();

    public SmartPlayer(int playerIndex) {
        this.playerIndex = playerIndex;
        magicCardsPlayed = new ArrayList<Card>();
    }

    /**
     * Driver code for updating best card and pile given the current state of the game.
     * @param hand current hand
     * @param piles current piles
     * @param newRound true if start of new round, false otherwise
     */
    @Override
    public void updateState(Hand hand, Hand[] piles, boolean newRound) {
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

    /**
     * Responsible for checking if a heart must be played or if either of the two
     * conditions for playing a card are met by any card. If any condition is met
     * both the card and pile that should be played are updated.
     * @param hand current hand
     * @param piles current piles
     * @param isCharacter true if a character card must be played, false otherwise
     */
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
            if (hearts.size() > 0) {
                bestCard = Optional.of(hearts.get(GameOfThrones.getRandom().nextInt(hearts.size())));
                pile = playerIndex % 2;
                return;
            }
            bestCard = Optional.empty();
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
//        If the above condition is not met check if any card would win the game if it were to end now
//        clone both the current piles
        ArrayList<Card> myPile = cloneHandAsList(piles[playerIndex % 2]);
        ArrayList<Card> oppPile = cloneHandAsList(piles[(playerIndex + 1)% 2]);
        boolean[] initialResults = attackDefenceResults(myPile, oppPile);
        if (initialResults[ATTACK_WIN_INDEX] && initialResults[DEFENCE_WIN_INDEX]){
            bestCard = Optional.empty();
            return;
        }
//        for each effect card add it to the cloned pile and test if it would win the game as it stands
        for (Card card : effectCards){
            myPile = cloneHandAsList(piles[playerIndex % 2]);
            oppPile = cloneHandAsList(piles[(playerIndex + 1)% 2]);
            GameOfThrones.Suit suit = (GameOfThrones.Suit) card.getSuit();
            assert (!suit.isCharacter()) : "Heart cards should not be considered for play";
            int pileIndex;
            if (suit.isMagic()){
                pileIndex = (playerIndex + 1) % 2;
                if (piles[pileIndex].getNumberOfCards() == 1){
                    continue;
                }
                oppPile.add(card);
            }
            else{
                pileIndex = playerIndex % 2;
                myPile.add(card);
            }
//            store the current card and pile if playing it would win the game this turn
            boolean[] currentResults = attackDefenceResults(myPile, oppPile);
            if (!initialResults[ATTACK_WIN_INDEX] && currentResults[ATTACK_WIN_INDEX]
                    || !initialResults[DEFENCE_WIN_INDEX] && currentResults[DEFENCE_WIN_INDEX]){
                bestCard = Optional.of(card);
                pile = pileIndex;
                return;
            }
        }
        bestCard = Optional.empty();
    }

    /**
     * Clones the hand passed as an ArrayList
     * @param hand hand to be cloned
     * @return ArrayList with objects of type Card
     */
    private ArrayList<Card> cloneHandAsList(Hand hand){
        ArrayList<Card> handClone = new ArrayList<>();
        for (Card card : hand.getCardList()){
            Card newCard = card.clone();
            handClone.add(newCard);
        }
        return handClone;
    }

    @Override
    public Optional<Card> getBestCard() {
        return bestCard;
    }

    @Override
    public int getPile() {
        return pile;
    }

    /**
     * Returns total number of cards on current piles
     * @param piles
     * @return total no. of cards
     */
    public int getTotalCardsPlayed(Hand[] piles){
        int totalCardsPlayed = 0;
        for (Hand pile: piles) {
            totalCardsPlayed += pile.getCardList().size();
        }
        return totalCardsPlayed;
    }

    /**
     * Returns a subsection of the hand passed filtered by an ArrayList of suits passed as a parameter.
     * @param hand hand to be filtered
     * @param suits ArrayList of suits to be filtered by
     * @return ArrayList of filtered cards
     */
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

    /**
     * Gets the result of both rounds of battle and returns these results as a boolean array
     * @param myPile pile of player in question
     * @param oppPile pile of opposition
     * @return boolean array indicating success in the attack and defence phase
     */
    private boolean[] attackDefenceResults(ArrayList<Card> myPile, ArrayList<Card> oppPile){
        int[] myResults = pileCalculator.pileRanksByList(myPile);
        int[] oppResults = pileCalculator.pileRanksByList(oppPile);
        boolean attackWin = myResults[ATTACK_RANK_INDEX] > oppResults[DEFENCE_RANK_INDEX];
        boolean defenceWin = myResults[DEFENCE_RANK_INDEX] >= oppResults[ATTACK_RANK_INDEX];
        return new boolean[]{attackWin, defenceWin};
    }

}

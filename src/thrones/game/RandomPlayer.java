package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RandomPlayer implements Player{
    private int playerIndex;
    private Optional<Card> bestCard;
    private int pile;

    public RandomPlayer(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    @Override
    public void updateState(Hand hand, Hand[] piles) {
//        play heart if one of first two cards
        int totalCardsPlayed = 0;
        for (Hand pile: piles) {
            totalCardsPlayed += pile.getCardList().size();
        }
//        play card (is character depends on total cards played)
        setBestCard(hand, totalCardsPlayed < 2);

        if (totalCardsPlayed < 2){
            pile = playerIndex % 2;
        }
        else{
            setPile();
        }
    }

    @Override
    public Optional<Card> getBestCard() {
        return bestCard;
    }

    @Override
    public int getPile() {
        return pile;
    }

    private void setBestCard(Hand hand, boolean isCharacter) {
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
        if (shortListCards.isEmpty() || !isCharacter && GameOfThrones.random.nextInt(3) == 0) {
            bestCard = Optional.empty();
//        select random card
        } else {
            bestCard = Optional.of(shortListCards.get(GameOfThrones.random.nextInt(shortListCards.size())));
        }
    }

    private void setPile() {
        pile = GameOfThrones.random.nextInt(2);
    }
}

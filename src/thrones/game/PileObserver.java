package thrones.game;

import ch.aplu.jgamegrid.TextActor;

public class PileObserver {
    GameOfThrones game;

    public PileObserver(GameOfThrones game) {
        this.game = game;
    }

    public void notifyPileToRemove(TextActor currentPile) {
        game.removeActor(currentPile);
    }

    public void updatePileRankState(int j, int rank, int rank1) {
        game.updatePileRankState(j, rank, rank1);
    }
}

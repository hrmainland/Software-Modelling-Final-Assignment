package thrones.game;

// class that controls all rendering of objects and acts as a facade between BattleHandler/PileHandler
// and the Game of Thrones class

import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

public class RenderingFacade {
    GameOfThrones game;
    private final Location[] handLocations = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };
    private final int handWidth = 400;
    public RenderingFacade(GameOfThrones game) {
        this.game = game;
    }

    public void renderhandLayouts(int nbPlayers, Hand[] hands){
        RowLayout[] layouts = new RowLayout[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90 * i);
            hands[i].setView(game, layouts[i]);
            hands[i].draw();
        }
    }

    public void updateScores(){
        game.updateScores();
    }

    public void printStartBattleInfo(int[] pile0Ranks, int[] pile1Ranks, Hand[] piles, int ATTACK_RANK_INDEX, int DEFENCE_RANK_INDEX){
        System.out.println("piles[0]: " + game.canonical((piles[0])));
        System.out.println("piles[0] is " + "Attack: " + pile0Ranks[ATTACK_RANK_INDEX] +
                " - Defence: " + pile0Ranks[DEFENCE_RANK_INDEX]);
        System.out.println("piles[1]: " + game.canonical(piles[1]));
        System.out.println("piles[1] is " + "Attack: " + pile1Ranks[ATTACK_RANK_INDEX] +
                " - Defence: " + pile1Ranks[DEFENCE_RANK_INDEX]);
    }

    public void setStatusText(String character0Result, String character1Result){
        game.setStatusText(character0Result + " " + character1Result);
    }

    public void notifyPileToRemove(TextActor currentPile) {
        game.removeActor(currentPile);
    }

    public void updatePileRankState(int j, int rank, int rank1) {
        game.updatePileRankState(j, rank, rank1);
    }
}

package thrones.game;

// class that controls all rendering of objects and acts as a facade between BattleHandler/PileHandler
// and the Game of Thrones class

import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

import java.awt.*;

public class GameRenderer {
    GameOfThrones game;
    private final Location[] handLocations = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };
    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(25, 25),
            new Location(575, 125)
    };
    private final Location[] pileLocations = {
            new Location(350, 280),
            new Location(350, 430)
    };
    private final Location[] pileStatusLocations = {
            new Location(250, 200),
            new Location(250, 520)
    };
    private final int pileWidth = 40;
    private Actor[] pileTextActors = {null, null};
    private Font bigFont = new Font("Arial", Font.BOLD, 36);
    private Font smallFont = new Font("Arial", Font.PLAIN, 10);
    private Actor[] scoreActors = {null, null, null, null};
    private final int handWidth = 400;

    public GameRenderer(GameOfThrones game) {
        this.game = game;
    }

    public void renderhandLayouts(int nbPlayers, Hand[] hands) {
        RowLayout[] layouts = new RowLayout[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90 * i);
            hands[i].setView(game, layouts[i]);
            hands[i].draw();
        }
    }

    public void printStartBattleInfo(int[] pile0Ranks, int[] pile1Ranks, Hand[] piles, int ATTACK_RANK_INDEX, int DEFENCE_RANK_INDEX) {
        System.out.println("piles[0]: " + game.canonical((piles[0])));
        System.out.println("piles[0] is " + "Attack: " + pile0Ranks[ATTACK_RANK_INDEX] +
                " - Defence: " + pile0Ranks[DEFENCE_RANK_INDEX]);
        System.out.println("piles[1]: " + game.canonical(piles[1]));
        System.out.println("piles[1] is " + "Attack: " + pile1Ranks[ATTACK_RANK_INDEX] +
                " - Defence: " + pile1Ranks[DEFENCE_RANK_INDEX]);
    }

    public void setStatusText(String character0Result, String character1Result) {
        game.setStatusText(character0Result + " " + character1Result);
    }

    public void renderScores(int i) {
        String text = "P" + i + "-0";
        scoreActors[i] = new TextActor(text, Color.WHITE, game.bgColor, bigFont);
        game.addActor(scoreActors[i], scoreLocations[i]);
    }

    public void reRenderScore(int[] scores, String[] playerTeams, int nbPlayers) {
        for (int i = 0; i < nbPlayers; i++) {
            game.removeActor(scoreActors[i]);
            String text = "P" + i + "-" + scores[i];
            scoreActors[i] = new TextActor(text, Color.WHITE, game.bgColor, bigFont);
            game.addActor(scoreActors[i], scoreLocations[i]);
        }
        System.out.println(playerTeams[0] + " score = " + scores[0] + "; " + playerTeams[1] + " score = " + scores[1]);
    }

    public void renderPileText() {
        String text = "Attack: 0 - Defence: 0";
        for (int i = 0; i < pileTextActors.length; i++) {
            pileTextActors[i] = new TextActor(text, Color.WHITE, game.bgColor, smallFont);
            game.addActor(pileTextActors[i], pileStatusLocations[i]);
        }
    }

    public void updatePileRankState(int pileIndex, int attackRank, int defenceRank, String[] playerTeams) {
        TextActor currentPile = (TextActor) pileTextActors[pileIndex];
        game.removeActor(currentPile);
        String text = playerTeams[pileIndex] + " Attack: " + attackRank + " - Defence: " + defenceRank;
        pileTextActors[pileIndex] = new TextActor(text, Color.WHITE, game.bgColor, smallFont);
        game.addActor(pileTextActors[pileIndex], pileStatusLocations[pileIndex]);
    }

    public void renderPile(Hand pile, int pileNumber){
        pile.setView(game, new RowLayout(pileLocations[pileNumber], 8 * pileWidth));
        pile.draw();
    }
}
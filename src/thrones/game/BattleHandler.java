package thrones.game;
import ch.aplu.jcardgame.Hand;

/**
 * Simple class that handles the battling of piles and prints results to the console
 */

public class BattleHandler {

    private final static int ATTACK_RANK_INDEX = 0;
    private final static int DEFENCE_RANK_INDEX = 1;
    private final static int CHARACTER_CARD = 0;
    private final static int PLAYER_ZERO = 0;
    private final static int PLAYER_ONE = 1;
    private RenderingFacade renderingFacade;

    //PileHandler pileHandler;

    public BattleHandler(RenderingFacade renderingFacade) {
        this.renderingFacade = renderingFacade;
        //this.pileHandler = pilehandler;
    }
    // Major method call, used to execute battle sequence
    public void battle(int[] pile0Ranks, int[] pile1Ranks, int[] scores, Hand[] piles) {
        printStartBattleInfo(pile0Ranks, pile1Ranks, piles);
        GameOfThrones.Rank pile0CharacterRank = (GameOfThrones.Rank) piles[PLAYER_ZERO].getCardList().get(CHARACTER_CARD).getRank();
        GameOfThrones.Rank pile1CharacterRank = (GameOfThrones.Rank) piles[PLAYER_ONE].getCardList().get(CHARACTER_CARD).getRank();
        String character0Result;
        String character1Result;

        if (pile0Ranks[ATTACK_RANK_INDEX] > pile1Ranks[DEFENCE_RANK_INDEX]) {
            scores[0] += pile1CharacterRank.getRankValue();
            scores[2] += pile1CharacterRank.getRankValue();
            character0Result = "Character 0 attack on character 1 succeeded.";
        } else {
            scores[1] += pile1CharacterRank.getRankValue();
            scores[3] += pile1CharacterRank.getRankValue();
            character0Result = "Character 0 attack on character 1 failed.";
        }

        if (pile1Ranks[ATTACK_RANK_INDEX] > pile0Ranks[DEFENCE_RANK_INDEX]) {
            scores[1] += pile0CharacterRank.getRankValue();
            scores[3] += pile0CharacterRank.getRankValue();
            character1Result = "Character 1 attack on character 0 succeeded.";
        } else {
            scores[0] += pile0CharacterRank.getRankValue();
            scores[2] += pile0CharacterRank.getRankValue();
            character1Result = "Character 1 attack character 0 failed.";
        }
        renderingFacade.updateScores();
        System.out.println(character0Result);
        System.out.println(character1Result);
        renderingFacade.setStatusText(character0Result, character1Result);
    }

    private void printStartBattleInfo(int[] pile0Ranks, int[] pile1Ranks, Hand[] piles) {
        renderingFacade.printStartBattleInfo(pile0Ranks, pile1Ranks,piles, ATTACK_RANK_INDEX, DEFENCE_RANK_INDEX);
    }

}

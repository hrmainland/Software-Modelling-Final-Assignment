package thrones.game;

/**
 * class that creates all instances of players
 */
public class PlayerFactory {

    public PlayerFactory() {
    }

    /**
     * Returns instance of Player given the string extracted from the properties file
     * @param playerType string indicating player type
     * @param playerIndex Index of the player being created
     * @return instance of Player
     */
    public Player getPlayer(String playerType, int playerIndex) {
        Player player;
        switch (playerType) {
            case "random":
                player = new RandomPlayer(playerIndex);
                break;
            case "simple":
                player = new SimplePlayer(playerIndex);
                break;
            case "smart":
                player = new SmartPlayer(playerIndex);
                break;
            default:
                player = new HumanPlayer(playerIndex);
                break;
        }
        return player;
    }
}

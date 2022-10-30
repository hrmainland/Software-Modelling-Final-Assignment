package thrones.game;

/**
 * class that creates all instances of players
 */
public class PlayerFactory {

    public PlayerFactory() {
    }

    // method that contains all the required logic to create the specific players
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

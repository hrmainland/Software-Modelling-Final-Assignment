package thrones.game;

// Oh_Heaven.java
import ch.aplu.jcardgame.*;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class GameOfThrones extends CardGame {

    private static int seed;
    private static Random random;
    private static int watchingTime;
    private final int NUM_PLAYERS = 4;
    private final int NUM_START_CARDS = 9;
    private final int NUM_PLAYS = 6;
    private final int NUM_ROUNDS = 3;
    private final int DECK_CARDS = 52;
    private final int ACE_COUNT = 4;
    private final int HEARTS_PP = 3;
    private final int NUM_PILES = 2;
    private final Deck DECK = new Deck(Suit.values(), Rank.values(), "cover");
    private final GameRenderer gameRenderer = new GameRenderer(this);
    private final PileCalculator pileCalculator = PileCalculator.getInstance();
    private final BattleHandler battleHandler = new BattleHandler();
    private Hand[] hands;
    private Hand[] piles;
    private final String[] playerTeams = { "[Players 0 & 2]", "[Players 1 & 3]"};
    private int nextStartingPlayer = random.nextInt(NUM_PLAYERS);
    private int[] scores = new int[NUM_PLAYERS];

    private Optional<Card> selected;
    private final int NON_SELECTION_VALUE = -1;
    private int selectedPileIndex = NON_SELECTION_VALUE;
    private final int UNDEFINED_INDEX = -1;
    private final int ATTACK_RANK_INDEX = 0;
    private final int DEFENCE_RANK_INDEX = 1;


    private static ArrayList<Player> playerList = new ArrayList<>();
    private static final PlayerFactory playerFactory = new PlayerFactory();

    enum GoTSuit { CHARACTER, DEFENCE, ATTACK, MAGIC }

    public enum Suit {
        SPADES(GoTSuit.DEFENCE),
        HEARTS(GoTSuit.CHARACTER),
        DIAMONDS(GoTSuit.MAGIC),
        CLUBS(GoTSuit.ATTACK);

        Suit(GoTSuit gotsuit) {
            this.gotsuit = gotsuit;
        }
        private final GoTSuit gotsuit;

        public boolean isDefence(){ return gotsuit == GoTSuit.DEFENCE; }

        public boolean isAttack(){ return gotsuit == GoTSuit.ATTACK; }

        public boolean isCharacter(){ return gotsuit == GoTSuit.CHARACTER; }

        public boolean isMagic(){ return gotsuit == GoTSuit.MAGIC; }
    }

    public enum Rank {
        // Reverse order of rank importance (see rankGreater() below)
        // Order of cards is tied to card images
        ACE(1),
        KING(10),
        QUEEN(10),
        JACK(10),
        TEN(10),
        NINE(9),
        EIGHT(8),
        SEVEN(7),
        SIX(6),
        FIVE(5),
        FOUR(4),
        THREE(3),
        TWO(2);
        Rank(int rankValue) {
            this.rankValue = rankValue;
        }
        private final int rankValue;
        public int getRankValue() {
            return rankValue;
        }
    }

    /*
    Canonical String representations of Suit, Rank, Card, and Hand
    */
    String canonical(Suit s) { return s.toString().substring(0, 1); }
    String canonical(Rank r) {
        switch (r) {
            case ACE: case KING: case QUEEN: case JACK: case TEN:
                return r.toString().substring(0, 1);
            default:
                return String.valueOf(r.getRankValue());
        }
    }
    String canonical(Card c) { return canonical((Rank) c.getRank()) + canonical((Suit) c.getSuit()); }
    String canonical(Hand h) {
        return "[" + h.getCardList().stream().map(this::canonical).collect(Collectors.joining(",")) + "]";
    }

    // Draws a random card from the Hand, based on a random number generator
    public static Card randomCard(Hand hand) {
        assert !hand.isEmpty() : " random card from empty hand.";
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }

    /*
     Deals out the entire pack of 52 cards without shuffling, removing aces,
     and ensuring each player gets 3 heart cards and 9 non-hearts
     */
    private void dealingOut(Hand[] hands) {
        Hand pack = DECK.toHand(false);
        assert pack.getNumberOfCards() == DECK_CARDS : " Starting pack is not 52 cards.";
        // Remove 4 Aces
        List<Card> aceCards = pack.getCardsWithRank(Rank.ACE);
        for (Card card : aceCards) {
            card.removeFromHand(false);
        }
        assert pack.getNumberOfCards() == DECK_CARDS - ACE_COUNT : " Pack without aces is not 48 cards.";
        // Give each player 3 heart cards
        for (int i = 0; i < NUM_PLAYERS; i++) {
            for (int j = 0; j < HEARTS_PP; j++) {
                List<Card> heartCards = pack.getCardsWithSuit(Suit.HEARTS);
                int x = random.nextInt(heartCards.size());
                Card randomCard = heartCards.get(x);
                randomCard.removeFromHand(false);
                hands[i].insert(randomCard, false);
            }
        }
        assert pack.getNumberOfCards() == DECK_CARDS - ACE_COUNT - (NUM_PLAYERS*HEARTS_PP) :
                " Pack without aces and hearts is not 36 cards.";
        // Give each player 9 of the remaining cards
        for (int i = 0; i < NUM_START_CARDS; i++) {
            for (int j = 0; j < NUM_PLAYERS; j++) {
                assert !pack.isEmpty() : " Pack has prematurely run out of cards.";
                Card dealt = randomCard(pack);
                dealt.removeFromHand(false);
                hands[j].insert(dealt, false);
            }
        }
        for (int j = 0; j < NUM_PLAYERS; j++) {
            assert hands[j].getNumberOfCards() == DECK_CARDS - ACE_COUNT - (NUM_PLAYERS*HEARTS_PP) -
                    (NUM_PLAYERS*NUM_START_CARDS) : " Hand does not have twelve cards.";
        }
    }

    private void initScore() {
        for (int i = 0; i < NUM_PLAYERS; i++) {
            scores[i] = 0;
            gameRenderer.renderScores(i);
        }
        gameRenderer.renderPileText();
    }

    private void setupGame() {

        // Initialise Hands
        hands = new Hand[NUM_PLAYERS];
        for (int i = 0; i < NUM_PLAYERS; i++) {
            hands[i] = new Hand(DECK);
        }
        dealingOut(hands);

        // Sort Hands by Suit
        for (int i = 0; i < NUM_PLAYERS; i++) {
            hands[i].sort(Hand.SortType.SUITPRIORITY, true);
            System.out.println("hands[" + i + "]: " + canonical(hands[i]));
        }

        // graphics
        gameRenderer.renderhandLayouts(NUM_PLAYERS, hands);
    }

    private int getPlayerIndex(int index) {
        return index % NUM_PLAYERS;
    }

    private void resetPile() {
        if (piles != null) {
            for (Hand pile : piles) {
                pile.removeAll(true);
            }
        }
        piles = new Hand[NUM_PILES];
        for (int i = 0; i < NUM_PILES; i++) {
            piles[i] = new Hand(DECK);
            gameRenderer.renderPile(piles[i], i);
        }

        rankUpdater(piles);
    }

    private void executeAPlay() throws BrokeRuleException {
        resetPile();
        updatePlayers(true);

        nextStartingPlayer = getPlayerIndex(nextStartingPlayer);
        if (hands[nextStartingPlayer].getNumberOfCardsWithSuit(Suit.HEARTS) == 0)
            nextStartingPlayer = getPlayerIndex(nextStartingPlayer + 1);
        assert hands[nextStartingPlayer].getNumberOfCardsWithSuit(Suit.HEARTS) != 0 : " Starting player has no hearts.";

        // 1: play the first 2 hearts
        int playerIndex;
        for (int i = 0; i < 2; i++) {
            playerIndex = getPlayerIndex(nextStartingPlayer + i);
            playHeartCard(playerIndex);
        }

        // 2: play the remaining NUM_PLAYERS * NUM_ROUNDS - 2
        int remainingTurns = NUM_PLAYERS * NUM_ROUNDS - 2;
        playerIndex = nextStartingPlayer + 2;

        while(remainingTurns > 0) {
            playerIndex = getPlayerIndex(playerIndex);
            playNonHeartCard(playerIndex);
            playerIndex++;
            remainingTurns--;
        }

        // 3: calculate winning & update scores for players
        rankUpdater(piles);
        int[] pile0Ranks = pileCalculator.calculatePileRanks(0, piles);
        int[] pile1Ranks = pileCalculator.calculatePileRanks(1, piles);
        gameRenderer.printStartBattleInfo(pile0Ranks, pile1Ranks,piles, ATTACK_RANK_INDEX, DEFENCE_RANK_INDEX);
        scores = battleHandler.battle(pile0Ranks, pile1Ranks, scores, piles);
        gameRenderer.reRenderScore(scores, playerTeams, NUM_PLAYERS);
        gameRenderer.setStatusText(battleHandler.getCharacter0Result(), battleHandler.getCharacter1Result());

        // 4: discarded all cards on the piles
        nextStartingPlayer += 1;
        delay(watchingTime);
    }

    private void playHeartCard(int playerIndex) throws BrokeRuleException {
        int pileIndex;
        setStatusText("Player " + playerIndex + " select a Heart card to play");

        // currentPlayer chooses card, pile based on their in-class rules
        Player currentPlayer = playerList.get(playerIndex);
        selected = currentPlayer.getBestCard();
        pileIndex = playerIndex % NUM_PILES;
        validateMove(selected, pileIndex);

        // Print console message
        assert selected.isPresent() : " Pass returned on selection of character.";
        System.out.println("Player " + playerIndex + " plays " + canonical(selected.get()) + " on pile " + pileIndex);

        // Move card from hand to pile and draw
        transferCard(pileIndex);

        // Update the state of every player to reflect move change
        updatePlayers(false);
    }

    private void playNonHeartCard(int playerIndex) throws BrokeRuleException {
        setStatusText("Player" + playerIndex + " select a non-Heart card to play.");

        // currentPlayer chooses card, pile based on their in-class rules
        Player currentPlayer = playerList.get(playerIndex);
        selected = currentPlayer.getBestCard();

        if (selected.isPresent()) {
            selectedPileIndex = currentPlayer.getPile();
            validateMove(selected, selectedPileIndex);
            setStatusText("Selected: " + canonical(selected.get()) + ". Player" + playerIndex + " select a pile to play the card.");
            System.out.println("Player " + playerIndex + " plays " + canonical(selected.get()) + " on pile " + selectedPileIndex);
            transferCard(selectedPileIndex);

        } else {
            setStatusText("Pass.");
        }
        // Update the state of every player to reflect move change
        updatePlayers(false);
    }

    private void transferCard(int pileIndex) {
        // Handle drawing / transfer logic
        selected.get().setVerso(false);
        selected.get().transfer(piles[pileIndex], true); // transfer to pile (includes graphic effect)
        rankUpdater(piles);
    }

    public void validateMove(Optional<Card> card, int pileIndex) throws BrokeRuleException {
//        check for heart being played as first card on both piles
        if (!card.isPresent()){return;}
        Suit cardSuit = (Suit) card.get().getSuit();
        if (piles[pileIndex].getNumberOfCards() == 0 && !cardSuit.isCharacter()){
            throw new BrokeRuleException("The first card played on each pile must be a character card");
        }
//        check for diamond being played on first card
        if (piles[pileIndex].getNumberOfCards() == 1 && cardSuit.isMagic()){
            throw new BrokeRuleException("A magic card cannot be played on a character card");
        }
    }

    private void updatePlayers(boolean newRound){
        for (int j=0; j<NUM_PLAYERS; j++) {
            playerList.get(j).updateState(hands[j], piles, newRound);
        }
    }

    public void rankUpdater(Hand[] piles){
        ArrayList<int[]> bothRanks;
        bothRanks = pileCalculator.updatePileRanks(piles);
        for (int j = 0; j < piles.length; j++){
            int[] ranks = bothRanks.get(j);
            gameRenderer.updatePileRankState(j, ranks[ATTACK_RANK_INDEX], ranks[DEFENCE_RANK_INDEX], playerTeams);
        }
    }

    public GameOfThrones() throws BrokeRuleException {
        super(700, 700, 30);

        String version = "1.0";
        setTitle("Game of Thrones (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
        initScore();
        setupGame();

        for (int i = 0; i < NUM_PLAYS; i++) {
            executeAPlay();
            gameRenderer.reRenderScore(scores, playerTeams, NUM_PLAYERS);
        }

        String text;
        if (scores[0] > scores[1]) {
            text = "Players 0 and 2 won.";
        } else if (scores[0] == scores[1]) {
            text = "All players drew.";
        } else {
            text = "Players 1 and 3 won.";
        }
        System.out.println("Result: " + text);
        setStatusText(text);

        refresh();
    }

    public static void main(String[] args) throws FileNotFoundException, BrokeRuleException {
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Properties properties;

        if (args == null || args.length == 0) {
            properties = PropertiesLoader.loadPropertiesFile("properties/got.properties");
        } else {
            properties = PropertiesLoader.loadPropertiesFile(args[0]);
        }

        initialiseGameProperties(properties);

        System.out.println("Seed = " + seed);
        GameOfThrones.random = new Random(seed);
        new GameOfThrones();
    }

    private static void initialiseGameProperties(Properties properties) {

        String seedProp = properties.getProperty("seed");  //Seed property
        if (seedProp != null) { // Use property seed
            seed = Integer.parseInt(seedProp);
        } else { // and no property
            seed = new Random().nextInt(); // so randomise
        }

        String watchingTimeProp = properties.getProperty("watchingTime");
        if (watchingTimeProp != null) { // Use property watching time
            watchingTime = Integer.parseInt(watchingTimeProp);
        } else { // and no property
            watchingTime = 5000; // so use default
        }

        for (int i=0; i<4; i++) {
            String playerType = properties.getProperty("players." + i);
            playerList.add(playerFactory.getPlayer(playerType, i));
        }
    }

    public static Random getRandom() {
        return random;
    }
}


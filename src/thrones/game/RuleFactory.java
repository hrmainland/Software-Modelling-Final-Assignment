package thrones.game;

public class RuleFactory {
    public RuleFactory() {
    }

    public GameRule getRule(String ruleType) {
        GameRule rule;
        switch (ruleType) {
            case "DiamondOnHeart":
                rule = new DiamondOnHeartRule();
                break;
            default:
                rule = new FirstTwoCharacterRule();
                break;

        }
        return rule;
    }
}

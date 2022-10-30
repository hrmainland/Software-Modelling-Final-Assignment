package thrones.game;

/**
 * a factory that allows all the rule strategies to be created
 */
public class RuleFactory {
    public RuleFactory() {
    }

    /**
     * the creating logic for the rules
     * @param ruleType
     * @return the specified rule
     */
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

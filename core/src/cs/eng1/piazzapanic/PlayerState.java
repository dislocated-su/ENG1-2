package cs.eng1.piazzapanic;

import com.badlogic.gdx.Gdx;
import cs.eng1.piazzapanic.utility.Timer;
import java.util.HashMap;

public class PlayerState {

    private final float happinessMult = 1.3f;

    private static PlayerState instance = null;

    private float cash = 1000000;

    private int difficultyLevel = 1;

    private float upgradeCost = 100f;

    private int hireCost = 1000;

    private HashMap<PowerUp, Timer> powerUpTimers = new HashMap<PowerUp, Timer>() {
        {
            put(PowerUp.WALK_FAST, new Timer(60000, false, false));
            put(PowerUp.COOK_FAST, new Timer(60000, false, false));
            put(PowerUp.NO_SPOILING, new Timer(60000, false, false));
            put(PowerUp.NO_REP_LOSS, new Timer(60000, false, false));
            put(PowerUp.MORE_MONEY, new Timer(60000, false, false));
        }
    };

    private final HashMap<PowerUp, Integer> powerUpCosts = new HashMap<PowerUp, Integer>() {
        {
            put(PowerUp.WALK_FAST, 100);
            put(PowerUp.COOK_FAST, 100);
            put(PowerUp.NO_SPOILING, 100);
            put(PowerUp.NO_REP_LOSS, 100);
            put(PowerUp.MORE_MONEY, 100);
        }
    };

    public enum PowerUp {
        WALK_FAST,
        COOK_FAST,
        NO_SPOILING,
        NO_REP_LOSS,
        MORE_MONEY,
    }

    private PlayerState() {
    }

    /**
     * Gets the value of unlocking a new station, also increments this price each
     * time when called with buying = true
     *
     * @param buying whether the call is at time of buying, meaning when it will
     *               increase price as well
     * @returns returnCost, the cost of purchasing the station
     */
    public float getUpgradeCost(boolean buying) {
        float returnCost = upgradeCost;
        if (buying) {
            upgradeCost += 100f;
        }
        return returnCost;
    }

    /**
     * Returns cost of buying a new chef, increasing the price for the second cook
     * bought (as there is a max of two cooks for purchase)
     *
     * @param buying whether the call is at time of buying, meaning it will increase
     *               the price as well
     * @returns output, the cost of hiring a cook
     */
    public int getChefHireCost(boolean buying) {
        int output = hireCost;
        if (buying) {
            hireCost = 10000;
        }
        return output;
    }

    /**
     * @param powerUp to be checked
     * @returns cost of the PowerUp
     */
    public int getPowerupCost(PowerUp powerup) {
        return powerUpCosts.get(powerup);
    }

    /**
     * Returns an instance of PlayerState, making this class function as a Singleton
     * class and can be called to access the same object across the program
     *
     * @returns instance, the instance of PlayerState, newly created if no one
     *          exists prior
     */
    public static PlayerState getInstance() {
        if (instance == null) {
            instance = new PlayerState();
        }
        return instance;
    }

    /**
     * Resets the instance of PlayerState
     */
    public static void reset() {
        instance = null;
    }

    /**
     * @return cash, the player's current amount of money
     */
    public float getCash() {
        return cash;
    }

    /**
     * Calculates the total multiplier to earned cash, calculating based on active
     * PowerUps and happiness
     *
     * @param customerHappy, whether or not the customer is happy (served in half
     *                       their alloted time)
     * @return totalMultiplier, the total multiplier on earned cash
     */
    private double totalMultiplier(boolean customerHappy) {
        double totalMultiplier = 1;
        if (customerHappy) {
            totalMultiplier *= happinessMult;
        }
        if (getBuffActive(PowerUp.MORE_MONEY)) {
            totalMultiplier *= 2;
        }
        return totalMultiplier;
    }

    /**
     * Increases the player's cash
     *
     * @param baseAmount     the base amount of cash a player will earn
     * @param customerHappy, the customer's happiness, given to totalMultiplier
     *                       function
     */
    public void earnCash(float baseAmount, boolean customerHappy) {
        Gdx.app.log("Current cash", cash + "");
        getInstance().cash += baseAmount * totalMultiplier(customerHappy);
    }

    /**
     * Reduces the player's cash if they have enough
     *
     * @param amount, the amount of cash to be spent
     */
    public void spendCash(double amount) {
        if (cash < amount) {
            return;
        }

        cash -= amount;
        Gdx.app.log("Current cash", cash + "");
    }

    /**
     * Iterates over the active PowerUp timers and calls the timer tick function on
     * them to decrease duration
     *
     * @param delta time since last function call
     */
    public void act(float delta) {
        for (Timer timer : powerUpTimers.values()) {
            // first check if a timer is running, then check if timer is complete for
            // efficiency
            if (timer.getRunning()) {
                if (timer.tick(delta)) {
                    timer.stop();
                    timer.reset();
                }
            }
        }
    }

    /**
     * Returns whether a PowerUp is active
     *
     * @param powerUp to check if it is currently active
     * @return true if the PowerUp is active, false if not
     */
    public boolean getBuffActive(PowerUp powerUp) {
        return powerUpTimers.get(powerUp).getRunning();
    }

    /**
     * Activates a PowerUp
     *
     * @param powerUp to be activated
     */
    public void activateBuff(PowerUp powerUp) {
        powerUpTimers.get(powerUp).start();
    }

    /**
     * @param powerUp to be checked
     * @returns the time the powerUp will last for at max
     */
    public int getBuffDuration(PowerUp powerUp) {
        return powerUpTimers.get(powerUp).getDelay();
    }

    /**
     * @param powerUp to be checked
     * @returns remaining time of the PowerUp
     */
    public int getBuffRemaining(PowerUp powerUp) {
        return powerUpTimers.get(powerUp).getRemainingTime();
    }

    /**
     * @param powerUp to return name of
     * @returns the formatted name of the PowerUp (capitalized first letter, removed
     *          underscore)
     */
    public String getPowerupName(PowerUp powerUp) {
        String output = powerUp.name().replaceAll("_", " ").toLowerCase();
        output = Character.toUpperCase(output.charAt(0)) + output.substring(1);
        return output;
    }

    /**
     * @return current difficulty level
     */
    public int getDifficulty() {
        return difficultyLevel;
    }

    /**
     * @param value to set as difficulty
     */
    public void setDifficulty(int value) {
        difficultyLevel = value;
    }
}

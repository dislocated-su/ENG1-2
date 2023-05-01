package cs.eng1.piazzapanic;

import cs.eng1.piazzapanic.utility.Timer;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;

public class PlayerState {

    private final float happinessMult = 1.3f;

    private static PlayerState instance = null;

    private float cash = 1000000;

    private boolean paused = false;

    private int difficultyLevel = 1;

    private float upgradeCost = 100f;

    private HashMap<PowerUp, Timer> powerUpTimers = new HashMap<PowerUp, Timer>() {
        {
            put(PowerUp.WALK_FAST, new Timer(60000, false, false));
            put(PowerUp.COOK_FAST, new Timer(60000, false, false));
            put(PowerUp.NO_SPOILING, new Timer(60000, false, false));
            put(PowerUp.NO_REP_LOSS, new Timer(60000, false, false));
            put(PowerUp.MORE_MONEY, new Timer(60000, false, false));
        }
    };

    private static final HashMap<PowerUp, Integer> powerUpCosts = new HashMap<PowerUp, Integer>() {
        {
            put(PowerUp.WALK_FAST, 100);
            put(PowerUp.COOK_FAST, 100);
            put(PowerUp.NO_SPOILING, 100);
            put(PowerUp.NO_REP_LOSS, 100);
            put(PowerUp.MORE_MONEY, 100);
        }
    };

    public int getPowerupCost(PowerUp powerup) {
        return powerUpCosts.get(powerup);
    }

    public enum PowerUp {
        WALK_FAST,
        COOK_FAST,
        NO_SPOILING,
        NO_REP_LOSS,
        MORE_MONEY,
    }

    private PlayerState() {
    }

    public float getUpgradeCost(boolean buying) {
        float returnCost = upgradeCost;
        if (buying) {
            upgradeCost += 100f;
        }
        return returnCost;
    }

    int hireCost = 1000;

    public int getChefHireCost(boolean buying) {
        int output = hireCost;
        if (buying) {
            hireCost = 10000;
        }
        return output;
    }

    public static PlayerState getInstance() {
        if (instance == null) {
            instance = new PlayerState();
        }
        return instance;
    }

    /**
     *
     */
    public static void reset() {
        instance = null;
    }

    /**
     *
     * @return
     */
    public float getCash() {
        return cash;
    }

    /**
     *
     * @param customerHappy
     * @return
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
     *
     * @param baseAmount
     * @param customerHappy
     */
    public void earnCash(float baseAmount, boolean customerHappy) {
        Gdx.app.log("Current cash", cash + "");
        getInstance().cash += baseAmount * totalMultiplier(customerHappy);
    }

    /**
     *
     * @param amount
     * @return
     */
    public boolean spendCash(double amount) {

        if (cash < amount) {
            return false;
        }

        cash -= amount;
        Gdx.app.log("Current cash", cash + "");
        return true;
    }

    /**
     *
     * @param delta
     */
    public void act(float delta) {
        if (!paused) {
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
    }

    /**
     *
     * @param powerUp
     * @return
     */
    public boolean getBuffActive(PowerUp powerUp) {
        return powerUpTimers.get(powerUp).getRunning();
    }

    /**
     *
     * @param powerUp
     */
    public void activateBuff(PowerUp powerUp) {
        powerUpTimers.get(powerUp).start();
    }

    public int getBuffDuration(PowerUp powerUp) {
        return powerUpTimers.get(powerUp).getDelay();
    }

    public int getBuffRemaining(PowerUp powerUp) {
        return powerUpTimers.get(powerUp).getRemainingTime();
    }

    public String getPowerupName(PowerUp powerUp) {
        String output = powerUp
                .name()
                .replaceAll("_", " ")
                .toLowerCase();
        output = Character.toUpperCase(output.charAt(0)) +
                output.substring(1);
        return output;
    }

    /**
     *
     * @return
     */
    public int getDifficulty() {
        return difficultyLevel;
    }

    /**
     *
     * @param value
     */
    public void setDifficulty(int value) {
        difficultyLevel = value;
    }

    /**
     * sets paused flag to true, which prevents the powerUp timers from ticking
     */
    public void pause() {
        paused = true;
    }

    /**
     * sets paused flag to false, which allows the powerUp timers to tick
     */
    public void resume() {
        paused = false;
    }

    public boolean getPaused() {
        return paused;
    }
}

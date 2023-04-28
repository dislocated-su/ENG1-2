package cs.eng1.piazzapanic;

import cs.eng1.piazzapanic.utility.Timer;
import java.util.HashMap;

public class PlayerState {

    private final float happinessMult = 1.3f;

    private static PlayerState instance = null;

    private float cash = 0;

    private boolean paused = false;

    private int difficultyLevel = 1;

    private float upgradeCost = 100f;

    private HashMap<PowerUp, Timer> powerUpTimers = new HashMap<PowerUp, Timer>();

    public enum PowerUp {
        DOUBLE_CHEF_SPEED,
        DOUBLE_PREP_SPEED,
        NO_FAIL_PREP,
        NO_REP_LOSS,
        MORE_MONEY,
    }

    private PlayerState() {
        // for (PowerUp powerUp : PowerUp.values()) {
        // powerUpTimers.put(powerUp, new Timer(60000, false, false));
        // }
        powerUpTimers.put(
                PowerUp.DOUBLE_CHEF_SPEED,
                new Timer(60000, false, false));
        powerUpTimers.put(
                PowerUp.DOUBLE_PREP_SPEED,
                new Timer(60000, false, false));
        powerUpTimers.put(PowerUp.NO_FAIL_PREP, new Timer(60000, false, false));
        powerUpTimers.put(PowerUp.NO_REP_LOSS, new Timer(60000, false, false));
        powerUpTimers.put(PowerUp.MORE_MONEY, new Timer(60000, false, false));
    }

    public float getUpgradeCost(boolean buying) {
        float returnCost = upgradeCost;
        if (buying) {
            upgradeCost += 100f;
        }
        return returnCost;
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
     * @param index
     * @return
     */
    public boolean getBuffActive(PowerUp powerUp) {
        return powerUpTimers.get(powerUp).getRunning();
    }

    /**
     *
     * @param index
     */
    public void activateBuff(PowerUp powerUp) {
        powerUpTimers.get(powerUp).start();
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

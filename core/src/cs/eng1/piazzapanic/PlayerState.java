package cs.eng1.piazzapanic;

import cs.eng1.piazzapanic.utility.Timer;

public class PlayerState {

    private final float happinessMult = 1.3f;

    private static PlayerState instance = null;

    private float cash = 0;

    private int difficultyLevel;

    private Timer[] powerUpTimers = {
        new Timer(10000, false, false), // doubleChefSpeed
        new Timer(60000, false, false), // doublePrepSpeed
        new Timer(60000, false, false), // noFailPrep
        new Timer(60000, false, false), // noRepLoss
        new Timer(60000, false, false), // moreMoney
    };
    // private boolean[] powerUps = { false, false, false, false, false };

    private PlayerState() {
    }

    /**
     *
     * @return
     */
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
        if (getBuffActive(4)) {
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
        for (Timer timer : powerUpTimers) {
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
     *
     * @param index
     * @return
     */
    public boolean getBuffActive(int index) {
        return powerUpTimers[index].getRunning();
    }

    /**
     *
     * @param index
     */
    public void activateBuff(int index) {
        powerUpTimers[index].start();
    }

    public int getDifficulty() {
        return difficultyLevel;
    }

    public void setDifficulty(int value) {
        difficultyLevel = value;
    }
}

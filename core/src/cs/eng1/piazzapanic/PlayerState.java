package cs.eng1.piazzapanic;

import cs.eng1.piazzapanic.utility.Timer;

public class PlayerState {

    private final float happinessMult = 1.3f;

    private static PlayerState instance = null;

    public float cash = 0;

    private Timer[] powerUpTimers = {
        new Timer(10000, true, false), // doubleChefSpeed
        new Timer(60000, false, false), // doublePrepSpeed
        new Timer(60000, false, false), // noFailPrep
        new Timer(60000, false, false), // noRepLoss
        new Timer(60000, false, false), // moreMoney
    };

    public static PlayerState getInstance() {
        if (instance == null) {
            instance = new PlayerState();
        }
        return instance;
    }

    private PlayerState() {}

    public float getCash() {
        return cash;
    }

    // enum for power up types if needed, would require implementing valued enum
    // though
    public enum PowerUp {
        DOUBLE_CHEF_SPEED,
        DOUBLE_PREP_SPEED,
        NO_FAIL_PREP,
        NO_REP_LOSS,
        MORE_MONEY,
    }

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

    public void earnCash(float baseAmount, boolean customerHappy) {
        getInstance().cash += baseAmount * totalMultiplier(customerHappy);
    }

    public boolean spendCash(double amount) {
        if (cash < amount) {
            return false;
        }

        cash -= amount;

        return true;
    }

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

    public boolean getBuffActive(int index) {
        return powerUpTimers[index].getRunning();
    }

    public void activateBuff(int index) {
        powerUpTimers[index].start();
    }

    public static void reset() {
        instance = null;
        // for (Timer timer : powerUpTimers)
        // {
        // timer.stop();
        // timer.reset();
        // }
        // cash = 0;
    }
}

package cs.eng1.piazzapanic;

import cs.eng1.piazzapanic.utility.Timer;

public class PlayerState {

    public boolean customerHappy;
    private float happinessMult = 1.3f;

    public boolean powerUpActive;
    public int powerUpLevel = 0;

    private static PlayerState instance = null;

    public static PlayerState getInstance() {
        if (instance == null) {
            return new PlayerState();
        } else {
            return instance;
        }
    }

    private PlayerState() {}

    // enum for power up types if needed, would require implementing valued enum
    // though
    /*
     * public enum PowerUp {
     * DOUBLE_CHEF_SPEED,
     * DOUBLE_PREP_SPEED,
     * NO_FAIL_PREP,
     * NO_REP_LOSS,
     * MORE_MONEY
     * }
     */

    public double powerupMult() {
        return ((powerUpLevel * 4) / (powerUpLevel * 30)) + 1;
    }

    private double totalMultiplier() {
        double totalMultiplier = 1;
        if (customerHappy) {
            totalMultiplier *= happinessMult;
        }
        if (powerUpActive) {
            totalMultiplier *= powerupMult();
        }
        return totalMultiplier;
    }

    public double cash;

    public void earnCash(float baseAmount) {
        cash += baseAmount * totalMultiplier();
    }

    public boolean spendCash(double amount) {
        if (cash < amount) {
            return false;
        }

        cash -= amount;

        return true;
    }

    // Powerups and other stuff here
    private static Timer[] powerUpTimers = {
        new Timer(60000, false, false), // doubleChefSpeed
        new Timer(60000, false, false), // doublePrepSpeed
        new Timer(60000, false, false), // noFailPrep
        new Timer(60000, false, false), // noRepLoss
        new Timer(60000, false, false), // moreMoney
    };

    public void act(float delta) {
        for (Timer timer : powerUpTimers) {
            // first check if a timer is running, then check if timer is ticking for
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
}

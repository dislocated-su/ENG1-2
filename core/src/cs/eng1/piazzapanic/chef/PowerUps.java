package cs.eng1.piazzapanic.chef;

import cs.eng1.piazzapanic.utility.Timer;

public class PowerUps {

    // doubleChefSpeed, doublePrepSpeed, noFailPrep, noRepLoss, moreMoney
    private Timer[] powerUpTimers = {
            new Timer(60000, false, false), // doubleChefSpeed
            new Timer(60000, false, false), // doublePrepSpeed
            new Timer(60000, false, false), // noFailPrep
            new Timer(60000, false, false), // noRepLoss
            new Timer(60000, false, false), // moreMoney
    };

    /**
     * private boolean doubleChefSpeed = false;
     * private boolean doublePrepSpeed = false;
     * private boolean noFailPrep = false;
     * private boolean noRepLoss = false;
     * private boolean moreMoney = false;
     */

    public PowerUps() {
    }

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

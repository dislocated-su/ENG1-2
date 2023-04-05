package cs.eng1.piazzapanic;

public class PlayerState {

    public boolean customerHappy;
    private float happinessMult = 1.3f;

    public boolean powerUpActive;
    public int powerUpLevel = 0;

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
}

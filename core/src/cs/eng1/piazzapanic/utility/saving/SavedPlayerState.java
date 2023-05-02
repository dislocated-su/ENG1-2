package cs.eng1.piazzapanic.utility.saving;

import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.PlayerState.PowerUp;
import cs.eng1.piazzapanic.utility.Timer;

public class SavedPlayerState {

    public float cash, upgradeCost;
    public int difficulty, hireCost, purchaseChefs;
    public SerializeableMap<PowerUp, Timer> powerUpTimers;

    public SavedPlayerState() {}

    public SavedPlayerState(PlayerState state) {
        this.powerUpTimers = new SerializeableMap<>(state.getPowerUpTimers());
        cash = state.getCash();
        difficulty = state.getDifficulty();
        upgradeCost = state.getUpgradeCost(false);
        hireCost = state.getChefHireCost(false);
        purchaseChefs = state.getPurchasedChefs();
    }
}

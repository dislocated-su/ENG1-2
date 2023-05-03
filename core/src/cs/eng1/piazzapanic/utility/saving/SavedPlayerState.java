package cs.eng1.piazzapanic.utility.saving;

import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.PlayerState.PowerUp;
import cs.eng1.piazzapanic.utility.Timer;

/**
 * SavedPlayerState stores the relevant values of PlayerState so that it can be
 * saved as part of a json file
 * 
 * SavedPlayerState stores the player's cash, selected difficulty, their power
 * up timers and their progress in purchasing stations and hiring chefs
 * 
 * @author Ross Holmes
 * @author Andrey Samoilov
 */
public class SavedPlayerState {

    public float cash, upgradeCost;
    public int difficulty, hireCost, purchaseChefs;
    public SerializableMap<PowerUp, Timer> powerUpTimers;

    public SavedPlayerState() {
    }

    public SavedPlayerState(PlayerState state) {
        this.powerUpTimers = new SerializableMap<>(state.getPowerUpTimers());
        cash = state.getCash();
        difficulty = state.getDifficulty();
        upgradeCost = state.getUpgradeCost(false);
        hireCost = state.getChefHireCost(false);
        purchaseChefs = state.getPurchasedChefs();
    }
}

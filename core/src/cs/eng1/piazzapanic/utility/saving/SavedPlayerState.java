package cs.eng1.piazzapanic.utility.saving;

import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.PlayerState.PowerUp;
import cs.eng1.piazzapanic.utility.Timer;

public class SavedPlayerState {

    public Timer walkTimer, cookTimer, spoilTimer, repLossTimer, moneyTimer;
    public float cash, upgradeCost;
    public int difficulty, hireCost;

    public SavedPlayerState() {

    }

    public SavedPlayerState(PlayerState state) {
        walkTimer = state.getTimer(PowerUp.WALK_FAST);
        cookTimer = state.getTimer(PowerUp.COOK_FAST);
        spoilTimer = state.getTimer(PowerUp.NO_SPOILING);
        repLossTimer = state.getTimer(PowerUp.NO_REP_LOSS);
        moneyTimer = state.getTimer(PowerUp.MORE_MONEY);
        cash = state.getCash();
        difficulty = state.getDifficulty();
        upgradeCost = state.getUpgradeCost(false);
        hireCost = state.getChefHireCost(false);
    }

}

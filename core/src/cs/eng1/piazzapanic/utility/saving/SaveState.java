package cs.eng1.piazzapanic.utility.saving;

import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.customer.CustomerManager;

public class SaveState {
    public SavedPlayerState playerState;

    // ChefManager chefManager;

    // CustomerManager customerManager;
    public void from(PlayerState state) {
        this.playerState = new SavedPlayerState(state);

    }
}
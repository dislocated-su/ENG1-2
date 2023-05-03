package cs.eng1.piazzapanic.utility.saving;

import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.customer.CustomerManager;

/**
 * SaveState stores all other types of Saved classes and serves to hold the json
 * values read and written to file
 *
 * @author Ross Holmes
 * @author Andrey Samoilov
 */
public class SaveState {

    public SavedPlayerState playerState;

    public SavedChefManager chefManager;

    public SavedCustomerManager customerManager;

    public SavedStation[] stations;

    public void setPlayerState(PlayerState state) {
        this.playerState = new SavedPlayerState(state);
    }

    public void setChefManager(ChefManager chefManager) {
        this.chefManager = new SavedChefManager(chefManager);
    }

    public void setCustomerManager(CustomerManager customerManager) {
        this.customerManager = new SavedCustomerManager(customerManager);
    }

    public void setStations(SavedStation[] stations) {
        this.stations = stations;
    }
}

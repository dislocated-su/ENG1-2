package cs.eng1.piazzapanic.utility.saving;

import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import java.util.List;

/**
 * SavedChefManager stores the relevant values for saving the ChefManager class
 * as a json
 *
 * SavedChefManager stores an array of SavedChefs, which is the currently
 * available chefs
 *
 * @author Ross Holmes
 * @author Andrey Samoilov
 */
public class SavedChefManager {

    public SavedChef[] savedChefs;

    public SavedChefManager(ChefManager chefManager) {
        List<Chef> chefs = chefManager.getChefs();
        savedChefs = new SavedChef[chefs.size()];

        for (int i = 0; i < chefs.size(); i++) {
            if (chefManager.getCurrentChef() == null) {
                savedChefs[i] = new SavedChef(chefs.get(i), false);
            } else {
                savedChefs[i] = new SavedChef(chefs.get(i), chefManager.getCurrentChef().equals(chefs.get(i)));
            }
        }
    }

    public SavedChefManager() {}
}

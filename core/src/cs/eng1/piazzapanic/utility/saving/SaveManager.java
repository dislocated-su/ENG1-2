package cs.eng1.piazzapanic.utility.saving;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.customer.CustomerManager;
import cs.eng1.piazzapanic.stations.IngredientStation;
import cs.eng1.piazzapanic.stations.Station;
import cs.eng1.piazzapanic.stations.SubmitStation;
import java.util.LinkedList;
import java.util.List;

public class SaveManager {

    private static SaveManager instance = null;

    private SaveManager() {}

    public static SaveManager getInstance() {
        if (instance == null) {
            instance = new SaveManager();
        }

        return instance;
    }

    /**
     * @param chefManager
     * @param customerManager
     * @param stage
     */
    public void save(
        ChefManager chefManager,
        CustomerManager customerManager,
        Stage stage
    ) {
        SaveState state = new SaveState();
        state.setPlayerState(PlayerState.getInstance());
        state.setChefManager(chefManager);
        state.setCustomerManager(customerManager);

        List<SavedStation> stations = new LinkedList<>();

        for (Actor actor : stage.getActors().items) {
            if (
                actor instanceof Station &&
                !(actor instanceof IngredientStation) &&
                !(actor instanceof SubmitStation)
            ) {
                stations.add(new SavedStation((Station) actor));
            }
        }

        state.setStations(stations.toArray(new SavedStation[stations.size()]));

        Json json = new Json();
        json.toJson(
            state,
            SaveState.class,
            null,
            new FileHandle("savefile.json")
        );
    }

    public SaveState load(String fileHandle) {
        Json json = new Json();
        SaveState state = json.fromJson(
            SaveState.class,
            new FileHandle(fileHandle)
        );

        return state;
    }
}

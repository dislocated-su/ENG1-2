package cs.eng1.piazzapanic.utility.saving;

import cs.eng1.piazzapanic.stations.ChoppingStation;
import cs.eng1.piazzapanic.stations.CookingStation;
import cs.eng1.piazzapanic.stations.GrillingStation;
import cs.eng1.piazzapanic.stations.RecipeStation;
import cs.eng1.piazzapanic.stations.Station;

public class SavedStation {

    public int id;
    public boolean inUse;
    public boolean locked;
    public SavedFood[] items;
    public String type;
    public SavedIngredientStack ingredientStack;

    public SavedStation() {
    }

    public SavedStation(Station station) {
        this.id = station.getId();
        this.inUse = station.getInUse();
        this.locked = station.getLocked();

        if (station instanceof RecipeStation) {
            RecipeStation recipeStation = (RecipeStation) station;
            ingredientStack = new SavedIngredientStack(recipeStation.ingredientStack);
            items = new SavedFood[recipeStation.displayIngredient.size()];
            for (int i = 0; i < recipeStation.displayIngredient.size(); i++) {
                items[i] = new SavedFood(recipeStation.displayIngredient.get(i));
            }
            type = "Recipe";
        } else {
            items = new SavedFood[1];
            if (station instanceof ChoppingStation) {
                ChoppingStation choppingStation = (ChoppingStation) station;
                items[0] = choppingStation.currentIngredient != null
                        ? new SavedFood(choppingStation.currentIngredient)
                        : null;
                type = "Chopping";
            } else if (station instanceof CookingStation) {
                CookingStation cookingStation = (CookingStation) station;
                items[0] = cookingStation.currentIngredient != null
                        ? new SavedFood(cookingStation.currentIngredient)
                        : null;
                type = "Cooking";
            } else if (station instanceof GrillingStation) {
                GrillingStation grillingStation = (GrillingStation) station;
                items[0] = grillingStation.currentIngredient != null
                        ? new SavedFood(grillingStation.currentIngredient)
                        : null;
                type = "Grilling";
            } else {
                throw new AssertionError(
                        "Attempting to save a station of invalid type");
            }
        }
    }
}

package cs.eng1.piazzapanic.utility.saving;

import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.BasicChoppable;
import cs.eng1.piazzapanic.food.ingredients.BasicCookable;
import cs.eng1.piazzapanic.food.ingredients.BasicGrillable;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.food.interfaces.Holdable;
import cs.eng1.piazzapanic.food.recipes.Recipe;

/**
 * SavedFood stores the values of an instance of food, ingredient or recipe,
 * relevant for saving in a manner that can be stored as a json
 * 
 * SavedFood stores the name of the food, along with whether it is useable,
 * completed and half completed (for grillable and cookable)
 * 
 * @author Ross Holmes
 * @author Andrey Samoilov
 */
public class SavedFood {

    public String type;
    public Boolean useable = false;
    public Boolean completed = false;
    public Boolean halfCompleted = false;

    public SavedFood() {
    }

    public SavedFood(Holdable item) {
        this.type = item.getType();

        if (item instanceof Ingredient) {
            this.useable = ((Ingredient) item).getUseable();
            if (item instanceof BasicGrillable) {
                BasicGrillable grillable = (BasicGrillable) item;
                this.completed = grillable.getGrilled();
                this.halfCompleted = grillable.getHalfGrilled();
            } else if (item instanceof BasicCookable) {
                BasicCookable cookable = (BasicCookable) item;
                this.completed = cookable.getCooked();
                this.halfCompleted = cookable.getHalfCooked();
            } else if (item instanceof BasicChoppable) {
                this.completed = ((BasicChoppable) item).getChopped();
            }
        }
    }

    /**
     * Allows the user to conver the SavedFood instance into an instance of the food
     * it is meant to represent
     * 
     * @param manager the FoodTextureManager instance
     * @returns an instance of the type of food with values correctly assigned
     */
    public Holdable get(FoodTextureManager manager) {
        Ingredient i = Ingredient.fromString(type, manager);
        if (i != null) {
            if (i instanceof BasicGrillable) {
                BasicGrillable grillable = (BasicGrillable) i;
                grillable.setIsGrilled(completed);
                grillable.setHalfGrilled(halfCompleted);
            } else if (i instanceof BasicCookable) {
                BasicCookable cookable = (BasicCookable) i;
                cookable.setIsCooked(completed);
                cookable.setHalfCooked(halfCompleted);
            } else if (i instanceof BasicChoppable) {
                BasicChoppable choppable = (BasicChoppable) i;
                choppable.setChopped(completed);
            }
            return i;
        }
        // If not an ingredient, then a recipe.
        return Recipe.fromString(type, manager);
    }
}

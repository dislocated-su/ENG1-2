package cs.eng1.piazzapanic.utility.saving;

import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.BasicChoppable;
import cs.eng1.piazzapanic.food.ingredients.BasicCookable;
import cs.eng1.piazzapanic.food.ingredients.BasicGrillable;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.food.interfaces.Holdable;
import cs.eng1.piazzapanic.food.recipes.Recipe;

public class SavedFood {

    public String type;
    public Boolean useable = false;
    public Boolean completed = false;
    public Boolean halfCompleted = false;

    public SavedFood() {}

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

    public Holdable get(FoodTextureManager manager) throws AssertionError {
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
            return (Holdable) i;
        }
        Recipe r = Recipe.fromString(type, manager);
        if (r != null) {
            return (Holdable) r;
        }

        throw new AssertionError("Loaded food is not of any known type");
    }
}

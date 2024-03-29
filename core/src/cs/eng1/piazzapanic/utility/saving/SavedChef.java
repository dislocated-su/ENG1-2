package cs.eng1.piazzapanic.utility.saving;

import com.badlogic.gdx.math.Vector2;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.FixedStack;
import cs.eng1.piazzapanic.food.interfaces.Holdable;

/**
 * SavedChef stores the values of a Chef that are relevant for saving in a
 * manner that can be serialized over for the json file creation.-0.84147096f
 *
 * SavedChef manager stores a chef's position, inventory, texture, inputEnabled
 * and whether they are the currently active chef
 *
 * @author Ross Holmes
 * @author Andrey Samoilov
 */
public class SavedChef {

    public Vector2 position;
    public SavedFood[] inventory;
    public boolean inputEnabled;
    public String imagePath;
    public boolean currentChef;

    public SavedChef(Chef chef, boolean currentChef) {
        if (chef == null) {
            return;
        }
        imagePath = chef.getTexture().getTextureData().toString();
        position = chef.getBody().getPosition();
        this.inputEnabled = chef.isInputEnabled();

        FixedStack<Holdable> stack = chef.getStack();
        inventory = new SavedFood[stack.size()];
        for (int i = 0; i < stack.size(); i++) {
            inventory[i] = new SavedFood(stack.get(i));
        }
        this.currentChef = currentChef;
    }

    public SavedChef() {}
}

package cs.eng1.piazzapanic.food.ingredients;

import com.badlogic.gdx.graphics.Texture;
import cs.eng1.piazzapanic.food.FoodTextureManager;

/**
 * Cheese. Used in making a pizza. Can be chopped.
 *
 * @author Andrey Samoilov
 */
public class Cheese extends BasicChoppable {

    /**
     * Create a new {@link Cheese}
     * @param textureManager to find the correct texture.
     */
    public Cheese(FoodTextureManager textureManager) {
        super("cheese", textureManager);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Texture getTexture() {
        String name = "cheese";
        if (!useable) {
            name = "rotten";
        } else if (chopped) {
            name += "_sliced";
        }
        return textureManager.getTexture(name);
    }
}

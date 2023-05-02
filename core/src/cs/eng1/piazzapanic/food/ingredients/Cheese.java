package cs.eng1.piazzapanic.food.ingredients;

import com.badlogic.gdx.graphics.Texture;
import cs.eng1.piazzapanic.food.FoodTextureManager;

public class Cheese extends BasicChoppable {

    public Cheese(FoodTextureManager textureManager) {
        super("cheese", textureManager);
    }

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

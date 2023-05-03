package cs.eng1.piazzapanic.food.ingredients;

import com.badlogic.gdx.graphics.Texture;
import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.PlayerState.PowerUp;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.interfaces.Choppable;
import cs.eng1.piazzapanic.food.interfaces.Holdable;

/**
 * A base class for ingredients that need to be chopped, such as Lettuce and
 * Tomato.
 * Implements the Choppable interface to enable the chopping functionality.
 *
 * @author Ross Holmes
 * @author Andrey Samoilov
 */
public abstract class BasicChoppable extends Ingredient implements Choppable {

    private float accumulator = 0f;
    private final float chopTime = 5f;
    private final float failTime = 5f;

    /**
     * Constructor for BasicChoppable.
     * 
     * @param type           the type of ingredient.
     * @param textureManager the texture manager to use.
     */
    public BasicChoppable(String type, FoodTextureManager textureManager) {
        super(type, textureManager);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean choppingTick(float delta) {
        accumulator += (delta * ((PlayerState.getInstance().getBuffActive(PowerUp.COOK_FAST)) ? 2 : 1));

        if (accumulator >= (chopTime
                + (failTime * (PlayerState.getInstance().getBuffActive(PowerUp.COOK_FAST) ? 2 : 1))) &&
                !PlayerState.getInstance().getBuffActive(PowerUp.NO_SPOILING)) {
            setUseable(false);
            return false;
        } else if (accumulator >= chopTime) {
            chopped = true;
        }
        return chopped;
    }

    /**
     * Gets the current chopping progress as a percentage.
     * 
     * @return the current chopping progress as a percentage.
     */
    @Override
    public float getChoppingProgress() {
        return (accumulator / chopTime) * 100f;
    }

    /**
     * Gets the result of the chopping.
     * 
     * @return the chopped ingredient.
     */
    @Override
    public Holdable getChoppingResult() {
        return this;
    }

    /**
     * Get texture based on ingredient type and chopping status.
     * 
     * @see Holdable
     * @return Texture
     */
    @Override
    public Texture getTexture() {
        String name = getType() + "_";
        if (!useable) {
            name = "rotten";
        } else if (chopped) {
            name += "chopped";
        } else {
            name += "raw";
        }
        return textureManager.getTexture(name);
    }
}

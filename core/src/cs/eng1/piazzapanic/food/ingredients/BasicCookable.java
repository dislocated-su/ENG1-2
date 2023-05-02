package cs.eng1.piazzapanic.food.ingredients;

import com.badlogic.gdx.graphics.Texture;
import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.PlayerState.PowerUp;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.interfaces.Cookable;
import cs.eng1.piazzapanic.food.interfaces.Holdable;

/**
 * A base class for ingredients that need to be cooked, such as {@link Potato}.
 * Implements the {@link Cookable} interface to enable the chopping functionality.
 *
 * @author Ross Holmes
 * @author Andrey Samoilov
 */
public abstract class BasicCookable extends Ingredient implements Cookable {

    private float accumulator = 0f;
    private final float cookingStepTime = 2f;
    private final float failTime = 3f;
    private boolean halfCooked = false;
    private boolean flipped = false;

    /**
     * Constructor for BasicCookable.
     * @param type the type of ingredient.
     * @param textureManager the texture manager to use.
     */
    public BasicCookable(String type, FoodTextureManager textureManager) {
        super(type, textureManager);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean cookingTick(float delta) {
        accumulator +=
            (
                delta *
                (
                    (PlayerState.getInstance().getBuffActive(PowerUp.COOK_FAST))
                        ? 2
                        : 1
                )
            );
        if (
            // fail time is doubled when double prep speed is active (since delta is
            // doubled)

            accumulator >=
            (
                cookingStepTime +
                (
                    failTime *
                    (
                        PlayerState
                                .getInstance()
                                .getBuffActive(PowerUp.COOK_FAST)
                            ? 2
                            : 1
                    )
                )
            ) &&
            !PlayerState.getInstance().getBuffActive(PowerUp.NO_SPOILING)
        ) {
            setUseable(false);
        } else if (accumulator >= cookingStepTime) {
            if (!getHalfCooked()) {
                halfCooked = true;
            } else if (flipped) {
                cooked = true;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean getHalfCooked() {
        return halfCooked;
    }


    public void setHalfCooked(boolean value) {
        halfCooked = value;
    }

    /**
     * @return {@code boolean} representing whether the ingredient is ready to be flipped or picked up.
     */
    @Override
    public boolean cookingStepComplete() {
        return (accumulator >= cookingStepTime);
    }

    /**
     * Gets the current cooking progress as a percentage.
     * @return the current cooking progress as a percentage.
     */
    @Override
    public float getCookingProgress() {
        return (accumulator / cookingStepTime) * 100f;
    }

    /**
     * Gets the result of the cooking.
     * @return the cooked ingredient.
     */
    @Override
    public Holdable getCookingResult() {
        return this;
    }

    /**
     * Flip the {@link BasicCookable}. This action is done by the player and prevents the object from becoming burnt.
     */
    @Override
    public void flip() {
        accumulator = 0;
        flipped = true;
    }

    /**
     * Get texture based on ingredient type and cooking status.
     * @see Holdable
     * @return Texture
     */
    @Override
    public Texture getTexture() {
        String name = getType();
        if (!useable) {
            name = "burnt";
        } else if (cooked) {
            name += "_cooked";
        }
        return textureManager.getTexture(name);
    }
}

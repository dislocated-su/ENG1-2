package cs.eng1.piazzapanic.food.ingredients;

import com.badlogic.gdx.graphics.Texture;
import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.PlayerState.PowerUp;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.interfaces.Grillable;
import cs.eng1.piazzapanic.food.interfaces.Holdable;

/**
 * A base class for ingredients that need to be grilled, such as {@link Patty}.
 * Implements the {@link Grillable} interface to enable the chopping functionality.
 *
 * @author Ross Holmes
 * @author Andrey Samoilov
 */
public abstract class BasicGrillable extends Ingredient implements Grillable {

    private float accumulator = 0f;
    private final float grillStepTime = 2f;
    private final float failTime = 3f;
    private boolean halfGrilled = false;
    private boolean flipped = false;

    /**
     * Constructor for BasicGrillable.
     * @param type the type of ingredient.
     * @param textureManager the texture manager to use.
     */
    public BasicGrillable(String type, FoodTextureManager textureManager) {
        super(type, textureManager);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean grillTick(float delta) {
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
            accumulator >=
            (
                grillStepTime +
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
        } else if (accumulator >= grillStepTime) {
            if (!getHalfGrilled()) {
                halfGrilled = true;
            } else if (flipped) {
                grilled = true;
            }
            return true;
        }
        return false;
    }

    /**
     * Gets the current grilling progress as a percentage.
     * @return the current grilling progress as a percentage.
     */
    @Override
    public float getGrillProgress() {
        return (accumulator / grillStepTime) * 100f;
    }

    @Override
    public Holdable getGrillResult() {
        return this;
    }

    @Override
    public boolean getGrilled() {
        return grilled;
    }

    public void setHalfGrilled(boolean value) {
        halfGrilled = value;
    }

    /**
     * Get the texture based on whether the lettuce has been chopped.
     *
     * @return the texture to display.
     */
    @Override
    public Texture getTexture() {
        String name = getType() + "_";

        if (!useable) {
            name = "burnt";
        } else if (!grilled) {
            name += "raw";
        } else {
            name += "grilled";
        }
        return textureManager.getTexture(name);
    }

    @Override
    public boolean getHalfGrilled() {
        return halfGrilled;
    }

    /**
     * @return {@code boolean} representing whether the ingredient is ready to be flipped or picked up.
     */
    @Override
    public boolean grillStepComplete() {
        return (accumulator >= grillStepTime);
    }

    /**
     * Flip the {@link BasicGrillable}. This action is done by the player and prevents the object from becoming burnt.
     */
    @Override
    public void flip() {
        accumulator = 0;
        flipped = true;
    }
}

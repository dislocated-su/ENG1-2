package cs.eng1.piazzapanic.food.ingredients;

import com.badlogic.gdx.graphics.Texture;
import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.PlayerState.PowerUp;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.interfaces.Grillable;
import cs.eng1.piazzapanic.food.interfaces.Holdable;

public class BasicGrillable extends Ingredient implements Grillable {

    private float accumulator = 0f;
    private final float grillStepTime = 2f;
    private final float failTime = 3f;

    private boolean halfGrilled = false;
    private boolean flipped = false;

    public BasicGrillable(String type, FoodTextureManager textureManager) {
        super(type, textureManager);
    }

    @Override
    public boolean grillTick(float delta) {
        accumulator += (delta *
                ((PlayerState.getInstance().getBuffActive(PowerUp.COOK_FAST))
                        ? 2
                        : 1));
        if (accumulator >= (grillStepTime +
                (failTime *
                        (PlayerState
                                .getInstance()
                                .getBuffActive(PowerUp.COOK_FAST)
                                        ? 2
                                        : 1)))
                &&
                !PlayerState.getInstance().getBuffActive(PowerUp.NO_SPOILING)) {
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
            name += "ruined";
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

    @Override
    public boolean grillStepComplete() {
        return (accumulator >= grillStepTime);
    }

    @Override
    public void flip() {
        accumulator = 0;
        flipped = true;
    }
}

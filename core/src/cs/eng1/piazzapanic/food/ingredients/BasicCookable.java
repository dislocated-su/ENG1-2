package cs.eng1.piazzapanic.food.ingredients;

import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.PlayerState.PowerUp;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.interfaces.Cookable;
import cs.eng1.piazzapanic.food.interfaces.Holdable;

public abstract class BasicCookable extends Ingredient implements Cookable {

    private float accumulator = 0f;
    private final float cookingStepTime = 2f;
    private final float failTime = 3f;

    private boolean halfCooked = false;
    private boolean flipped = false;

    public BasicCookable(String type, FoodTextureManager textureManager) {
        super(type, textureManager);
    }

    @Override
    public boolean cookingTick(float delta) {
        accumulator +=
            (
                delta *
                (
                    (
                            PlayerState
                                .getInstance()
                                .getBuffActive(PowerUp.DOUBLE_PREP_SPEED)
                        )
                        ? 2
                        : 1
                )
            );
        if (
            // fail time is doubled when double prep speed is active (since delta is doubled)

            accumulator >=
            (
                cookingStepTime +
                (
                    failTime *
                    (
                        PlayerState
                                .getInstance()
                                .getBuffActive(PowerUp.DOUBLE_PREP_SPEED)
                            ? 2
                            : 1
                    )
                )
            ) &&
            !PlayerState.getInstance().getBuffActive(PowerUp.NO_FAIL_PREP)
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

    @Override
    public Holdable getCookingResult() {
        return this;
    }

    @Override
    public boolean cookingStepComplete() {
        return (accumulator >= cookingStepTime);
    }

    @Override
    public float getCookingProgress() {
        return (accumulator / cookingStepTime) * 100f;
    }

    @Override
    public void flip() {
        accumulator = 0;
        flipped = true;
    }
}

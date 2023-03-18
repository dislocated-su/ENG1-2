package cs.eng1.tests;

import static org.junit.Assert.*;

import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.BasicCookable;
import cs.eng1.piazzapanic.food.ingredients.Potato;
import cs.eng1.piazzapanic.food.interfaces.Cookable;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class BasicCookableTests {

    FoodTextureManager foodTextureManager = new FoodTextureManager();
    Cookable potato = new Potato(foodTextureManager) {};

    /*
     * These test that cooking tick properly adds time to the Cookable and recognises when it is complete, as well as resetting when it is flipped.
     */
    @Test
    public void cookingTickTests() {
        assertFalse(
            "The BasicCookable should not be complete in half of its cooking time.",
            potato.cookingTick(1)
        );
        assertTrue(
            "When a basic cookable has been cooked for its cooking time, it should be fully cooked (pre flipping)",
            potato.cookingTick(1)
        );
        potato.flip();
        assertFalse(
            "When a basic cookable is flipped, it should refresh its cooking time.",
            potato.cookingTick(1)
        );
        assertTrue(
            "When a basic cookable has been flipped, it should be marked as fully cooked after being cooked again for its cooking time.",
            potato.cookingTick(1)
        );
    }

    /*
     * These test that the BasicCookable is properly assigned halfcooked and remain halfcooked after flipping.
     */
    @Test
    public void getHalfCookedTests() {
        assertFalse(
            "halfCooked should be false before cooking a BasicCookable.",
            potato.getHalfCooked()
        );
        potato.cookingTick(1);
        assertFalse(
            "halfCooked should be false before a BasicCookable has been flipped.",
            potato.getHalfCooked()
        );
        potato.cookingTick(1);
        assertTrue(
            "halfCooked should be true when a BasicCookable has cooked once for its cookingTime",
            potato.getHalfCooked()
        );
        potato.flip();
        assertTrue(
            "halfCooked should remain true after flipping a BasicCookable.",
            potato.getHalfCooked()
        );
    }

    /*
     * These test that getCookingResult returns whether or not a BasicCookable has been cooked by returning its Texture name
     */
    @Test
    public void getCookingResultTests() {
        assertEquals(
            "A BasicCookable should be uncooked before interacting with it.",
            "potato_raw",
            potato.getCookingResult().toString()
        );
        potato.cookingTick(1);
        assertEquals(
            "A BasicCookable should not be marked as cooked by progressing in cooking.",
            "potato_raw",
            potato.getCookingResult().toString()
        );
        potato.cookingTick(1);
        assertEquals(
            "A BasicCookable should not be marked as cooked by completing the half before flipping.",
            "potato_raw",
            potato.getCookingResult().toString()
        );
        potato.flip();
        assertEquals(
            "A BasicCookable should not be marked as cooked by flipping",
            "potato_raw",
            potato.getCookingResult().toString()
        );
        potato.cookingTick(1);
        assertEquals(
            "A BasicCookable should not be marked as cooked by progressing in cooking after being flipped.",
            "potato_raw",
            potato.getCookingResult().toString()
        );
        potato.cookingTick(1);
        assertEquals(
            "A BasicCookable should be considered cooked after completing its cookingTime after flipping.",
            "potato_cooked",
            potato.getCookingResult().toString()
        );
    }

    /*
     * These test that cookingStepComplete properly checks whether or not a BasicCookable has fully progressed to be flippable or be fully cooked.
     */
    @Test
    public void cookingStepCompleteTests() {
        assertFalse(
            "A BasicCookable should not flippable before cooking.",
            potato.cookingStepComplete()
        );
        potato.cookingTick(1);
        assertFalse(
            "A BasicCookable should not be flippable before cooking for its cookingTime",
            potato.cookingStepComplete()
        );
        potato.cookingTick(1);
        assertTrue(
            "A BasicCookable should be flippable after cooking for its cookingTime.",
            potato.cookingStepComplete()
        );
        potato.cookingTick(1);
        assertTrue(
            "A BasicCookable should remain flippable if it cooks for longer than its required time.",
            potato.cookingStepComplete()
        );
        potato.flip();
        assertFalse(
            "A BasicCookable should not be complete immediately after flipping.",
            potato.cookingStepComplete()
        );
        potato.cookingTick(1);
        assertFalse(
            "A BasicCookable should not be complete without cooking for its cookingTime after flipping.",
            potato.cookingStepComplete()
        );
        potato.cookingTick(1);
        assertTrue(
            "A BasicCookable should be complete after flipping, then cooking for its cookingTime",
            potato.cookingStepComplete()
        );
    }

    /*
     * These test that getCookingProgress properly returns the cooking progress of a BasicCookable in a percentage format, including over 100
     */
    @Test
    public void getCookingProgressTests() {
        assertEquals(
            "A BasicCookable should not be cooked to any amount before cooking it.",
            0,
            potato.getCookingProgress(),
            0.1
        );
        potato.cookingTick(1);
        assertEquals(
            "A BasicCookable should be half cooked after cooking for half of its cookingTime.",
            50,
            potato.getCookingProgress(),
            0.1
        );
        potato.cookingTick(1);
        assertEquals(
            "A BasicCookable should be fully cooked after cooking for its cookingTime.",
            100,
            potato.getCookingProgress(),
            0.1
        );
        potato.cookingTick(1);
        assertEquals(
            "A BasicCookable should continue cooking after reaching its cookingTime.",
            150,
            potato.getCookingProgress(),
            0.1
        );
        potato.flip();
        assertEquals(
            "A BasicCookable should be considered 'uncooked' after flipping.",
            0,
            potato.getCookingProgress(),
            0.1
        );
        potato.cookingTick(1);
        assertEquals(
            "A BasicCookable should be half cooked after cooking for half its cookingTime after flipping.",
            50,
            potato.getCookingProgress(),
            0.1
        );
        potato.cookingTick(1);
        assertEquals(
            "A BasicCookable should be fully cooked after cooking for its cookingTime after flipping.",
            100,
            potato.getCookingProgress(),
            0.1
        );
        potato.cookingTick(1);
        assertEquals(
            "A BasicCookable should continue cooking, even after being fully complete.",
            150,
            potato.getCookingProgress(),
            0.1
        );
    }

    /*
     * These test that flip properly flips the BasicCookable and fully reset the accumulator.
     */
    @Test
    public void flipTests() {
        potato.cookingTick(2);
        potato.flip();
        assertEquals(
            "After flipping, cooking progress should be 0.",
            0,
            potato.getCookingProgress(),
            0.1
        );
        assertFalse(
            "After flip is called on a BasicCookable, it should take the same amount of time to be cooked as it did to become flippable.",
            potato.cookingTick(1)
        );
    }
}

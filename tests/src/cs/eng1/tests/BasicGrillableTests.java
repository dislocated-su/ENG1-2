package cs.eng1.tests;

import static org.junit.Assert.*;

import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.BasicGrillable;
import cs.eng1.piazzapanic.food.interfaces.Grillable;
import org.junit.Test;
import org.junit.runner.RunWith;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.food.ingredients.Patty;

@RunWith(GdxTestRunner.class)
public class BasicGrillableTests {

    FoodTextureManager foodTextureManager = new FoodTextureManager();
    BasicGrillable patty = new Patty(foodTextureManager) {
    };

    @Test
    public void grillTickTests() {
        assertFalse(patty.grillTick(1));
        assertTrue(patty.grillTick(1));
        assertTrue(patty.getUseable());
        patty.flip();
        assertFalse(patty.grillTick(1));
        assertTrue(patty.grillTick(1));
        assertTrue(patty.getUseable());
        patty.grillTick(3);
        assertFalse(patty.getUseable());
    }

    @Test
    public void getGrillProgressTests() {
        assertEquals(
                "A BasicGrillable should not be grilled to any amount before cooking it.",
                0,
                patty.getGrillProgress(),
                0.1);
        patty.grillTick(1);
        assertEquals(
                "A BasicGrillable should be half grilled after cooking for half of its grillStepTime.",
                50,
                patty.getGrillProgress(),
                0.1);
        patty.grillTick(1);
        assertEquals(
                "A BasicGrillable should be fully grilled after cooking for its grillStepTime.",
                100,
                patty.getGrillProgress(),
                0.1);
        patty.grillTick(1);
        assertEquals(
                "A BasicGrillable should continue cooking after reaching its grillStepTime.",
                150,
                patty.getGrillProgress(),
                0.1);
        patty.flip();
        assertEquals(
                "A BasicGrillable should be considered 'ungrilled' after flipping.",
                0,
                patty.getGrillProgress(),
                0.1);
        patty.grillTick(1);
        assertEquals(
                "A BasicGrillable should be half grilled after cooking for half its grillStepTime after flipping.",
                50,
                patty.getGrillProgress(),
                0.1);
        patty.grillTick(1);
        assertEquals(
                "A BasicGrillable should be fully grilled after cooking for its grillStepTime after flipping.",
                100,
                patty.getGrillProgress(),
                0.1);
        patty.grillTick(1);
        assertEquals(
                "A BasicGrillable should continue cooking, even after being fully complete.",
                150,
                patty.getGrillProgress(),
                0.1);
    }

    @Test
    public void getGrillResultTests() {
        assertEquals(
                "A BasicGrillable should be ungrilled before interacting with it.",
                "patty_raw",
                patty.getGrillResult().toString());
        patty.grillTick(1);
        assertEquals(
                "A BasicGrillable should not be marked as grilled by progressing in cooking.",
                "patty_raw",
                patty.getGrillResult().toString());
        patty.grillTick(1);
        assertEquals(
                "A BasicGrillable should not be marked as grilled by completing the half before flipping.",
                "patty_raw",
                patty.getGrillResult().toString());
        patty.flip();
        assertEquals(
                "A BasicGrillable should not be marked as grilled by flipping",
                "patty_raw",
                patty.getGrillResult().toString());
        patty.grillTick(1);
        assertEquals(
                "A BasicGrillable should not be marked as grilled by progressing in cooking after being flipped.",
                "patty_raw",
                patty.getGrillResult().toString());
        patty.grillTick(1);
        assertEquals(
                "A BasicGrillable should be considered grilled after completing its grillStepTime after flipping.",
                "patty_grilled",
                patty.getGrillResult().toString());
        patty.grillTick(1);
        assertEquals("patty_grilled", patty.getGrillResult().toString());
        patty.grillTick(2);
        assertEquals("patty_ruined", patty.getGrillResult().toString());
    }
}

package cs.eng1.tests.food.ingredients;

import static org.junit.Assert.*;

import com.badlogic.gdx.graphics.Texture;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.BasicGrillable;
import cs.eng1.piazzapanic.food.ingredients.Patty;
import cs.eng1.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the functionality of the abstract class BasicGrillable - being grillTick adding grill progress,
 * getGrillProgress / getGrillResult / getGrilled / getHalfGrilled / grillStepComplete returning the proper step in grilling,
 * flip() flipping the Grillable.
 *
 * @author Joel Paxman
 */
@RunWith(GdxTestRunner.class)
public class BasicGrillableTests {

    FoodTextureManager foodTextureManager = new FoodTextureManager();
    BasicGrillable patty = new Patty(foodTextureManager) {};

    /**
     * Asserts grillTick progresses time and the grillable cooks / burns as expected
     */
    @Test
    public void grillTickTests() {
        assertFalse("The BasicGrillable should not be ready to flip immediately", patty.grillTick(1));
        assertTrue("The BasicGrillable should be flippable after cooking for its grillStepTime", patty.grillTick(4));
        assertTrue(
            "The BasicGrillable should remain flippable even after cooking for more than its grillStepTime",
            patty.grillTick(1)
        );
        assertTrue("The BasicGrillable should not be ruined after grilling for its grillStepTime", patty.getUseable());
        patty.flip();
        assertFalse(patty.grillTick(4));
        assertTrue(patty.grillTick(4));
        assertTrue(patty.getUseable());
        patty.grillTick(3);
        assertFalse(patty.getUseable());
    }

    /**
     * Asserts getGrillProgress returns a percentage progress of the grilling
     */
    @Test
    public void getGrillProgressTests() {
        assertEquals(
            "A BasicGrillable should not be grilled to any amount before grilling it.",
            0,
            patty.getGrillProgress(),
            0.1
        );
        patty.grillTick(2.5f);
        assertEquals(
            "A BasicGrillable should be half grilled after grilling for half of its grillStepTime.",
            50,
            patty.getGrillProgress(),
            0.1
        );
        patty.grillTick(2.5f);
        assertEquals(
            "A BasicGrillable should be fully grilled after grilling for its grillStepTime.",
            100,
            patty.getGrillProgress(),
            0.1
        );
        patty.grillTick(2.5f);
        assertEquals(
            "A BasicGrillable should continue grilling after reaching its grillStepTime.",
            150,
            patty.getGrillProgress(),
            0.1
        );
        patty.flip();
        assertEquals(
            "A BasicGrillable should be considered 'ungrilled' after flipping.",
            0,
            patty.getGrillProgress(),
            0.1
        );
        patty.grillTick(2.5f);
        assertEquals(
            "A BasicGrillable should be half grilled after grilling for half its grillStepTime after flipping.",
            50,
            patty.getGrillProgress(),
            0.1
        );
        patty.grillTick(2.5f);
        assertEquals(
            "A BasicGrillable should be fully grilled after grilling for its grillStepTime after flipping.",
            100,
            patty.getGrillProgress(),
            0.1
        );
        patty.grillTick(2.5f);
        assertEquals(
            "A BasicGrillable should continue grilling, even after being fully complete.",
            150,
            patty.getGrillProgress(),
            0.1
        );
    }

    /**
     * Assert getGrillResults returns the right texture names
     */
    @Test
    public void getGrillResultTests() {
        assertEquals(
            "A BasicGrillable should be ungrilled before interacting with it.",
            "patty_raw",
            patty.getGrillResult().toString()
        );
        patty.grillTick(1);
        assertEquals(
            "A BasicGrillable should not be marked as grilled by progressing in grilling.",
            "patty_raw",
            patty.getGrillResult().toString()
        );
        patty.grillTick(1);
        assertEquals(
            "A BasicGrillable should not be marked as grilled by completing the half before flipping.",
            "patty_raw",
            patty.getGrillResult().toString()
        );
        patty.flip();
        assertEquals(
            "A BasicGrillable should not be marked as grilled by flipping",
            "patty_raw",
            patty.getGrillResult().toString()
        );
        patty.grillTick(1);
        assertEquals(
            "A BasicGrillable should not be marked as grilled by progressing in grilling after being flipped.",
            "patty_raw",
            patty.getGrillResult().toString()
        );
        patty.grillTick(4f);
        patty.grillTick(3f);
        assertEquals(
            "A BasicGrillable should be considered grilled after completing its grillStepTime after flipping.",
            "patty_grilled",
            patty.getGrillResult().toString()
        );
        patty.grillTick(1);
        assertEquals(
            "A BasicGrillable should be considered grilled even if it continues cooking after being finished.",
            "patty_grilled",
            patty.getGrillResult().toString()
        );
        patty.grillTick(4f);
        assertEquals(
            "A BasicGrillable should be ruined after being fully cooked and continuing cooking for too long.",
            "patty_ruined",
            patty.getGrillResult().toString()
        );
        patty.grillTick(400);
        assertEquals(
            "A BasicGrillable should remain ruined for as long as it keeps being cooked after being ruined.",
            "patty_ruined",
            patty.getGrillResult().toString()
        );
    }

    /**
     * Assert getGrilled returns the grilled value
     */
    @Test
    public void getGrilledTests() {
        assertFalse(patty.getGrilled());
        patty.grillTick(5f);
        assertFalse(patty.getGrilled());
        patty.flip();
        assertFalse(patty.getGrilled());
        patty.grillTick(5f);
        assertTrue(patty.getGrilled());
    }

    /**
     * Assert getTexture returns the filename of the texture of the
     * BasicGrillable in it's current state
     */
    @Test
    public void getTextureTests() {
        final Texture rawPatty = foodTextureManager.getTexture("patty_raw");
        final Texture cookedPatty = foodTextureManager.getTexture("patty_grilled");
        final Texture ruinedPatty = foodTextureManager.getTexture("burnt");
        assertEquals("The BasicGrillable texture should be += _raw, as it is ungrilled", rawPatty, patty.getTexture());
        patty.grillTick(5f);
        assertEquals(
            "The BasicGrillable texture should be += _raw, as it is not fully grilled",
            rawPatty,
            patty.getTexture()
        );
        patty.flip();
        assertEquals(
            "The BasicGrillable texture should be += _raw, as it is not fully grilled",
            rawPatty,
            patty.getTexture()
        );
        patty.grillTick(5f);
        assertEquals(
            "The BasicGrillable texture should be += _grilled, as it has been fully grilled",
            cookedPatty,
            patty.getTexture()
        );
        patty.grillTick(6f);
        assertEquals(
            "The BasicGrillable texture should be burnt, as it has been grilled for too long",
            ruinedPatty,
            patty.getTexture()
        );
        patty.grillTick(400);
        assertEquals(
            "The BasicGrillable texture should still be +=_ruined, even after grilling for a lot longer",
            ruinedPatty,
            patty.getTexture()
        );
    }

    /**
     * Assert getHalfGrilled properly returns the halfGrilled value
     */
    @Test
    public void getHalfGrilledTests() {
        assertFalse(patty.getHalfGrilled());
        patty.grillTick(2.5f);
        assertFalse(patty.getHalfGrilled());
        patty.grillTick(2.5f);
        assertTrue(patty.getHalfGrilled());
        patty.flip();
        assertTrue(patty.getHalfGrilled());
        patty.grillTick(2.5f);
        assertTrue(patty.getHalfGrilled());
    }

    /**
     * Assert grillStepComplete returns if the BasicGrillable is
     * interactable to either flip or pick up.
     */
    @Test
    public void grillStepCompleteTests() {
        assertFalse(
            "A BasicGrillable shouldn't have a complete grill step without being grilled at all.",
            patty.grillStepComplete()
        );
        patty.grillTick(2.5f);
        assertFalse(
            "A BasicGrillable shouldn't have a complete grill step without being grilled for the full grillStep time.",
            patty.grillStepComplete()
        );
        patty.grillTick(2.5f);
        assertTrue(
            "A BasicGrillable should have a complete grill step after being grilled for the full grillStep time.",
            patty.grillStepComplete()
        );
        patty.grillTick(1);
        assertTrue(
            "A BasicGrillable should have a complete grill step after being grilled for more than the full grillStep time.",
            patty.grillStepComplete()
        );
        patty.flip();
        assertFalse(
            "A BasicGrillable shouldn't have a complete grill step without being grilled at all after flipping.",
            patty.grillStepComplete()
        );
        patty.grillTick(1f);
        assertFalse(
            "A BasicGrillable shouldn't have a complete grill step without being grilled for a grillStep after flipping.",
            patty.grillStepComplete()
        );
        patty.grillTick(4f);
        assertTrue(
            "A BasicGrillable should have a complete grill step after being grilled for the full grillStep time.",
            patty.grillStepComplete()
        );
        patty.grillTick(1);
        assertTrue(
            "A BasicGrillable should have a complete grill step after being grilled for more than the full grillStep time.",
            patty.grillStepComplete()
        );
    }

    /**
     * Assert flip() flips the grillable.
     */
    @Test
    public void flipTests() {
        patty.grillTick(2);
        patty.flip();
        assertEquals("After flipping, grill progress should be 0.", 0, patty.getGrillProgress(), 0.1);
        assertFalse(
            "After flip is called on a BasicGrilable, it should take the same amount of time to be cooked as it did to become flippable.",
            patty.grillTick(1)
        );
    }
}

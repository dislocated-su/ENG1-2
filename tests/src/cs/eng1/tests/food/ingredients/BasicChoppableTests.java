package cs.eng1.tests.food.ingredients;

import static org.junit.Assert.*;

import com.badlogic.gdx.graphics.Texture;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.BasicChoppable;
import cs.eng1.piazzapanic.food.ingredients.Tomato;
import cs.eng1.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class BasicChoppableTests {

    FoodTextureManager foodTextureManager = new FoodTextureManager();
    BasicChoppable tomato = new Tomato(foodTextureManager) {};

    /**
     * choppingTick returns correct chopping status
     */
    @Test
    public void choppingTickTests() {
        assertFalse(
            "When accumulator ticks for delta = one, it shouldn't be considered chopped.",
            tomato.choppingTick(1)
        );
        assertTrue(
            "When accumulator ticks and becomes equal to chopTime, it should become chopped.",
            tomato.choppingTick(1)
        );
        assertTrue(
            "When accumulator ticks and the choppable has already been chopped, it should still be considered chopped",
            tomato.choppingTick(1)
        );
        assertFalse(
            "When a BasicChoppable is chopped for too long, it should become unuseable.",
            tomato.choppingTick(3)
        );
    }

    /**
     * getChoppingProgress returns correct chopping percentages
     */
    @Test
    public void getChoppingProgressTests() {
        assertEquals(
            "No progress has been made on chopping, so chopping progress should be 0",
            0,
            tomato.getChoppingProgress(),
            0.01
        );
        tomato.choppingTick(1);
        assertEquals(
            "Chopping is half done, so chopping progress should be 50",
            50,
            tomato.getChoppingProgress(),
            0.01
        );
        tomato.choppingTick(1);
        assertEquals(
            "Chopping is done, so chopping progress should be 100",
            100,
            tomato.getChoppingProgress(),
            0.01
        );
        tomato.choppingTick(1);
        assertEquals(
            "Chopping should continue to progress, even after the BasicChoppable is fully chopped.",
            150,
            tomato.getChoppingProgress(),
            0.01
        );
    }

    /**
     * getChoppingResult checked to add correct tags (via .toString() of the return
     * Holdable)
     */
    @Test
    public void getChoppingResultTests() {
        assertEquals(
            "The chopping result of tomato should be +=_raw, as no progress has been made.",
            "tomato_raw",
            tomato.getChoppingResult().toString()
        );
        tomato.choppingTick(1);
        assertEquals(
            "The chopping result of tomato should be +=_raw, as progress has only been partially made.",
            "tomato_raw",
            tomato.getChoppingResult().toString()
        );
        tomato.choppingTick(1);
        assertEquals(
            "The chopping result of tomato should be +=_chopped, as progress has finished.",
            "tomato_chopped",
            tomato.getChoppingResult().toString()
        );
        tomato.choppingTick(1);
        assertEquals(
            "The chopping result of tomato should be +=_chopped, as progress has already finished.",
            "tomato_chopped",
            tomato.getChoppingResult().toString()
        );
        tomato.choppingTick(2);
        assertEquals(
            "The BasicCookable has been chopped for too long, so the result should be +=_ruined.",
            "tomato_ruined",
            tomato.getChoppingResult().toString()
        );
        tomato.choppingTick(400);
        assertEquals(
            "The BasicCookable has been chopped for too long, so the result should be +=_ruined and should remain this way indefinitely.",
            "tomato_ruined",
            tomato.getChoppingResult().toString()
        );
    }

    /**
     * getTexture returns correct texture based on whether the choppable is chopped
     * or not.
     */
    @Test
    public void getChoppedTests() {
        assertFalse(
            "A BasicChoppable should not become chopped immediately,",
            tomato.getChopped()
        );
        tomato.choppingTick(1);
        assertFalse(
            "A BasicChoppable should not be chopped before it has chopped for its chopTime.",
            tomato.getChopped()
        );
        tomato.choppingTick(1);
        assertTrue(
            "A BasicChoppable should become chopped after chopping for its chopTime.",
            tomato.getChopped()
        );
        tomato.choppingTick(3);
        assertTrue(
            "A BasicChoppable should be ruined after chopping for too long but remain chopped.",
            (!tomato.getUseable()) && tomato.getChopped()
        );
        tomato.choppingTick(400);
        assertTrue(
            "A BasicChoppable should remain indefinitely chopped after becoming chopped.",
            tomato.getChopped()
        );
    }

    /**
     * getTexture returns correct texture based on whether the choppable is chopped
     * or not.
     */
    @Test
    public void getTextureTests() {
        final Texture rawTomato = foodTextureManager.getTexture("tomato_raw");
        final Texture choppedTomato = foodTextureManager.getTexture(
            "tomato_chopped"
        );
        final Texture ruinedTomato = foodTextureManager.getTexture("rotten");
        assertEquals(
            "The BasicChoppable texture should be += _raw, as it is unchopped.",
            rawTomato,
            tomato.getTexture()
        );
        tomato.choppingTick(1);
        assertEquals(
            "The BasicChoppable texture should be += _raw, as it is unchopped.",
            rawTomato,
            tomato.getTexture()
        );
        tomato.choppingTick(1);
        assertEquals(
            "The BasicChoppable texture should be += _chopped, as it has been chopped.",
            choppedTomato,
            tomato.getTexture()
        );
        tomato.choppingTick(1);

        assertEquals(
            "The BasicChoppable texture should be += _chopped, as it has been chopped.",
            choppedTomato,
            tomato.getTexture()
        );
        tomato.choppingTick(1);
        assertEquals(
            "The BasicChoppable texture should be += _chopped, as it has been chopped.",
            choppedTomato,
            tomato.getTexture()
        );
        tomato.choppingTick(1);
        assertEquals(
            "The BasicChoppable texture should be rotten, as it has been ruined.",
            ruinedTomato,
            tomato.getTexture()
        );
        tomato.choppingTick(400);
        assertEquals(
            "The BasicChoppable texture should stay ruined indefinitely.",
            ruinedTomato,
            tomato.getTexture()
        );
    }
}

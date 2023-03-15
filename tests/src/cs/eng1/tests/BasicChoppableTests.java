package cs.eng1.tests;

import static org.junit.Assert.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.BasicChoppable;
import cs.eng1.piazzapanic.food.interfaces.Choppable;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class BasicChoppableTests {

    FoodTextureManager foodTextureManager = new FoodTextureManager();
    Choppable tomato = new BasicChoppable("tomato", foodTextureManager) {};

    // These tests make sure the chopping ticks work correctly
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
    }

    // These test chopping progress returns percentage results for how far the
    // ingredient has been chopped
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
    }

    // These test when the ingredients are marked as chopped and raw based on progress
    @Test
    public void getChoppingResultTests() {
        assertEquals(
            "The chopping result of tomato should be +=_raw, as no progress has been made",
            "tomato_raw",
            tomato.getChoppingResult().toString()
        );
        tomato.choppingTick(1);
        assertEquals(
            "The chopping result of tomato should be +=_raw, as progress has only been partially made",
            "tomato_raw",
            tomato.getChoppingResult().toString()
        );
        tomato.choppingTick(1);
        assertEquals(
            "The chopping result of tomato should be +=_chopped, as progress has finished",
            "tomato_chopped",
            tomato.getChoppingResult().toString()
        );
        tomato.choppingTick(1);
        assertEquals(
            "The chopping result of tomato should be +=_chopped, as progress has already finished",
            "tomato_chopped",
            tomato.getChoppingResult().toString()
        );
    }

    // These test chopped becomes true when accumulator = chopTime
    @Test
    public void getChoppedTests() {
        assertFalse(
            "Tomato shouldn't be chopped, as no ticks have happened",
            tomato.getChopped()
        );
        tomato.choppingTick(2);
        assertTrue(
            "Tomato should be chopped, as accumulator = chopTime",
            tomato.getChopped()
        );
    }

    @Test
    public void getTextureTests() {
        final Texture rawTomato = foodTextureManager.getTexture("tomato_raw");
        final Texture choppedTomato = foodTextureManager.getTexture(
            "tomato_chopped"
        );
        assertEquals(
            "The tomato texture should be tomato_raw, as it is unchopped",
            rawTomato,
            tomato.getTexture()
        );
        tomato.choppingTick(2);
        assertEquals(
            "The tomato texture should be tomato_chopped, as it has been chopped",
            choppedTomato,
            tomato.getTexture()
        );
    }
}

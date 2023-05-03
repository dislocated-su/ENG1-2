package cs.eng1.tests.assets;

import static org.junit.Assert.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Asserts the existence of every image
 */
@RunWith(GdxTestRunner.class)
public class AssetTests {

    /**
     * This test is here to make sure the testing environment is not broken.
     */
    @Test
    public void alwaysTrueTest() {
        assertTrue("This test should pass every time.", true);
    }

    /**
     * Asserts the existence of a cookedPatty image.
     */
    @Test
    public void cookedPattyAssetTest() {
        assertTrue(
            "Passes if cooked_patty.png exists",
            Gdx.files.internal("food/buff_chef/grilled_patty.png").exists()
        );
    }

    /**
     * Asserts the existence of an uncooked patty image.
     */
    @Test
    public void uncookedPattyAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/formed_patty.png exists",
            Gdx.files.internal("food/buff_chef/formed_patty.png").exists()
        );
    }

    /**
     * Asserts the existence of a lettuce image.
     */
    @Test
    public void lettuceAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/lettuce.png exists",
            Gdx.files.internal("food/buff_chef/lettuce.png").exists()
        );
    }

    /**
     * Asserts the existence of a chopped lettuce image.
     */
    @Test
    public void lettuceChoppedAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/sliced_lettuce.png exists",
            Gdx.files.internal("food/buff_chef/sliced_lettuce.png").exists()
        );
    }

    /**
     * Asserts the existence of a tomato image.
     */
    @Test
    public void tomatoAssetTest() {
        assertTrue("Passes if food/buff_chef/tomato.png", Gdx.files.internal("food/buff_chef/tomato.png").exists());
    }

    /**
     * Asserts the existence of a chopped tomato image.
     */
    @Test
    public void tomatoChoppedAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/sliced_tomato.png exists",
            Gdx.files.internal("food/buff_chef/sliced_tomato.png").exists()
        );
    }

    /**
     * Asserts the existence of a cheese image.
     */
    @Test
    public void cheeseAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/cheese.png exists",
            Gdx.files.internal("food/buff_chef/cheese.png").exists()
        );
    }

    /**
     * Asserts the existence of a dough image.
     */
    @Test
    public void doughAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/dough.png exists",
            Gdx.files.internal("food/buff_chef/dough.png").exists()
        );
    }

    /**
     * Asserts the existence of a potato image.
     */
    @Test
    public void potatoAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/potato.png exists",
            Gdx.files.internal("food/buff_chef/potato.png").exists()
        );
    }

    /**
     * Asserts the existence of an uncooked pizza image.
     */
    @Test
    public void uncookedPizzaAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/formed_pizza.png exists",
            Gdx.files.internal("food/buff_chef/formed_pizza.png").exists()
        );
    }

    /**
     * Asserts the existence of a cooked pizza image.
     */
    @Test
    public void pizzaAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/cooked_pizza.png exists",
            Gdx.files.internal("food/buff_chef/cooked_pizza.png").exists()
        );
    }

    /**
     * Asserts the existence of a jacket potato image.
     */
    @Test
    public void jacketPotatoAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/jacket_potato.png exists",
            Gdx.files.internal("food/buff_chef/jacket_potato.png").exists()
        );
    }

    /**
     * Asserts the existence of a bun image.
     */
    @Test
    public void bunAssetTest() {
        assertTrue("Passes if food/buff_chef/buns.png exists", Gdx.files.internal("food/buff_chef/buns.png").exists());
    }

    /**
     * Asserts the existence of a complete burger image.
     */
    @Test
    public void burgerAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/burger.png exists",
            Gdx.files.internal("food/buff_chef/burger.png").exists()
        );
    }

    /**
     * Asserts the existence of a salad image.
     */
    @Test
    public void saladAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/salad.png exists",
            Gdx.files.internal("food/buff_chef/salad.png").exists()
        );
    }

    /**
     * Asserts the existence of a cooked potato image.
     */
    public void cookedPotatoTest() {
        assertTrue(
            "Passes if food/buff_chef/cooked_potato.png exists",
            Gdx.files.internal("food/buff_chef/cooked_potato.png").exists()
        );
    }

    /**
     * Asserts the existence of a rotten image.
     */
    public void rottenTest() {
        assertTrue(
            "Passes if food/original/rotten.png exists",
            Gdx.files.internal("food/original/rotten.png").exists()
        );
    }

    /**
     * Asserts the existence of a burnt image.
     */
    public void burntTest() {
        assertTrue("Passes if food/original/burnt.png exists", Gdx.files.internal("food/original/burnt.png").exists());
    }

    /**
     * Asserts the existence of badlogic.
     */
    @Test
    public void badlogicTest() {
        assertTrue("Passes if badlogic.jpg exists", Gdx.files.internal("badlogic.jpg").exists());
    }

    FoodTextureManager foodTextureManager = new FoodTextureManager();

    /**
     * Asserts the fetch of a fake texture returns badlogic.jpg
     */
    @Test
    public void falseTextureTest() {
        Texture falseTexture = foodTextureManager.getTexture("not_real");
        assertEquals(
            "A false foodType should return badlogic.jpg",
            "badlogic.jpg",
            falseTexture.getTextureData().toString()
        );
    }

    /**
     * Asserts function of fetching a patty.
     */
    @Test
    public void cookedPattyFetchTest() {
        Texture cookedPattyTexture = foodTextureManager.getTexture("patty_grilled");
        assertEquals(
            "The foodType should return the texture cooked_patty.png",
            "food/buff_chef/grilled_patty.png",
            cookedPattyTexture.getTextureData().toString()
        );
    }

    /**
     * Asserts function of fetching an uncooked patty.
     */
    @Test
    public void uncookedPattyFetchTest() {
        Texture uncookedpattyTexture = foodTextureManager.getTexture("patty_raw");
        assertEquals(
            "The foodType should return the texture uncooked_patty.png",
            "food/buff_chef/formed_patty.png",
            uncookedpattyTexture.getTextureData().toString()
        );
    }

    /**
     * Asserts function of fetching lettuce.
     */
    @Test
    public void lettuceFetchTest() {
        Texture lettuceTexture = foodTextureManager.getTexture("lettuce_raw");
        assertEquals(
            "The foodType should return the texture lettuce.png",
            "food/buff_chef/lettuce.png",
            lettuceTexture.getTextureData().toString()
        );
    }

    /**
     * Asserts function of fetching a tomato.
     */
    @Test
    public void tomatoFetchTest() {
        Texture tomatoRawTexture = foodTextureManager.getTexture("tomato_raw");
        assertEquals(
            "The foodType should return the texture tomato.png",
            "food/buff_chef/tomato.png",
            tomatoRawTexture.getTextureData().toString()
        );
    }

    /**
     * Asserts function of fetching a chopped tomato
     */
    @Test
    public void tomatoChoppedFetchTest() {
        Texture tomatoChoppedTexture = foodTextureManager.getTexture("tomato_chopped");
        assertEquals(
            "The foodType should return the texture tomato_chopped.png ",
            "food/buff_chef/sliced_tomato.png",
            tomatoChoppedTexture.getTextureData().toString()
        );
    }

    /**
     * Asserts function of fetching a bun.
     */
    @Test
    public void bunFetchTest() {
        Texture bunTexture = foodTextureManager.getTexture("bun");
        assertEquals(
            "The foodType should return the texure buns.png",
            "food/buff_chef/buns.png",
            bunTexture.getTextureData().toString()
        );
    }

    /**
     * Asserts function of fetching a finished burger.
     */
    @Test
    public void sandwichBurgerFetchTest() {
        Texture burgerTexture = foodTextureManager.getTexture("burger");
        assertEquals(
            "The foodType should return the texture burger.png ",
            "food/buff_chef/burger.png",
            burgerTexture.getTextureData().toString()
        );
    }

    /**
     * Asserts function of fetching a salad.
     */
    @Test
    public void saladFetchTest() {
        Texture saladTexture = foodTextureManager.getTexture("salad");
        assertEquals(
            "The foodType should return the texture salad.png",
            "food/buff_chef/salad.png",
            saladTexture.getTextureData().toString()
        );
    }

    /**
     * Asserts function of fetching a cooked pizza.
     */
    @Test
    public void pizzaFetchTest() {
        Texture pizzaTexture = foodTextureManager.getTexture("pizza");
        assertEquals(
            "The foodType should return the texture cooked_pizza.png",
            "food/buff_chef/cooked_pizza.png",
            pizzaTexture.getTextureData().toString()
        );
    }

    /**
     * Asserts function of fetching a pizza.
     */
    @Test
    public void uncookedPizzaFetchTest() {
        Texture uncookedPizzaTexture = foodTextureManager.getTexture("uncooked_pizza");
        assertEquals(
            "The foodType should return the texture formed_pizza.png",
            "food/buff_chef/formed_pizza.png",
            uncookedPizzaTexture.getTextureData().toString()
        );
    }

    /**
     * Asserts function of fetching a jacket potato.
     */
    @Test
    public void jacketPotatoFetchTest() {
        Texture jacketPotatoTexture = foodTextureManager.getTexture("jacket_potato");
        assertEquals(
            "The foodType should return the texture jacket_potato.png",
            "food/buff_chef/jacket_potato.png",
            jacketPotatoTexture.getTextureData().toString()
        );
    }

    /**
     * Asserts function of fetching a potato.
     */
    @Test
    public void potatoFetchTest() {
        Texture potatoTexture = foodTextureManager.getTexture("potato");
        assertEquals(
            "The foodType should return the texture potato.png",
            "food/buff_chef/potato.png",
            potatoTexture.getTextureData().toString()
        );
    }

    /**
     * Asserts function of fetching dough.
     */
    @Test
    public void doughFetchTest() {
        Texture doughTexture = foodTextureManager.getTexture("dough");
        assertEquals(
            "The foodType should return the texture dough.png",
            "food/buff_chef/dough.png",
            doughTexture.getTextureData().toString()
        );
    }

    /**
     * Asserts function of fetching cheese.
     */
    @Test
    public void cheeseFetchTest() {
        Texture cheeseTexture = foodTextureManager.getTexture("cheese");
        assertEquals(
            "The foodType should return the texture cheese.png",
            "food/buff_chef/cheese.png",
            cheeseTexture.getTextureData().toString()
        );
    }
}

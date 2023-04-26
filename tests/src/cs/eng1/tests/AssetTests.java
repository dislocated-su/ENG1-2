package cs.eng1.tests;

import static org.junit.Assert.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class AssetTests {

    @Test
    /**
     * This test is here to make sure the testing environment is not broken.
     */
    public void alwaysTrueTest() {
        assertTrue("This test should pass every time", true);
    }

    @Test
    public void cookedPattyAssetTest() {
        // Checks if "cooked_patty.png" exists
        assertTrue(
            "Passes if cooked_patty.png exists",
            Gdx.files.internal("food/buff_chef/grilled_patty.png").exists()
        );
    }

    @Test
    public void uncookedPattyAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/formed_patty.png exists",
            Gdx.files.internal("food/buff_chef/formed_patty.png").exists()
        );
    }

    @Test
    public void lettuceAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/lettuce.png exists",
            Gdx.files.internal("food/buff_chef/lettuce.png").exists()
        );
    }

    @Test
    public void lettuceChoppedAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/sliced_lettuce.png exists",
            Gdx.files.internal("food/buff_chef/sliced_lettuce.png").exists()
        );
    }

    @Test
    public void tomatoAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/tomato.png",
            Gdx.files.internal("food/buff_chef/tomato.png").exists()
        );
    }

    @Test
    public void tomatoChoppedAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/sliced_tomato.png exists",
            Gdx.files.internal("food/buff_chef/sliced_tomato.png").exists()
        );
    }

    @Test
    public void cheeseAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/cheese.png exists",
            Gdx.files.internal("food/buff_chef/cheese.png").exists()
        );
    }

    @Test
    public void doughAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/dough.png exists",
            Gdx.files.internal("food/buff_chef/dough.png").exists()
        );
    }

    @Test
    public void potatoAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/potato.png exists",
            Gdx.files.internal("food/buff_chef/potato.png").exists()
        );
    }

    @Test
    public void uncookedPizzaAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/formed_pizza.png exists",
            Gdx.files.internal("food/buff_chef/formed_pizza.png").exists()
        );
    }

    @Test
    public void pizzaAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/cooked_pizza.png exists",
            Gdx.files.internal("food/buff_chef/cooked_pizza.png").exists()
        );
    }

    @Test
    public void jacketPotatoAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/jacket_potato.png exists",
            Gdx.files.internal("food/buff_chef/jacket_potato.png").exists()
        );
    }

    @Test
    public void bunAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/buns.png exists",
            Gdx.files.internal("food/buff_chef/buns.png").exists()
        );
    }

    @Test
    public void burgerAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/burger.png exists",
            Gdx.files.internal("food/buff_chef/burger.png").exists()
        );
    }

    @Test
    public void saladAssetTest() {
        assertTrue(
            "Passes if food/buff_chef/salad.png exists",
            Gdx.files.internal("food/buff_chef/salad.png").exists()
        );
    }

    public void cookedPotatoTest() {
        assertTrue(
            "Passes if food/buff_chef/cooked_potato.png",
            Gdx.files.internal("food/buff_chef/cooked_potato.png").exists()
        );
    }

    @Test
    public void badlogicTest() {
        assertTrue(
            "Passes if badlogic.jpg exists",
            Gdx.files.internal("badlogic.jpg").exists()
        );
    }

    // This checks the foodType fetches the correct texture in FoodTextureManager
    FoodTextureManager foodTextureManager = new FoodTextureManager();

    @Test
    public void falseTextureTest() {
        Texture falseTexture = foodTextureManager.getTexture("not_real");
        assertEquals(
            "A false foodType should return badlogic.jpg",
            "badlogic.jpg",
            falseTexture.getTextureData().toString()
        );
    }

    @Test
    public void cookedPattyFetchTest() {
        Texture cookedPattyTexture = foodTextureManager.getTexture(
            "patty_grilled"
        );
        TextureData texturedata = cookedPattyTexture.getTextureData();
        if (texturedata instanceof FileTextureData) {
            assertEquals(
                "The foodType should return the texture cooked_patty.png",
                "food/buff_chef/grilled_patty.png",
                ((FileTextureData) texturedata).getFileHandle().path()
            );
        } else {
            fail("Failed to load texture.");
        }
    }

    @Test
    public void uncookedPattyFetchTest() {
        Texture uncookedpattyTexture = foodTextureManager.getTexture(
            "patty_raw"
        );
        assertEquals(
            "The foodType should return the texture uncooked_patty.png",
            "food/buff_chef/formed_patty.png",
            uncookedpattyTexture.getTextureData().toString()
        );
    }

    @Test
    public void lettuceFetchTest() {
        Texture lettuceTexture = foodTextureManager.getTexture("lettuce_raw");
        assertEquals(
            "The foodType should return the texture lettuce.png",
            "food/buff_chef/lettuce.png",
            lettuceTexture.getTextureData().toString()
        );
    }

    @Test
    public void tomatoFetchTest() {
        Texture tomatoRawTexture = foodTextureManager.getTexture("tomato_raw");
        assertEquals(
            "The foodType should return the texture tomato.png",
            "food/buff_chef/tomato.png",
            tomatoRawTexture.getTextureData().toString()
        );
    }

    @Test
    public void tomatoChoppedPattyFetchTest() {
        Texture tomatoChoppedTexture = foodTextureManager.getTexture(
            "tomato_chopped"
        );
        assertEquals(
            "The foodType should return the texture tomato_chopped.png ",
            "food/buff_chef/sliced_tomato.png",
            tomatoChoppedTexture.getTextureData().toString()
        );
    }

    @Test
    public void bunFetchTest() {
        Texture bunTexture = foodTextureManager.getTexture("bun");
        assertEquals(
            "The foodType should return the texure buns.png",
            "food/buff_chef/buns.png",
            bunTexture.getTextureData().toString()
        );
    }

    @Test
    public void sandwichBurgerFetchTest() {
        Texture burgerTexture = foodTextureManager.getTexture("burger");
        assertEquals(
            "The foodType should return the texture burger.png ",
            "food/buff_chef/burger.png",
            burgerTexture.getTextureData().toString()
        );
    }

    @Test
    public void saladFetchTest() {
        Texture saladTexture = foodTextureManager.getTexture("salad");
        assertEquals(
            "The foodType should return the texture salad.png",
            "food/buff_chef/salad.png",
            saladTexture.getTextureData().toString()
        );
    }

    @Test
    public void pizzaFetchTest() {
        Texture pizzaTexture = foodTextureManager.getTexture("pizza");
        assertEquals(
            "The foodType should return the texture cooked_pizza.png",
            "food/buff_chef/cooked_pizza.png",
            pizzaTexture.getTextureData().toString()
        );
    }

    @Test
    public void uncookedPizzaFetchTest() {
        Texture uncookedPizzaTexture = foodTextureManager.getTexture(
            "uncooked_pizza"
        );
        assertEquals(
            "The foodType should return the texture fomed_pizza.png",
            "food/buff_chef/formed_pizza.png",
            uncookedPizzaTexture.getTextureData().toString()
        );
    }

    @Test
    public void jacketPotatoFetchTest() {
        Texture jacketPotatoTexture = foodTextureManager.getTexture(
            "jacket_potato"
        );
        assertEquals(
            "The foodType should return the texture jacket_potato.png",
            "food/buff_chef/jacket_potato.png",
            jacketPotatoTexture.getTextureData().toString()
        );
    }

    @Test
    public void potatoFetchTest() {
        Texture potatoTexture = foodTextureManager.getTexture("potato");
        assertEquals(
            "The foodType should return the texture potato.png",
            "food/buff_chef/potato.png",
            potatoTexture.getTextureData().toString()
        );
    }

    @Test
    public void doughFetchTest() {
        Texture doughTexture = foodTextureManager.getTexture("dough");
        assertEquals(
            "The foodType should return the texture dough.png",
            "food/buff_chef/dough.png",
            doughTexture.getTextureData().toString()
        );
    }

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

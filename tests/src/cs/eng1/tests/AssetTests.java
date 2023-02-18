package cs.eng1.tests;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import cs.eng1.piazzapanic.food.FoodTextureManager;

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
    public void cookedPattyAssetTest(){
        // Checks if "cooked_patty.png" exists
        assertTrue("Passes if cooked_patty.png exists", Gdx.files.internal("food/original/cooked_patty.png").exists());
    }
    
    @Test
    public void uncookedPattyAssetTest(){
        assertTrue("Passes if food/original/uncooked_patty.png exists", Gdx.files.internal("food/original/uncooked_patty.png").exists());
    }

    @Test
    public void lettuceAssetTest(){
        assertTrue("Passes if food/glitch/vegetable/lettuce.png exists", Gdx.files.internal("food/glitch/vegetable/lettuce.png").exists());
    }

    @Test
    public void lettuceChoppedAssetTest(){
        assertTrue("Passes if food/original/lettuce_chopped.png exists", Gdx.files.internal("food/original/lettuce_chopped.png").exists());
    }

    @Test
    public void tomatoAssetTest(){
        assertTrue("Passes if food/glitch/fruit/tomato.png exists", Gdx.files.internal("food/glitch/fruit/tomato.png").exists());
    }
        
    @Test
    public void tomatoChoppedAssetTest(){
        assertTrue("Passes if food/original/tomato_chopped.png exists", Gdx.files.internal("food/original/tomato_chopped.png").exists());
    }
        
    @Test
    public void bunAssetTest(){
        assertTrue("Passes if food/glitch/misc/bun.png exists", Gdx.files.internal("food/glitch/misc/bun.png").exists());
    }
        
    @Test
    public void burgerAssetTest(){
        assertTrue("Passes if food/glitch/misc/sandwich_burger_04.png exists", Gdx.files.internal("food/glitch/misc/sandwich_burger_04.png").exists());
    }
        
    @Test
    public void saladAssetTest(){
        assertTrue("Passes if food/glitch/misc/salad.png exists", Gdx.files.internal("food/glitch/misc/salad.png").exists());
    }

    @Test
    public void badlogicTest(){
        assertTrue("Passes if badlogic.jpg exists", Gdx.files.internal("badlogic.jpg").exists());
    }

    @Test
    public void testingAssetsAreCorrectlyMade(){
        // This checks the foodType fetches the correct texture in FoodTextureManager
        FoodTextureManager foodTextureManager = new FoodTextureManager();
        Texture pattyTexture = foodTextureManager.getTexture("patty_cooked");
        Texture truePattyTexture = new Texture(Gdx.files.internal("food/original/cooked_patty.png"));
    
        assertEquals("The foodType matches the texture returned", pattyTexture.getTextureData().toString(), truePattyTexture.getTextureData().toString());
    }
}



    
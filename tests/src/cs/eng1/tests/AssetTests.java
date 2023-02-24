package cs.eng1.tests;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.glutils.FileTextureData;

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


        // This checks the foodType fetches the correct texture in FoodTextureManager
    FoodTextureManager foodTextureManager = new FoodTextureManager();

    @Test
    public void falseTextureTest(){
        Texture falseTexture = foodTextureManager.getTexture("not_real");
        assertEquals("A false foodType should return badlogic.jpg",
         falseTexture.getTextureData().toString(), "badlogic.jpg");
    }
        
    @Test
    public void cookedPattyFetchTest(){
        Texture cookedPattyTexture = foodTextureManager.getTexture("patty_cooked");
        TextureData texturedata = cookedPattyTexture.getTextureData();
        if (texturedata instanceof FileTextureData) {
            assertEquals("The foodType should return the texture cooked_patty.png", 
            ((FileTextureData) texturedata).getFileHandle().path(),"food/original/cooked_patty.png");
        }
        else {
            fail("Failed to load texture.");
        }

    }
    
    @Test
    public void uncookedPattyFetchTest(){
        Texture uncookedpattyTexture = foodTextureManager.getTexture("patty_raw");
        assertEquals("The foodType should return the texture uncooked_patty.png", 
        uncookedpattyTexture.getTextureData().toString(),"food/original/uncooked_patty.png");
    }

    @Test
    public void lettuceFetchTest(){
        Texture lettuceTexture = foodTextureManager.getTexture("lettuce_raw");
        assertEquals("The foodType should return the texture lettuce.png", 
        lettuceTexture.getTextureData().toString(),"food/glitch/vegetable/lettuce.png");
    }

    @Test
    public void tomatoFetchTest(){
        Texture tomatoRawTexture = foodTextureManager.getTexture("tomato_raw");
        assertEquals("The foodType should return the texture tomato.png", 
        tomatoRawTexture.getTextureData().toString(),"food/glitch/fruit/tomato.png");
    }

    @Test
    public void tomatoChoppedPattyFetchTest(){
        Texture tomatoChoppedTexture = foodTextureManager.getTexture("tomato_chopped");
        assertEquals("The foodType should return the texture tomato_chopped.png ", 
        tomatoChoppedTexture.getTextureData().toString(),"food/original/tomato_chopped.png");
    }

    @Test
    public void bunFetchTest(){
        Texture bunTexture = foodTextureManager.getTexture("bun");
        assertEquals("The foodType should return the texure bun.png", 
        bunTexture.getTextureData().toString(),"food/glitch/misc/bun.png");
    }

    @Test
    public void sandwichBurgerFetchTest(){
        Texture burgerTexture = foodTextureManager.getTexture("burger");
        assertEquals("The foodType should return the texture sandwich_burger_04.png ", 
        burgerTexture.getTextureData().toString(),"food/glitch/misc/sandwich_burger_04.png");
    }

    @Test
    public void saladFetchTest(){
        Texture saladTexture = foodTextureManager.getTexture("salad");
        assertEquals("The foodType should return the texture salad.png", 
        saladTexture.getTextureData().toString(),"food/glitch/misc/salad.png");
    }
    

    
}


    
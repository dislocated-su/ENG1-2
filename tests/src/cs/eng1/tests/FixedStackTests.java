package cs.eng1.tests;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.glutils.FileTextureData;

import static org.junit.Assert.*;

import cs.eng1.piazzapanic.chef.FixedStack;
import cs.eng1.piazzapanic.food.FoodTextureManager;

@RunWith(GdxTestRunner.class)
public class FixedStackTests {
    FixedStack<Integer> fixedStack = new FixedStack<Integer>(5);
    @Test
    public void fixedStackLengthTest() {
        assertEquals("The stack has a max size equal to its size.",fixedStack.maxSize, 5);
    }

    @Test
    public void hasSpaceTest(){
        FixedStack<Integer> fixedStack = new FixedStack<Integer>(1);
        assertTrue("hasSpace is true when the stack isn't full",fixedStack.hasSpace());
    }

    @Test
    public void hasNoSpaceTest(){
        FixedStack<Integer> fixedStack = new FixedStack<Integer>(0);
        assertFalse("hasSpace is false when at max size",fixedStack.hasSpace());
    }

    @Test
    public void pushWithSpaceTest(){
        FixedStack<Integer> fixedStack = new FixedStack<Integer>(5);
        for(int i = 1; i<5; i++){
            fixedStack.push(1);
            assertEquals("Push should work when the size is less than maxSize", i, fixedStack.size());
        }
    }

    @Test
    public void pushWithoutSpaceTest(){
        FixedStack<Integer> fixedStack = new FixedStack<Integer>(0);
        fixedStack.push(1);
        assertEquals("Push should not work when the size is equal to maxSize",0, fixedStack.size());
    }

    @Test
    public void cookedPattyFetchTest(){
        FoodTextureManager foodTextureManager = new FoodTextureManager();
        Texture cookedPattyTexture = foodTextureManager.getTexture("patty_cooked");
        TextureData texturedata = cookedPattyTexture.getTextureData();
        if (texturedata instanceof FileTextureData) {
            assertEquals("The foodType should return the texture cooked_patty.png", 
            "food/original/cooked_patty.png", ((FileTextureData) texturedata).getFileHandle().path());
        }
        else { 
            fail("Failed to load texture.");
        }

    }

    //Couldn't test fringe cases of stacks being larger than maxSize
}
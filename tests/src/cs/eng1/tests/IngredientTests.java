package cs.eng1.tests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.food.ingredients.Patty;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class IngredientTests {

    FoodTextureManager textureManager = new FoodTextureManager();
    Ingredient patty = Ingredient.fromString("patty", textureManager);
    Ingredient lettuce = Ingredient.fromString("lettuce", textureManager);
    Ingredient potato = Ingredient.fromString("potato", textureManager);
    Ingredient cheese = Ingredient.fromString("cheese", textureManager);

    /*
     * These test the behaviour of fromString
     */

    @Test
    public void testFromString() {
        Ingredient ingredient = Ingredient.fromString("patty", textureManager);
        assertNotNull("fromString should find an ingredient.", ingredient);
        assertEquals(
            "The class of the ingredient found should be the correct class.",
            Patty.class,
            ingredient.getClass()
        );
    }

    /*
     * These test isCooked properly accesses cooked
     */
    @Test
    public void testIsCooked() {
        Ingredient ingredient = Ingredient.fromString("patty", textureManager);
        assertFalse(
            "The ingredient shouldn't be immediately cooked.",
            ingredient.getCooked()
        );

        ingredient.setIsCooked(true);
        assertTrue(
            "getCooked should be true when cooked is true.",
            ingredient.getCooked()
        );

        ingredient.setIsCooked(false);
        assertFalse(
            "getCooked should be false when cooked is false.",
            ingredient.getCooked()
        );
    }

    /*
     * These test isChopped properly accesses chopped
     */
    @Test
    public void testIsChopped() {
        assertFalse(
            "An ingredient should not be chopped immediately.",
            lettuce.getChopped()
        );

        lettuce.setChopped(true);
        assertTrue(
            "getChopped should be true when chopped is true.",
            lettuce.getChopped()
        );

        lettuce.setChopped(false);
        assertFalse(
            "getChopped should be false when chopped is false.",
            lettuce.getChopped()
        );
    }

    /*
     * These test isGrilled properly accesses grilled.
     */
    @Test
    public void testIsGrilled() {
        assertFalse(
            "An ingredient should not be grilled immediately.",
            patty.getGrilled()
        );

        patty.setIsGrilled(true);
        assertTrue(
            "getGrilled should be true when grilled is true.",
            patty.getGrilled()
        );

        patty.setIsGrilled(false);
        assertFalse(
            "getGrilled should be false when grilled is false.",
            patty.getGrilled()
        );
    }

    /*
     * These test toString gets the right name for each state of an ingredient
     */
    @Test
    public void testToString() {
        assertEquals(
            "Ingredient should be raw without interaction.",
            "patty_raw",
            patty.toString()
        );
        patty.setIsGrilled(true);
        assertEquals(
            "Ingredient should be changed to _grilled when fully grilled.",
            "patty_grilled",
            patty.toString()
        );
        patty.setUseable(false);
        assertEquals(
            "Ingredient should be _ruined when not useable.",
            "patty_ruined",
            patty.toString()
        );

        assertEquals(
            "Ingredient should be raw without interaction.",
            "lettuce_raw",
            lettuce.toString()
        );
        lettuce.setChopped(true);
        assertEquals(
            "Ingredient should be changed to _choppedwhen fully chopped.",
            "lettuce_chopped",
            lettuce.toString()
        );
        lettuce.setUseable(false);
        assertEquals(
            "Ingredient should be _ruined when not useable.",
            "lettuce_ruined",
            lettuce.toString()
        );

        assertEquals(
            "Ingredient should be raw without interaction.",
            "potato_raw",
            potato.toString()
        );
        potato.setIsCooked(true);
        assertEquals(
            "Ingredient should be changed to _cooked when fully cooked.",
            "potato_cooked",
            potato.toString()
        );
        potato.setUseable(false);
        assertEquals(
            "Ingredient should be _ruined when not useable.",
            "potato_ruined",
            potato.toString()
        );
        cheese.setChopped(true);
        assertEquals(
            "Ingredient should be _chopped when chopped.",
            "cheese_chopped",
            cheese.toString()
        );
    }

    /*
     * These test getUseable accesses useable
     */
    @Test
    public void testUseable() {
        assertTrue(
            "Ingredient should be useable by default",
            patty.getUseable()
        );

        patty.setUseable(false);
        assertFalse(
            "getUseable should be false when useable is false.",
            patty.getUseable()
        );

        patty.setUseable(true);
        assertTrue(
            "getUseable should be true when useable is true.",
            patty.getUseable()
        );
    }

    @Test
    public void arrayFromStringTests() {
        Ingredient[] ingredients = Ingredient.arrayFromString(
            "patty,tomato,lettuce,bun,cheese,potato,dough,uncooked_pizza",
            textureManager
        );
        assertEquals(
            "arrayFromString should properly split the given string properly.",
            8,
            ingredients.length
        );
        assertEquals(
            "arrayFromString should get textures from the split string.",
            lettuce.getTexture(),
            ingredients[2].getTexture()
        );
    }
}

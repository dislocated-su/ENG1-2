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
        Ingredient ingredient = Ingredient.fromString(
            "lettuce",
            textureManager
        );
        assertFalse(
            "An ingredient should not be chopped immediately.",
            ingredient.getChopped()
        );

        ingredient.setChopped(true);
        assertTrue(
            "getChopped should be true when chopped is true.",
            ingredient.getChopped()
        );

        ingredient.setChopped(false);
        assertFalse(
            "getChopped should be false when chopped is false.",
            ingredient.getChopped()
        );
    }

    /*
     * These test isGrilled properly accesses grilled.
     */
    @Test
    public void testIsGrilled() {
        Ingredient ingredient = Ingredient.fromString("patty", textureManager);
        assertFalse(
            "An ingredient should not be grilled immediately.",
            ingredient.getGrilled()
        );

        ingredient.setIsGrilled(true);
        assertTrue(
            "getGrilled should be true when grilled is true.",
            ingredient.getGrilled()
        );

        ingredient.setIsGrilled(false);
        assertFalse(
            "getGrilled should be false when grilled is false.",
            ingredient.getGrilled()
        );
    }

    /*
     * These test toString gets the right name for each state of an ingredient
     */
    @Test
    public void testToString() {
        Ingredient patty = Ingredient.fromString("patty", textureManager);
        Ingredient lettuce = Ingredient.fromString("lettuce", textureManager);
        Ingredient potato = Ingredient.fromString("potato", textureManager);

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
    }

    /*
     * These test getUseable accesses useable
     */
    @Test
    public void testUseable() {
        Ingredient ingredient = Ingredient.fromString("patty", textureManager);

        assertTrue(
            "Ingredient should be useable by default",
            ingredient.getUseable()
        );

        ingredient.setUseable(false);
        assertFalse(
            "getUseable should be false when useable is false.",
            ingredient.getUseable()
        );

        ingredient.setUseable(true);
        assertTrue(
            "getUseable should be true when useable is true.",
            ingredient.getUseable()
        );
    }
}

package cs.eng1.piazzapanic.food;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import java.util.HashMap;

/**
 * Helper class that allows textures to be mapped and stored and disposed of in a single location.
 *
 * @author Alistair Foggin
 */
public class FoodTextureManager implements Disposable {

    private final HashMap<String, Texture> foodTextures;
    private final Texture notFoundImage;

    public FoodTextureManager() {
        this.foodTextures = new HashMap<>();
        this.foodTextures.put(
                "patty_grilled",
                new Texture(
                    Gdx.files.internal("food/buff_chef/grilled_patty.png")
                )
            );
        this.foodTextures.put(
                "patty_raw",
                new Texture(
                    Gdx.files.internal("food/buff_chef/formed_patty.png")
                )
            );
        this.foodTextures.put(
                "lettuce_raw",
                new Texture(Gdx.files.internal("food/buff_chef/lettuce.png"))
            );
        this.foodTextures.put(
                "lettuce_chopped",
                new Texture(
                    Gdx.files.internal("food/buff_chef/sliced_lettuce.png")
                )
            );
        this.foodTextures.put(
                "tomato_raw",
                new Texture(Gdx.files.internal("food/buff_chef/tomato.png"))
            );
        this.foodTextures.put(
                "tomato_chopped",
                new Texture(
                    Gdx.files.internal("food/buff_chef/sliced_tomato.png")
                )
            );
        this.foodTextures.put(
                "bun",
                new Texture(Gdx.files.internal("food/buff_chef/buns.png"))
            );
        this.foodTextures.put(
                "cheese",
                new Texture(Gdx.files.internal("food/buff_chef/cheese.png"))
            );
        this.foodTextures.put(
                "dough",
                new Texture(Gdx.files.internal("food/buff_chef/dough.png"))
            );
        this.foodTextures.put(
                "potato",
                new Texture(Gdx.files.internal("food/buff_chef/potato.png"))
            );
        this.foodTextures.put(
                "uncooked_pizza",
                new Texture(
                    Gdx.files.internal("food/buff_chef/formed_pizza.png")
                )
            );
        this.foodTextures.put(
                "burger",
                new Texture(Gdx.files.internal("food/buff_chef/burger.png"))
            );
        this.foodTextures.put(
                "salad",
                new Texture(Gdx.files.internal("food/buff_chef/salad.png"))
            );
        this.foodTextures.put(
                "pizza",
                new Texture(
                    Gdx.files.internal("food/buff_chef/cooked_pizza.png")
                )
            );
        this.foodTextures.put(
                "jacket_potato",
                new Texture(
                    Gdx.files.internal("food/buff_chef/jacket_potato.png")
                )
            );
        this.foodTextures.put(
                "cheese_sliced",
                new Texture("food/buff_chef/grated_cheese.png")
            );
        this.foodTextures.put(
                "potato_cooked",
                new Texture("food/buff_chef/cooked_potato.png")
            );
        this.foodTextures.put("burnt", new Texture("food/original/burnt.png"));
        this.foodTextures.put(
                "rotten",
                new Texture("food/original/rotten.png")
            );

        notFoundImage = new Texture(Gdx.files.internal("badlogic.jpg"));
    }

    /**
     * @param foodType The food string to get the corresponding texture from the
     *                 hashmap.
     * @return the texture for the specified food or an image to signify that the
     *         texture does not
     *         exist.
     */
    public Texture getTexture(String foodType) {
        Texture texture = foodTextures.get(foodType);
        if (texture != null) {
            return texture;
        } else {
            return notFoundImage;
        }
    }

    @Override
    public void dispose() {
        for (Texture texture : foodTextures.values()) {
            texture.dispose();
        }
        notFoundImage.dispose();
    }
}

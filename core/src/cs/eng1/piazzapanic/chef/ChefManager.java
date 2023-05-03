package cs.eng1.piazzapanic.chef;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.ui.UIOverlay;
import cs.eng1.piazzapanic.utility.KeyboardInput;
import cs.eng1.piazzapanic.utility.saving.SavedChef;
import cs.eng1.piazzapanic.utility.saving.SavedChefManager;
import cs.eng1.piazzapanic.utility.saving.SavedFood;
import java.util.ArrayList;
import java.util.List;

/**
 * The controller that handles switching control between chefs and tells them
 * about the surrounding environment.
 *
 * @author Alistair Foggin
 * @author Ross Holmes
 */
public class ChefManager implements Disposable {

    public World world;
    protected KeyboardInput keyboardInput;

    private final ArrayList<Chef> chefs;
    private Chef currentChef = null;
    private final UIOverlay overlay;
    final String[] chefSprites = new String[] {
        "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png",
        "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Woman Green/womanGreen_hold.png",
        "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Blue/manBlue_hold.png",
        "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Red/manRed_hold.png",
        "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Woman Old/womanOld_hold.png",
    };
    private final float chefScale;

    /**
     * @param chefScale     the amount to scale the texture by so that each chef is
     *                      an
     *                      accurate size.
     * @param overlay       the user interface overlay to display information about
     *                      the
     *                      current chef and time, and to provide more controls.
     * @param world         Box2D world to use for Chef collision and movement.
     *
     * @param keyboardInput Input processor for input from the keyboard. Passed down
     *                      to the chefs this class owns.
     */
    public ChefManager(
        float chefScale,
        UIOverlay overlay,
        World world,
        KeyboardInput keyboardInput
    ) {
        this.overlay = overlay;
        this.world = world;
        this.keyboardInput = keyboardInput;
        this.chefScale = chefScale;

        chefs = new ArrayList<>(chefSprites.length);

        // Initialize chefs
        for (int i = 0; i < 3; i++) {
            String sprite = chefSprites[i];
            Texture chefTexture = new Texture(Gdx.files.internal(sprite));
            Chef chef = new Chef(
                chefTexture,
                new Vector2(
                    chefTexture.getWidth() * chefScale,
                    chefTexture.getHeight() * chefScale
                ),
                this
            );
            chef.setInputEnabled(false);
            chefs.add(chef);
        }
    }

    public ChefManager(
        SavedChefManager save,
        float chefScale,
        UIOverlay overlay,
        World world,
        KeyboardInput keyboardInput,
        FoodTextureManager textureManager
    ) {
        this.overlay = overlay;
        this.world = world;
        this.keyboardInput = keyboardInput;
        this.chefScale = chefScale;

        chefs = new ArrayList<>();

        for (SavedChef savedChef : save.savedChefs) {
            Texture chefTexture = new Texture(Gdx.files.internal(savedChef.imagePath));
            Chef chef = new Chef(
                chefTexture,
                new Vector2(
                    chefTexture.getWidth() * chefScale,
                    chefTexture.getHeight() * chefScale
                ),
                this
            );
            chef.init(savedChef.position.x, savedChef.position.y);
            for (SavedFood item : savedChef.inventory) {
                chef.grabItem(item.get(textureManager));
            }
            chef.setInputEnabled(savedChef.inputEnabled);
            chefs.add(chef);

            if (savedChef.currentChef) {
                currentChef = chef;
            }
        }
    }

    /**
     * Reset each chef to their original position when you load
     */
    public void init(List<Vector2> spawnPoints) {
        int index = 0;
        for (Chef chef : chefs) {
            Vector2 pos = spawnPoints.get(index);
            chef.init(pos.x, pos.y);
            index++;
            if (index == 3) {
                index = 0;
            }
        }
    }

    /**
     *
     */
    public void hireChef(Vector2 position, Stage stage) {
        if (chefs.size() >= 5) {
            return;
        }

        Texture chefTexture = new Texture(Gdx.files.internal(chefSprites[chefs.size()]));
        Chef chef = new Chef(
            chefTexture,
            new Vector2(chefTexture.getWidth() * chefScale, chefTexture.getHeight() * chefScale),
            this
        );

        chef.init(position.x, position.y);
        chef.setInputEnabled(false);

        chefs.add(chef);
        stage.addActor(chef);
        final ChefManager manager = this;
        stage.addListener(
            new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Actor actorHit = stage.hit(x, y, false);
                    if (actorHit instanceof Chef) {
                        manager.setCurrentChef((Chef) actorHit);
                    }
                }
            }
        );
    }

    public List<Chef> getChefs() {
        return chefs;
    }

    public void act(float delta) {
        if (keyboardInput.changeCooks) {
            keyboardInput.changeCooks = false;
            int chefIndex = chefs.indexOf(currentChef) + 1;
            if (chefIndex >= chefs.size()) {
                chefIndex = 0;
            }
            setCurrentChef(chefs.get(chefIndex));
        }
    }

    /**
     * Add the created Chefs to the game world
     *
     * @param stage The game world to which the chefs should be added.
     */
    public void addChefsToStage(final Stage stage) {
        for (Chef chef : chefs) {
            stage.addActor(chef);
        }
        final ChefManager manager = this;
        stage.addListener(
            new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Actor actorHit = stage.hit(x, y, false);
                    if (actorHit instanceof Chef) {
                        manager.setCurrentChef((Chef) actorHit);
                    }
                }
            }
        );
    }

    /**
     * Given a chef, update the state of the chefs to make sure that only one has
     * input enabled.
     *
     * @param chef the chef to be controlled by the user
     */
    public void setCurrentChef(Chef chef) {
        if (currentChef != chef) {
            if (currentChef != null) {
                currentChef.setInputEnabled(false);
            }
            currentChef = chef;
            currentChef.setInputEnabled(true);
        }
        currentChefStackUpdated();
    }

    public Chef getCurrentChef() {
        return currentChef;
    }

    /**
     * Update the UI when the current chef's stack has been updated
     */
    public void currentChefStackUpdated() {
        overlay.updateChefUI(currentChef);
    }

    @Override
    public void dispose() {
        for (Chef chef : chefs) {
            chef.dispose();
        }
    }
}

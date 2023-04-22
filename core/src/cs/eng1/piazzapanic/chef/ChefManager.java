package cs.eng1.piazzapanic.chef;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import cs.eng1.piazzapanic.ui.UIOverlay;
import cs.eng1.piazzapanic.utility.KeyboardInput;
import java.util.ArrayList;
import java.util.List;

/**
 * The controller that handles switching control between chefs and tells them
 * about the surrounding
 * environment.
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
    };
    private final float[] chefX = new float[] { 5f, 10f };
    private final float[] chefY = new float[] { 3f, 3f };

    /**
     * @param chefScale the amount to scale the texture by so that each chef is an
     *                  accurate
     *                  size.
     * @param overlay   the user interface overlay to display information about the
     *                  current chef
     *                  and time, and to provide more controls.
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

        // Load chef sprites
        chefs = new ArrayList<>(chefSprites.length);

        // Initialize chefs
        for (int i = 0; i < chefSprites.length; i++) {
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
            chef.setBounds(
                chefX[i],
                chefY[i],
                chefTexture.getWidth() * chefScale,
                chefTexture.getHeight() * chefScale
            );
            chef.setInputEnabled(false);
            chefs.add(chef);
        }
    }

    public float[] getChefX() {
        return chefX;
    }

    public float[] getChefY() {
        return chefY;
    }


    /**
     * Reset each chef to their original position when you load
     */
    public void init() {
        for (int i = 0; i < chefs.size(); i++) {
            chefs.get(i).init(chefX[i], chefY[i]);
        }
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

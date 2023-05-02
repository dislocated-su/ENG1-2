package cs.eng1.piazzapanic.stations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.observable.Observer;
import cs.eng1.piazzapanic.observable.Subject;
import cs.eng1.piazzapanic.ui.StationActionUI;
import cs.eng1.piazzapanic.ui.StationUIController;

import java.util.LinkedList;
import java.util.List;

/**
 * Base class for all Stations. Implements {@link Observer}
 *
 * @author Alistair Foggin
 * @author Matt Fitzpatrick
 */
public class Station extends Actor implements Observer<Chef> {

    protected final int id;
    protected final StationUIController uiController;
    protected final StationActionUI.ActionAlignment actionAlignment;
    protected final TextureRegion stationImage;

    protected boolean inUse = false;

    protected final List<Subject<Chef>> chefSubjects = new LinkedList<>();

    public Chef nearbyChef = null;
    private float imageRotation = 0.0f;

    protected boolean locked;

    /**
     * The constructor method for the class
     *
     * @param id           The unique identifier of the station
     * @param image        The rectangular area of the texture
     * @param uiController The controller from which we can get show and hide the
     *                     action
     *                     buttons belonging to the station
     * @param alignment    Dictates where the action buttons are shown
     * @param locked       Whether the station is locked and has to be purchased before it can be used.
     */
    public Station(
        int id,
        TextureRegion image,
        StationUIController uiController,
        StationActionUI.ActionAlignment alignment,
        Boolean locked
    ) {
        this.locked = locked != null && locked;
        this.id = id;
        stationImage = image; // Texture of the object
        actionAlignment = alignment;
        this.uiController = uiController;
    }

    /**
     * Reset the station values to be the default.
     */
    public void reset() {
        uiController.hideActions(this);
        uiController.hideProgressBar(this);
    }

    public void setImageRotation(float rotation) {
        this.imageRotation = rotation;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(
            stationImage,
            getX(),
            getY(),
            0.5f,
            0.5f,
            getWidth(),
            getHeight(),
            1f,
            1f,
            imageRotation
        );
    }

    /**
     * Take a food texture and render it on top of the station at a smaller size
     * than the station.
     *
     * @param batch       the sprite batch to draw rectangles to the screen.
     * @param foodTexture the texture to be drawn onto the screen.
     */
    protected void drawFoodTexture(Batch batch, Texture foodTexture) {
        drawFoodTexture(batch, foodTexture, 1);
    }

    /**
     * Take a food texture and render it on top of the station with a custom
     * scaling.
     *
     * @param batch       the sprite batch to draw rectangles to the screen.
     * @param foodTexture the texture to be drawn onto the screen.
     * @param scale       how big the texture is to be drawn (relative to default
     *                    size of .6f)
     */
    protected void drawFoodTexture(
        Batch batch,
        Texture foodTexture,
        float scale
    ) {
        batch.draw(
            foodTexture,
            getX() + (float) (.2f / Math.pow(scale, 2)),
            getY() + (float) (.2f / Math.pow(scale, 2)),
            .6f * scale,
            .6f * scale
        );
    }

    /**
     * Draw the outline of the shape of the station as a rectangle and draw a blue
     * line from the
     * centre of this station (which is an Observer) to the centre of the
     * stationCollider that it is
     * linked to (the Subject that this is registered to).
     *
     * @param shapes The renderer to use to draw debugging information
     */
    @Override
    public void drawDebug(ShapeRenderer shapes) {
        Color oldColor = shapes.getColor();

        // Draw bounds of this station
        shapes.setColor(Color.RED);
        shapes.rect(getX(), getY(), getWidth(), getHeight());

        // Check for any station colliders
        if (chefSubjects.isEmpty()) {
            shapes.setColor(oldColor);
            return;
        }

        // Draw lines to linked station colliders
        shapes.setColor(Color.BLUE);
        for (Subject<Chef> chefSubject : chefSubjects) {
            if (chefSubject instanceof Actor) {
                Actor collider = (Actor) chefSubject;
                Vector2 start = new Vector2(
                    getX() + getWidth() / 2f,
                    getY() + getHeight() / 2f
                );
                Vector2 end = new Vector2(
                    collider.getX() + collider.getWidth() / 2f,
                    collider.getY() + collider.getHeight() / 2f
                );
                shapes.line(start, end);
            }
        }

        // Reset colour
        shapes.setColor(oldColor);
    }

    /**
     * Take the chef sent from the Subject and decide what interactions are
     * possible.
     *
     * @param chef The chef that the station should interact with which is given
     *             from the Subject to
     *             this Observer.
     */
    @Override
    public void update(Chef chef) {
        if (chef != null) {
            this.nearbyChef = chef;
            uiController.showActions(this, getActionTypes());
        } else if (this.nearbyChef != null) {
            Chef remainingChef = null;
            for (Subject<Chef> chefSubject : chefSubjects) {
                remainingChef = chefSubject.getLastNotification();
                if (remainingChef != null) {
                    break;
                }
            }
            if (remainingChef == null) {
                this.nearbyChef = null;
                uiController.hideActions(this);
            } else if (remainingChef != nearbyChef) {
                this.nearbyChef = remainingChef;
                uiController.showActions(this, getActionTypes());
            } else {
                uiController.showActions(this, getActionTypes());
            }
        }
    }

    @Override
    public void addSubject(Subject<Chef> chefSubject) {
        this.chefSubjects.add(chefSubject);
    }

    @Override
    public void removeSubject(Subject<Chef> chefSubject) {
        this.chefSubjects.remove(chefSubject);
    }

    @Override
    public void deregisterFromAllSubjects() {
        for (Subject<Chef> chefSubject : this.chefSubjects) {
            chefSubject.deregister(this);
        }
        this.chefSubjects.clear();
    }

    @Override
    public List<Subject<Chef>> getSubjects() {
        return this.chefSubjects;
    }

    /**
     * @return the list of possible actions that this station based on the current
     *         state
     */
    public LinkedList<StationAction.ActionType> getActionTypes() {
        PlayerState state = PlayerState.getInstance();
        if (locked && state.getCash() > state.getUpgradeCost(false)) {
            LinkedList<StationAction.ActionType> actionTypes =
                new LinkedList<>();
            actionTypes.add(StationAction.ActionType.BUY_STATION);
            return actionTypes;
        } else {
            return new LinkedList<>();
        }
    }

    /**
     * Given an action, the station should attempt to do that action based on the
     * chef that is nearby
     * or what ingredient(s) are currently on the station.
     *
     * @param action the action that needs to be done by this station if it can.
     */
    public void doStationAction(StationAction.ActionType action) {
        if (action == StationAction.ActionType.BUY_STATION) {
            locked = false;
            PlayerState state = PlayerState.getInstance();
            state.spendCash(state.getUpgradeCost(true));
            uiController.showActions(this, getActionTypes());
        }
    }

    /**
     * @return the direction in which the action buttons should be displayed.
     */
    public StationActionUI.ActionAlignment getActionAlignment() {
        return actionAlignment;
    }

    public int getId() {
        return id;
    }

    public boolean getInUse() {
        return inUse;
    }

    public boolean getLocked() {
        return locked;
    }
}

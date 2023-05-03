package cs.eng1.piazzapanic.customer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.PlayerState.PowerUp;
import cs.eng1.piazzapanic.box2d.Box2dLocation;
import cs.eng1.piazzapanic.box2d.Box2dSteeringBody;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.piazzapanic.utility.Timer;
import cs.eng1.piazzapanic.utility.saving.SavedCustomer;

/**
 * In-game customer, displayed as a Scene2D actor.
 * World collisions are handled by box2d and station interaction collisions are handled using StationCollider and Scene2D.
 * @author Ross Holmes
 * @author Andrey Samoilov
 */
public class Customer extends Actor implements Disposable {

    /**
     * Wrapper for a steering agent responsible for moving the body.
     * @see Box2dSteeringBody
     */
    public Box2dSteeringBody steeringBody;
    /**
     * Current objective of the steering agent. Represented as an ID for a map in CustomerManager.
     */
    public Integer currentObjective = null;
    private final Timer repTimer;
    private final Recipe order;
    private final CustomerManager customerManager;
    private final Texture texture;
    private final Vector2 textureBounds;
    private final Body body;
    private boolean reputation = true;
    private boolean orderCompleted = false;
    private Box2dLocation endObjective;

    public Customer(
        Texture texture,
        Vector2 bounds,
        Vector2 position,
        Recipe order,
        CustomerManager customerManager
    ) {
        repTimer = new Timer(60000, true, false);
        this.order = order;
        this.customerManager = customerManager;
        this.texture = texture;
        this.textureBounds = bounds;
        setPosition(position.x, position.y);

        CircleShape circle = new CircleShape();
        circle.setRadius(0.4f);
        FixtureDef fDef = new FixtureDef();
        fDef.shape = circle;
        fDef.density = 20f;
        fDef.friction = 0.4f;

        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(), getY() + 0.2f);
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.linearDamping = 20f;
        bDef.fixedRotation = true;

        body = customerManager.world.createBody(bDef);
        body.createFixture(fDef);

        this.steeringBody = new Box2dSteeringBody(this.body, true, 0.4f);
    }

    /**
     * @return this customer's order.
     */
    public Recipe getOrder() {
        return order;
    }

    /**
     * @return {@link Timer} that when elapsed loses reputation of the player.
     */
    public Timer getRepTimer() {
        return repTimer;
    }

    public boolean getReputation() {
        return reputation;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean getOrderCompleted() {
        return orderCompleted;
    }

    /**
     * Procedure for fulfilling the customers order:
     *  Completes the order of the current customer.
     *  Updates the player's cash.
     *  Makes the customer walk away.
     */
    public void fulfillOrder() {
        boolean happiness = (repTimer.getDelay() / repTimer.getElapsed()) > 0.5;
        PlayerState.getInstance().earnCash(100, happiness);
        orderCompleted = true;
        Gdx.app.log("Current cash", Float.toString(PlayerState.getInstance().getCash()));
        customerManager.walkBack(this);
        endObjective = customerManager.getObjective(currentObjective);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(
            texture,
            getX() + (1 - textureBounds.x) / 2f,
            getY() + (1 - textureBounds.y) / 2f,
            textureBounds.x / 2f,
            textureBounds.y / 2f,
            textureBounds.x,
            textureBounds.y,
            1f,
            1f,
            getRotation(),
            0,
            0,
            texture.getWidth(),
            texture.getHeight(),
            false,
            false
        );
        if (orderCompleted) {
            Texture texture = order.getTexture();
            batch.draw(
                texture,
                getX() + 0.5f,
                getY() + 0.2f,
                0f,
                0.3f,
                0.6f,
                0.6f,
                1.5f,
                1.5f,
                getRotation(),
                0,
                0,
                texture.getWidth(),
                texture.getHeight(),
                false,
                false
            );
        }
    }

    @Override
    public void act(float delta) {
        Vector2 position = body.getPosition();

        steeringBody.update(delta);

        setPosition(position.x - 0.5f, position.y - 0.5f);

        setRotation((float) Math.toDegrees(body.getAngle()));

        if (orderCompleted) {
            if (endObjective.getPosition().epsilonEquals(position, 0.5f)) {
                this.remove();
                this.dispose();
            }
        }

        if (
            !orderCompleted &&
            reputation &&
            repTimer.tick(delta) &&
            !PlayerState.getInstance().getBuffActive(PowerUp.NO_REP_LOSS)
        ) {
            customerManager.loseReputation();
            reputation = false;
            Gdx.app.log("rep loss", "");
        }
    }

    public boolean isOrderCompleted() {
        return orderCompleted;
    }

    public SavedCustomer getSavedCustomer() {
        SavedCustomer save = new SavedCustomer();
        save.currentObjective = currentObjective;
        save.imagePath = texture.getTextureData().toString();
        save.order = order.getType();
        save.orderCompleted = orderCompleted;
        save.position = body.getPosition();
        save.repTimer = repTimer;
        save.reputation = reputation;
        return save;
    }

    @Override
    public void dispose() {
        texture.dispose();
        customerManager.world.destroyBody(this.body);
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }
}

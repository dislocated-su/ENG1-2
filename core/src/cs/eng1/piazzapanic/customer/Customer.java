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
import cs.eng1.piazzapanic.food.interfaces.Holdable;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.piazzapanic.utility.Timer;

public class Customer extends Actor implements Disposable {

    private final Timer repTimer;
    private final Recipe order;
    private final CustomerManager customerManager;
    private boolean reputation = true;
    private boolean orderCompleted = false;
    private final Texture texture;
    private final Vector2 textureBounds;

    private Body body;

    private float rotation;

    public Customer(Texture texture, Vector2 bounds, Recipe order, CustomerManager customerManager) {
        repTimer = new Timer(60000, true, false);
        this.order = order;
        this.customerManager = customerManager;
        this.texture = texture;
        this.textureBounds = bounds;
        setPosition(17f,17f);
        createBody();
    }

    public void createBody() {
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
    }

    public Recipe getOrder() {
        return order;
    }

    public void fulfillOrder() {
        PlayerState.getInstance().earnCash(100, reputation);
        orderCompleted = true;
        PlayerState.getInstance();
        Gdx.app.log(
            "Current cash",
            Float.toString(PlayerState.getInstance().getCash())
        );
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
                rotation,
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
                rotation,
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

        setPosition(position.x - 0.5f, position.y - 0.5f);
        
        if (!orderCompleted && reputation && repTimer.tick(delta)) {
            customerManager.loseReputation();
            reputation = false;
            Gdx.app.log("rep loss", "");
        }

        super.act(delta);
    }

    public boolean isOrderCompleted() {
        return orderCompleted;
    }

    @Override
    public void dispose() {}
}

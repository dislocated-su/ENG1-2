package cs.eng1.piazzapanic.chef;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import cs.eng1.piazzapanic.ingredients.Ingredient;
import cs.eng1.piazzapanic.stations.Station;

public class Chef extends Actor {

  private final Sprite image;
  private final ChefManager chefManager;
  private final FixedStack<Ingredient> ingredientStack = new FixedStack<>(5);

  private final Vector2 inputVector;
  private final float speed = 3f;
  private final float collisionSkin = 0.01f;
  private boolean inputEnabled = true;

  //interactions between chef and stations are implemented
  public Chef(Sprite image, ChefManager chefManager) {
    this.image = image;
    this.chefManager = chefManager;
    inputVector = new Vector2();
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.draw(image, getX(), getY(), getWidth(), getHeight());
  }

  @Override
  public void act(float delta) {
    getInput();

    Vector2 movement = calculateMovement(delta);
    addAction(Actions.moveBy(movement.x, movement.y));

    super.act(delta);
  }

  private void getInput() {
    inputVector.x = 0;
    inputVector.y = 0;
    if (!isInputEnabled()) {
      return;
    }
    float x = 0f;
    float y = 0f;
    if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
      y += 1f;
    }
    if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      y -= 1f;
    }
    if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      x += 1f;
    }
    if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      x -= 1f;
    }
    setInputVector(x, y);
  }

  private Vector2 calculateMovement(float delta) {
    Vector2 movement = new Vector2(inputVector.x * speed * delta, inputVector.y * speed * delta);

    // Adjust movement for collision
    movement.x = adjustHorizontalMovementForCollision(movement.x);
    movement.y = adjustVerticalMovementForCollision(movement.y);

    return movement;
  }

  private Rectangle getCollisionObjectBoundaries(float x, float y) {
    Actor actorHit = getStage().hit(x, y, false);
    Cell tileHit = chefManager.getCellAtPosition(x, getY());

    if (tileHit != null) {
      return new Rectangle((float) Math.floor(x), (float) Math.floor(y), 1, 1);
    } else if (actorHit instanceof Station || actorHit instanceof Chef) {
      return new Rectangle(actorHit.getX(), actorHit.getY(),
          actorHit.getWidth(), actorHit.getHeight());
    } else {
      return null;
    }
  }

  private float adjustHorizontalMovementForCollision(float xMovement) {
    if (xMovement > 0.0001f) {
      float rightBorder = getX() + getWidth() + xMovement;
      Rectangle hitBoundsBottom = getCollisionObjectBoundaries(rightBorder, getY());
      Rectangle hitBoundsTop = getCollisionObjectBoundaries(rightBorder, getY() + getHeight());
      float adjustment = -getWidth() - collisionSkin - getX();

      if (hitBoundsBottom != null) {
        xMovement = hitBoundsBottom.x + adjustment;
      }
      if (hitBoundsTop != null) {
        // Account for the case that the bottom collision is closer
        xMovement = Math.min(xMovement, hitBoundsTop.x + adjustment);
      }
    } else if (xMovement < -0.0001f) {
      float leftBorder = getX() + xMovement;
      Rectangle hitBoundsBottom = getCollisionObjectBoundaries(leftBorder, getY());
      Rectangle hitBoundsTop = getCollisionObjectBoundaries(leftBorder, getY() + getHeight());
      float adjustment = collisionSkin - getX();

      if (hitBoundsBottom != null) {
        xMovement = hitBoundsBottom.x + hitBoundsBottom.width + adjustment;
      }
      if (hitBoundsTop != null) {
        xMovement = Math.max(xMovement, hitBoundsTop.x + hitBoundsTop.width + adjustment);
      }
    }
    return xMovement;
  }

  private float adjustVerticalMovementForCollision(float yMovement) {
    if (yMovement > 0.0001f) {
      float topBorder = getY() + getHeight() + yMovement;
      Rectangle hitBoundsLeft = getCollisionObjectBoundaries(getX(), topBorder);
      Rectangle hitBoundsRight = getCollisionObjectBoundaries(getX() + getWidth(), topBorder);
      float adjustment = -getHeight() - collisionSkin - getY();

      if (hitBoundsLeft != null) {
        yMovement = hitBoundsLeft.y + adjustment;
      }
      if (hitBoundsRight != null) {
        yMovement = Math.min(yMovement, hitBoundsRight.y + adjustment);
      }
    } else if (yMovement < -0.0001f) {
      float bottomBorder = getY() + yMovement;
      Rectangle hitBoundsLeft = getCollisionObjectBoundaries(getX(), bottomBorder);
      Rectangle hitBoundsRight = getCollisionObjectBoundaries(getX() + getWidth(), bottomBorder);
      float adjustment = collisionSkin - getY();

      if (hitBoundsLeft != null) {
        yMovement = hitBoundsLeft.y + hitBoundsLeft.height + adjustment;
      }
      if (hitBoundsRight != null) {
        yMovement = Math.max(yMovement, hitBoundsRight.y + hitBoundsRight.height + adjustment);
      }
    }
    return yMovement;
  }

  public boolean hasIngredient() {
    return !ingredientStack.empty();
  }

  public boolean canGrabIngredient() {
    return ingredientStack.hasSpace();
  }

  public void grabIngredient(Ingredient ingredient) {
    ingredientStack.push(ingredient);
  }

  public Ingredient placeIngredient() {
    return ingredientStack.pop();
  }

  public void setInputVector(float x, float y) {
    inputVector.x = x;
    inputVector.y = y;
    if (inputVector.len() > 1f) {
      inputVector.nor();
    }
  }

  public boolean isInputEnabled() {
    return inputEnabled;
  }

  public void setInputEnabled(boolean inputEnabled) {
    this.inputEnabled = inputEnabled;
  }
}

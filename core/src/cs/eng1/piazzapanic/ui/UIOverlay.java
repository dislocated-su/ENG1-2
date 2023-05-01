package cs.eng1.piazzapanic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.PlayerState.PowerUp;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.food.interfaces.Holdable;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.piazzapanic.ui.ButtonManager.ButtonColour;
import java.util.Collection;
import java.util.List;

// if you can just avoid
// please dont mark us on this

public class UIOverlay {

    private final PiazzaPanicGame game;

    private Stage uiStage;

    private Table recipeBook, recipeBookRoot, recipeBookSteps;

    private Label reputationLabel;

    private Table activePowerups;

    private final VerticalGroup orderGroup;

    private Stack chefInventoryRoot;
    private VerticalGroup chefInventory;

    private Stack chefDisplay;
    private Image chefImage;

    private final Texture pointer;
    private final TextureRegionDrawable removeBtnDrawable;

    // private final Label recipeCountLabel;
    private final Label resultLabel;

    private UIStopwatch timer;
    private final UIStopwatch resultTimer;
    public boolean pauseToggle = false;

    private Boolean activatedShop = false;

    private Value scale;

    public UpgradesUi upgradesUi;

    private Label moneyLabel;

    public UIOverlay(Stage uiStage, final PiazzaPanicGame game) {
        this.game = game;
        this.uiStage = uiStage;

        // Initialize table
        Table root = new Table();
        root.setFillParent(true);
        root.center().top().pad(15f);
        uiStage.addActor(root);
        root.debug();

        Table upgradeButtonTable = new Table();
        upgradeButtonTable.setFillParent(true);
        upgradeButtonTable.bottom().pad(15f).padRight(60f);
        uiStage.addActor(upgradeButtonTable);

        activePowerups = new Table();
        activePowerups.padLeft(25f).padRight(15f);

        TextureRegionDrawable greyBackground = new TextureRegionDrawable(
                new Texture("Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/green_button_gradient_down.png"));

        activePowerups.setBackground(greyBackground);
        removeBtnDrawable = new TextureRegionDrawable(
                new Texture(
                        "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_crossWhite.png"));

        // Initialise pointer image
        pointer = new Texture(
                "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/blue_sliderDown.png");

        createChefInventory();

        createGameTimer();

        // Initialize the UI to display the currently requested recipe
        Stack recipeDisplay = new Stack();
        Image orderGroupBG = new Image(
                new Texture(
                        "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_button_square_gradient_down.png"));

        orderGroup = new VerticalGroup();
        orderGroupBG.setVisible(false);
        recipeDisplay.add(orderGroupBG);
        recipeDisplay.add(orderGroup);

        // Initialize counter for showing remaining recipes
        // LabelStyle counterStyle = new LabelStyle(
        // game.getFontManager().getHeaderFont(),
        // Color.BLACK
        // );
        // recipeCountLabel = new Label("0", counterStyle);

        // Initialize winning label
        LabelStyle labelStyle = new Label.LabelStyle(
                game.getFontManager().getTitleFont(),
                null);

        resultLabel = new Label("Congratulations! Your final time was:", labelStyle);
        resultLabel.setVisible(false);
        resultTimer = new UIStopwatch(labelStyle);
        resultTimer.setVisible(false);

        // Add everything
        scale = Value.percentWidth(0.04f, root);
        Value timerWidth = Value.percentWidth(0.12f, root);
        Value moneyWidth = Value.percentWidth(0.2f, root);
        Value upgradeButtonScale = Value.percentWidth(0.08f, root);

        root.add(chefDisplay).left().width(scale).height(scale);
        root.add(reputationLabel).expandX().width(moneyWidth).height(scale);
        root.add(timer).expandX().width(timerWidth).height(scale);
        root.add(moneyLabel).expandX().width(moneyWidth).height(scale);
        root.add(homeButton()).right().width(scale).height(scale);
        root.row().padTop(10f);
        root.add(chefInventoryRoot).left().top().width(scale);
        root.add().colspan(3);
        root.add(recipeDisplay).right().top().width(scale);
        root.row();
        root.row();
        root.add().expandY();
        root.row();
        upgradeButtonTable
                .add(upgradesButton())
                .width(upgradeButtonScale)
                .height(scale)
                .expandX()
                .left();
        upgradeButtonTable.add().expandX();
        upgradeButtonTable.add(activePowerups).height(scale).left();

        // Initialize button for Upgrade implementation
        recipeBookRoot = new Table();
        recipeBookRoot.setFillParent(true);
        recipeBook = new Table();
        recipeBookRoot.add(recipeBook).width(300).height(450);
        recipeBookSteps = new Table();

        createRecipeTable();
    }

    private void createChefInventory() {
        // Initialize UI for showing current chef
        this.chefDisplay = new Stack();
        chefDisplay.add(
                new Image(
                        new Texture(
                                "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_button_square_gradient_down.png")));
        chefImage = new Image();
        chefImage.setScaling(Scaling.fit);
        chefDisplay.add(chefImage);

        // Initialize UI for showing current chef's ingredient stack
        chefInventoryRoot = new Stack();
        Image chefInventoryBG = new Image(
                new Texture(
                        "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_button_square_gradient_down.png"));

        chefInventoryRoot.add(chefInventoryBG);
        chefInventory = new VerticalGroup();
        chefInventory.padBottom(10f);
        chefInventoryRoot.add(chefInventory);
    }

    public void createGameTimer() {
        // Initialize the timer
        LabelStyle timerStyle = new Label.LabelStyle(
                game.getFontManager().getHeaderFont(),
                null);

        Drawable background = new TextureRegionDrawable(
                new Texture(
                        "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/green_button_gradient_down.png"));

        background.setMinWidth(300);
        timerStyle.background = background;

        reputationLabel = new Label(" Reputation : 3 ", timerStyle);
        reputationLabel.setAlignment(Align.center);
        moneyLabel = new Label(" Money: £" + String.format("%.0f", PlayerState.getInstance().getCash()),
                timerStyle);
        moneyLabel.setAlignment(Align.center);

        timer = new UIStopwatch(timerStyle) {
            @Override
            public void updateTimer() {
                super.updateTimer();
                updateCash();
            }
        };
        timer.setAlignment(Align.center);

        uiStage.addListener(
                new InputListener() {
                    @Override
                    public boolean keyDown(InputEvent event, int keycode) {
                        if (keycode == Keys.ESCAPE) {
                            if (PlayerState.getInstance().getPaused()) {
                                resume();
                            } else {
                                pause();
                            }
                        }
                        return true;
                    }
                });
    }

    public void updateReputationCounter(int reputation) {
        reputationLabel.setText(" Reputation : " + reputation + " ");
    }

    /**
     * Reset values and UI to be in their default state.
     */
    public void init() {
        timer.reset();
        timer.start();
        resultLabel.setVisible(false);
        resultTimer.setVisible(false);
        updateChefUI(null);
    }

    /**
     * Show the image of the currently selected chef as well as have the stack of
     * ingredients
     * currently held by the chef.
     *
     * @param chef The chef that is currently selected for which to show the UI.
     */
    public void updateChefUI(final Chef chef) {
        if (chef == null) {
            chefImage.setDrawable(null);
            chefInventory.clearChildren();
            chefInventoryRoot.setVisible(false);
            return;
        }
        chefInventoryRoot.setVisible(!chef.getStack().isEmpty());
        Texture texture = chef.getTexture();
        chefImage.setDrawable(new TextureRegionDrawable(texture));

        chefInventory.clearChildren();
        for (Holdable ingredient : chef.getStack()) {
            Image image = new Image(ingredient.getTexture());
            image.getDrawable().setMinHeight(chefDisplay.getHeight());
            image.getDrawable().setMinWidth(chefDisplay.getWidth());
            chefInventory.addActor(image);
        }
        if (!chef.getStack().isEmpty()) {
            ImageButton btn = game
                    .getButtonManager()
                    .createImageButton(removeBtnDrawable, ButtonColour.RED, -1.5f);
            btn.addListener(
                    new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            chef.popFood();
                        }
                    });
            chefInventory.addActor(btn);
        }
    }

    /**
     * Show the label displaying that the game has finished along with the time it
     * took to complete.
     */
    public void finishGameUI() {
        resultLabel.setVisible(true);
        resultTimer.setTime(timer.getTime());
        resultTimer.setVisible(true);
        timer.stop();
    }

    /**
     * Update method for the ugprade button.
     */
    public TextButton upgradesButton() {
        TextButton upgrades;

        upgradesUi = game.getUpgradesUi();
        upgradesUi.addToStage(uiStage);

        upgrades = game
                .getButtonManager()
                .createTextButton("Upgrades", ButtonManager.ButtonColour.BLUE);
        upgrades.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (activatedShop == false) { // to check whether to hid or unhide the upgrades panel
                            upgradesUi.visible(true);
                            activatedShop = true;
                            upgrades.setText("Return");
                            pause();
                        } else {
                            upgradesUi.visible(false);
                            activatedShop = false;
                            upgrades.setText("Upgrades");
                            resume();
                        }
                    }
                });
        return upgrades;
    }

    private ImageButton homeButton() {
        // Initialize the home button
        ImageButton homeButton = game
                .getButtonManager()
                .createImageButton(
                        new TextureRegionDrawable(
                                new Texture(
                                        Gdx.files.internal(
                                                "Kenney-Game-Assets-1/2D assets/Game Icons/PNG/White/1x/home.png"))),
                        ButtonManager.ButtonColour.BLUE,
                        -1.5f);
        homeButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.loadHomeScreen();
                    }
                });
        return homeButton;
    }

    Recipe lastShown = null;

    /**
     * Describe the cooking process of a recipe to the user.
     *
     * @param recipe The recipe to display the ingredients for.
     */
    public void updateRecipeUI(Recipe recipe) {
        // recipe will be null when we reach the end of the scenario
        if (recipe == null || lastShown == recipe) {
            lastShown = null;
            recipeBookSteps.clear();
            recipeBookRoot.setVisible(false);
            resume();
            return;
        }

        pause();

        lastShown = recipe;

        String recipeString = recipe.getType();
        recipeString = "\"" +
                Character.toUpperCase(recipeString.charAt(0)) +
                recipeString.substring(1) +
                "\"";
        recipeName.setText(recipeString);

        recipeBookRoot.setVisible(true);
        recipeBookSteps.clear();
        stepName(recipe);
        recipeBookSteps.row();
        recipeBookSteps.add(new Image(pointer)).colspan(5).center().row();

        for (String recipeIngredient : recipe.getRecipeIngredients()) {
            Image image = new Image(
                    recipe.getTextureManager().getTexture(recipeIngredient));
            image.getDrawable().setMinWidth(64);
            image.getDrawable().setMinHeight(64);
            recipeBookSteps.add(image);
        }
        if (recipe.getType() == "pizza") {
            recipeBookSteps.row();
            recipeBookSteps.add(new Image(pointer)).colspan(5).center().row();
            Image uncooked = new Image(
                    recipe.getTextureManager().getTexture("uncooked_pizza"));
            uncooked.getDrawable().setMinWidth(64);
            uncooked.getDrawable().setMinHeight(64);
            recipeBookSteps.add(uncooked).colspan(5).center();
        }
        recipeBookSteps.row();
        recipeBookSteps.add(new Image(pointer)).colspan(5).center().row();
        Image image = new Image(recipe.getTexture());
        image.getDrawable().setMinWidth(64);
        image.getDrawable().setMinHeight(64);
        recipeBookSteps.add(image).colspan(5).center();
        // recipeBookSteps.row().colspan(i);

    }

    public void stepName(Recipe recipe) {
        List<String> ingredients = recipe.getRecipeIngredients();
        for (String ingredientName : ingredients) {
            String[] temp = ingredientName.split("_", 2);
            String foodName = temp[0];
            if (temp.length == 2) {
                Gdx.app.log("Split", temp[0] + "___" + temp[1]);
                switch (foodName) {
                    case "cheese":
                    case "potato":
                        break;
                    default:
                        foodName = foodName + "_raw";
                        break;
                }
            }
            Image image = new Image(
                    recipe.getTextureManager().getTexture(foodName));

            image.getDrawable().setMinWidth(64);
            image.getDrawable().setMinHeight(64);
            recipeBookSteps.add(image);
        }
    }

    /**
     * Update the vertical list of orders.
     */
    public void updateOrders(Collection<Recipe> orders) {
        generateOrders(orders, scale.get(), scale.get());
    }

    public void resizeOrders(
            Collection<Recipe> orders,
            float width,
            float height) {
        generateOrders(orders, 0.04f * width, 0.075f * height);
    }

    private void generateOrders(
            Collection<Recipe> orders,
            float width,
            float height) {
        orderGroup.clearChildren();

        for (Recipe order : orders) {
            Image recipeImage = new Image(order.getTexture());
            recipeImage.getDrawable().setMinHeight(height);
            recipeImage.getDrawable().setMinWidth(width);
            ImageButton recipeButton = game
                    .getButtonManager()
                    .createImageButton(
                            recipeImage.getDrawable(),
                            ButtonManager.ButtonColour.GREY,
                            0.5f);

            recipeButton.addListener(
                    new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            updateRecipeUI(order);
                        }
                    });
            orderGroup.addActor(recipeButton);
        }
    }

    Label recipeName;

    public void createRecipeTable() {
        recipeBookRoot.setVisible(false);
        recipeBook.clear();

        recipeBookRoot.right().padRight(chefDisplay.getWidth());

        LabelStyle recipeNameStyle = new LabelStyle(
                game.getFontManager().getHeaderFont(),
                null);
        recipeName = new Label("Recipe", recipeNameStyle);

        Stack titleStack = new Stack();

        ImageButton hideButton = game
                .getButtonManager()
                .createImageButton(removeBtnDrawable, ButtonColour.BLUE, -1.5f);

        hideButton.setHeight(0.5f);

        hideButton.addListener(
                new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        recipeBookRoot.setVisible(false);
                        resume();
                        lastShown = null;
                    }
                });

        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.add().width(250).expandX();
        buttonTable.add(hideButton).right();
        Table titleTable = new Table();
        titleTable.add(recipeName);
        titleStack.add(titleTable);
        titleStack.add(buttonTable);

        recipeBook.add(titleStack).padTop(10).expandX().row();

        recipeBook.add(recipeBookSteps).colspan(2).expand();

        TextureRegionDrawable textureRegionDrawableBg = new TextureRegionDrawable(
                new Texture(Gdx.files.internal("Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_panel.png")));
        recipeBook.setBackground(textureRegionDrawableBg.tint(Color.GRAY));
        recipeBookRoot.row();

        uiStage.addActor(recipeBookRoot);
    }

    public void currentActivePowerup() {
        activePowerups.clearChildren();

        PlayerState state = PlayerState.getInstance();

        int activePowerUpCount = 0;
        for (PowerUp powerup : PowerUp.values()) {
            // Check if powerup is active
            if (state.getBuffActive(powerup)) {
                activePowerUpCount++;
                Label activePowerupLabel = new Label(
                        String.format(""),
                        new LabelStyle(
                                game.getFontManager().getMediumFont(),
                                Color.WHITE));

                String labelText = PlayerState.getInstance().getPowerupName(powerup);

                labelText += ": " +
                        String.format("%d", state.getBuffRemaining(powerup) / 1000);

                activePowerupLabel.setText(labelText);
                activePowerups.add(activePowerupLabel).width(130).padRight(15);
            }
        }
        activePowerups.setVisible(!(activePowerUpCount == 0));
    }

    private void pause() {
        PlayerState.getInstance().pause();
        pauseToggle = true;
    }

    private void resume() {
        PlayerState.getInstance().resume();
        pauseToggle = true;
    }

    public void update() {
        currentActivePowerup();
    }

    public void resize(int width, int height) {
    }

    public void updateCash() {
        moneyLabel.setText(" Money: £" + String.format("%.0f", PlayerState.getInstance().getCash()));
    }
}

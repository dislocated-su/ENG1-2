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
import cs.eng1.piazzapanic.screens.EndScreen;
import cs.eng1.piazzapanic.ui.ButtonManager.ButtonColour;
import java.util.Collection;
import java.util.List;

// if you can just avoid
// please dont mark us on this

public class UIOverlay {

    private final PiazzaPanicGame game;
    private Stage uiStage;

    private Table recipeBook, recipeBookRoot, recipeBookSteps;
    private Table activePowerups;
    private Table floatingBottomTable;
    private Table root;

    private Stack chefInventoryRoot;
    private VerticalGroup chefInventory;

    private Stack chefDisplay;
    private Image chefImage;

    private VerticalGroup orderGroup;

    private final TextureRegionDrawable crossButtonDrawable;

    public boolean pauseToggle = false;
    public boolean paused = false;
    private boolean activatedShop = false;

    private Value scale;

    private PauseOverlay pauseOverlay;

    public UIOverlay(Stage uiStage, final PiazzaPanicGame game) {
        this.game = game;
        this.uiStage = uiStage;

        this.pauseOverlay = new PauseOverlay(uiStage, game, this);

        // Commonly reused green background
        Drawable greenPillBG = new TextureRegionDrawable(
                new Texture(
                        "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/green_button_gradient_down.png"));

        // Commonly reused grey background
        TextureRegionDrawable greyPillBG = new TextureRegionDrawable(
                new Texture(
                        "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_button_square_gradient_down.png"));

        // X button
        crossButtonDrawable = new TextureRegionDrawable(
                new Texture(
                        "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_crossWhite.png"));

        // Arrow down
        pointer = new Texture(
                "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/blue_sliderDown.png");

        // Initialize table
        root = new Table();
        root.setFillParent(true);
        root.center().top().pad(15f);
        uiStage.addActor(root);
        root.debug();

        floatingBottomTable = new Table();
        floatingBottomTable.setFillParent(true);
        floatingBottomTable.bottom().pad(15f).padRight(60f);
        uiStage.addActor(floatingBottomTable);

        activePowerups = new Table();
        activePowerups.padLeft(25f).padRight(15f);
        activePowerups.setBackground(greenPillBG);

        createChefInventory(greyPillBG);

        createGameTimer(greenPillBG);

        orderGroup = new VerticalGroup();

        Stack recipeDisplay = new Stack();
        recipeDisplay.add(new Image(greyPillBG));
        recipeDisplay.add(orderGroup);

        // Add everything
        scale = Value.percentWidth(0.04f, root);
        Value timerWidth = Value.percentWidth(0.12f, root);
        Value moneyWidth = Value.percentWidth(0.2f, root);
        Value upgradeButtonScale = Value.percentWidth(0.08f, root);

        // Root Table (Main UI)
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

        // Floating elements on the bottom
        floatingBottomTable
                .add(upgradesButton())
                .width(upgradeButtonScale)
                .height(scale)
                .expandX()
                .left();
        floatingBottomTable.add().expandX();
        floatingBottomTable.add(activePowerups).height(scale).left();

        // Initialize button for Upgrade implementation
        recipeBookRoot = new Table();
        recipeBookRoot.setFillParent(true);
        recipeBook = new Table();
        recipeBookRoot.add(recipeBook).width(300).height(450);
        recipeBookSteps = new Table();

        createRecipeTable();
    }

    private void createChefInventory(Drawable background) {
        // Initialize UI for showing current chef
        this.chefDisplay = new Stack();
        chefDisplay.add(new Image(background));
        chefImage = new Image();
        chefImage.setScaling(Scaling.fit);
        chefDisplay.add(chefImage);

        // Initialize UI for showing current chef's ingredient stack
        chefInventoryRoot = new Stack();

        chefInventoryRoot.add(new Image(background));
        chefInventory = new VerticalGroup();
        chefInventory.padBottom(10f);
        chefInventoryRoot.add(chefInventory);
    }

    private Label reputationLabel;
    private Label moneyLabel;
    private UIStopwatch timer;

    private void createGameTimer(Drawable background) {
        // Initialize the timer
        LabelStyle timerStyle = new Label.LabelStyle(
                game.getFontManager().getHeaderFont(),
                null);

        background.setMinWidth(300);
        timerStyle.background = background;

        reputationLabel = new Label("", timerStyle);
        updateReputationCounter(3);
        reputationLabel.setAlignment(Align.center);
        moneyLabel = new Label(
                " Money: £" +
                        String.format("%.0f", PlayerState.getInstance().getCash()),
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
                            if (paused) {
                                pauseOverlay.hide();
                                show();
                                resume();
                            } else {
                                hide();
                                pauseOverlay.show();
                                pause();
                            }
                        }
                        return true;
                    }
                });
    }

    private Label recipeName;
    private UpgradesUi upgradesUi;

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
                .createImageButton(crossButtonDrawable, ButtonColour.BLUE, -1.5f);

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
                new Texture(
                        Gdx.files.internal(
                                "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_panel.png")));
        recipeBook.setBackground(textureRegionDrawableBg.tint(Color.GRAY));
        recipeBookRoot.row();

        uiStage.addActor(recipeBookRoot);
    }

    public void updateReputationCounter(int reputation) {
        reputationLabel.setText("Reputation: " + reputation + " ");
    }

    /**
     * Reset values and UI to be in their default state.
     */
    public void init() {
        timer.reset();
        timer.start();
        updateChefUI(null);
    }

    public UIStopwatch getTimer() {
        return timer;
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
                    .createImageButton(
                            crossButtonDrawable,
                            ButtonColour.RED,
                            -1.5f);
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
        pause();
        hide();
        game.setScreen(
                new EndScreen(
                        game,
                        timer.getTimeString(),
                        reputationLabel.getText().toString()));
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
                            activatedShop = true;
                            upgradesUi.show();
                            upgrades.setText("Return");
                            pause();
                        } else {
                            activatedShop = false;
                            upgradesUi.hide();
                            upgrades.setText("Upgrades");
                            resume();
                        }
                    }
                });
        return upgrades;
    }

    /**
     * Initialize the homeButton.
     */
    private ImageButton homeButton() {
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

    private Recipe lastShown = null;
    private final Texture pointer;

    /**
     * Describe the cooking process of a recipe to the user with each step and
     * ingredient.
     *
     * @param recipe The recipe to display the ingredients for.
     */
    public void updateRecipeUI(Recipe recipe) {
        // null at the end of the scenario, lastShown allows for switching current
        // RecipeUI without exiting.
        if (recipe == null || lastShown == recipe) {
            lastShown = null;
            recipeBookSteps.clear();
            recipeBookRoot.setVisible(false);
            resume();
            return;
        }

        pause();
        // Name of the recipe to show at the top of the RecipeUI
        lastShown = recipe;

        String recipeString = recipe.getType().replaceAll("_", " ");
        recipeString = Character.toUpperCase(recipeString.charAt(0)) +
                recipeString.substring(1);

        recipeName.setText(recipeString);

        recipeBookRoot.setVisible(true);
        recipeBookSteps.clear();

        // Initial uncooked ingredients
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

            Image ingredientImage = new Image(
                    recipe.getTextureManager().getTexture(foodName));

            ingredientImage.getDrawable().setMinWidth(64);
            ingredientImage.getDrawable().setMinHeight(64);
            recipeBookSteps.add(ingredientImage);
        }

        recipeBookSteps.row();
        recipeBookSteps.add(new Image(pointer)).colspan(5).center().row();

        // Intermediary cooked ingredients
        for (String recipeIngredient : recipe.getRecipeIngredients()) {
            Image image = new Image(
                    recipe.getTextureManager().getTexture(recipeIngredient));
            image.getDrawable().setMinWidth(64);
            image.getDrawable().setMinHeight(64);
            recipeBookSteps.add(image);
        }

        // Pizza has extra cooking step
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

        // Final recipe image
        Image image = new Image(recipe.getTexture());
        image.getDrawable().setMinWidth(64);
        image.getDrawable().setMinHeight(64);

        recipeBookSteps.add(image).colspan(5).center();
    }

    /**
     * Update the vertical list of orders.
     */
    public void updateOrders(Collection<Recipe> orders) {
        generateOrders(orders, scale.get(), scale.get());
    }

    /**
     * Resizes the orders to the current screen size.
     */
    public void resizeOrders(
            Collection<Recipe> orders,
            float width,
            float height) {
        generateOrders(orders, 0.04f * width, 0.075f * height);
    }

    /**
     * Generates the order ui based on the current orders.
     *
     * @param orders The full list of orders to be delivered currently.
     * @param width  The width of the order images.
     * @param height The height of the order images.
     */
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

    /**
     * Refreshes the UI based on current active powerups.
     */
    private void updateActivePowerups() {
        activePowerups.clearChildren();
        activePowerups.setVisible(false);

        PlayerState state = PlayerState.getInstance();
        for (PowerUp powerup : PowerUp.values()) {
            // Check if powerup is active
            if (state.getBuffActive(powerup)) {
                activePowerups.setVisible(true);
                Label activePowerupLabel = new Label(
                        String.format(""),
                        new LabelStyle(
                                game.getFontManager().getMediumFont(),
                                Color.WHITE));

                String labelText = state.getPowerupName(powerup);

                labelText += ": " +
                        String.format("%d", state.getBuffRemaining(powerup) / 1000);

                activePowerupLabel.setText(labelText);
                activePowerups.add(activePowerupLabel).width(130).padRight(15);
            }
        }
    }

    /**
     * Updates the Money UI label at the top of the screen.
     */
    private void updateCash() {
        moneyLabel.setText(
                " Money: £" +
                        String.format("%.0f", PlayerState.getInstance().getCash()));
    }

    /**
     * Pauses the game
     */
    private void pause() {
        paused = true;
        pauseToggle = true;

    }

    /**
     * Resumes the game
     */
    protected void resume() {
        paused = false;
        pauseToggle = true;
    }

    /**
     * Updates active powerups and their timers.
     */
    public void update() {
        updateActivePowerups();
    }

    public void show() {
        chefInventory.setVisible(true);
        orderGroup.setVisible(true);
        root.setVisible(true);
        floatingBottomTable.setVisible(true);
    }

    public void hide() {
        recipeBookRoot.setVisible(false);
        root.setVisible(false);
        upgradesUi.hide();
        floatingBottomTable.setVisible(false);
    }
}

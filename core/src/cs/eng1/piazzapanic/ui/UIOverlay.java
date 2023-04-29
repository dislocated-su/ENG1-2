package cs.eng1.piazzapanic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
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
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.food.interfaces.Holdable;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.piazzapanic.ui.ButtonManager.ButtonColour;
import java.util.Collection;

public class UIOverlay {

    private final Image pointer;
    private final Stack chefDisplay;
    private final Image chefImage;
    private final VerticalGroup chefInventory;
    private final Stack chefInventoryRoot;
    private final TextureRegionDrawable removeBtnDrawable;
    //private final Image orderGroupBG;
    private final VerticalGroup orderGroup;
    private final UIStopwatch timer;
    private final Label recipeCountLabel;
    private final Label resultLabel;
    private final UIStopwatch resultTimer;
    private final PiazzaPanicGame game;
    private Table recipeBook, recipeBookRoot;
    Boolean activatedShop = false;
    private Stage uiStage;
    private Value scale;
    private Table recipeBookSteps;

    public UIOverlay(Stage uiStage, final PiazzaPanicGame game) {
        this.game = game;
        this.uiStage = uiStage;

        // Initialize table
        Table root = new Table();
        root.setFillParent(true);
        root.center().top().pad(15f);
        uiStage.addActor(root);
        
        // Initialize UI for showing current chef
        this.chefDisplay = new Stack();
        chefDisplay.add(
            new Image(
                new Texture(
                    "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_button_square_gradient_down.png"
                )
            )
        );
        chefImage = new Image();
        chefImage.setScaling(Scaling.fit);
        chefDisplay.add(chefImage);

        // Initialize button for Upgrade implementation
        upgradesButton(uiStage);

        // Initialise pointer image
        pointer =
            new Image(
                new Texture(
                    "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/blue_sliderDown.png"
                )
            );
        pointer.setScaling(Scaling.none);



        // Initialize UI for showing current chef's ingredient stack
        chefInventoryRoot = new Stack();
        Image ingredientImagesBG =
            new Image(
                new Texture(
                    "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_button_square_gradient_down.png"
                )
            );
        chefInventoryRoot.add(ingredientImagesBG);
        chefInventory = new VerticalGroup();
        chefInventory.padBottom(10f);
        chefInventoryRoot.add(chefInventory);

        // Initialize the timer
        LabelStyle timerStyle = new Label.LabelStyle(
            game.getFontManager().getTitleFont(),
            null
        );
        timerStyle.background =
            new TextureRegionDrawable(
                new Texture(
                    "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/green_button_gradient_down.png"
                )
            );
        timer = new UIStopwatch(timerStyle);
        timer.setAlignment(Align.center);

        // Initialize the home button
        ImageButton homeButton = game
            .getButtonManager()
            .createImageButton(
                new TextureRegionDrawable(
                    new Texture(
                        Gdx.files.internal(
                            "Kenney-Game-Assets-1/2D assets/Game Icons/PNG/White/1x/home.png"
                        )
                    )
                ),
                ButtonManager.ButtonColour.BLUE,
                -1.5f
            );
        homeButton.addListener(
            new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.loadHomeScreen();
                }
            }
        );

        
        removeBtnDrawable =
            new TextureRegionDrawable(
                new Texture(
                    "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_crossWhite.png"
                )
            );

        // Initialize the UI to display the currently requested recipe
        Stack recipeDisplay = new Stack();
        Image orderGroupBG =
            new Image(
                new Texture(
                    "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_button_square_gradient_down.png"
                )
            );
        orderGroup = new VerticalGroup();
        orderGroupBG.setVisible(false);
        recipeDisplay.add(orderGroupBG);
        recipeDisplay.add(orderGroup);

        // Initialize counter for showing remaining recipes
        LabelStyle counterStyle = new LabelStyle(
            game.getFontManager().getHeaderFont(),
            Color.BLACK
        );
        recipeCountLabel = new Label("0", counterStyle);

        // Initialize winning label
        LabelStyle labelStyle = new Label.LabelStyle(
            game.getFontManager().getTitleFont(),
            null
        );
        resultLabel =
            new Label("Congratulations! Your final time was:", labelStyle);
        resultLabel.setVisible(false);
        resultTimer = new UIStopwatch(labelStyle);
        resultTimer.setVisible(false);

        // Add everything
        scale = Value.percentWidth(0.04f, root);
        Value timerWidth = Value.percentWidth(0.2f, root);
        root.add(chefDisplay).left().width(scale).height(scale);
        root.add(timer).expandX().width(timerWidth).height(scale);
        root.add(homeButton).right().width(scale).height(scale);
        root.row().padTop(10f);
        root.add(chefInventoryRoot).left().top().width(scale);
        root.add().expandX().width(timerWidth);
        root.add(recipeDisplay).right().top().width(scale);
        root.row();
        root.add(resultLabel).colspan(3);
        root.row();
        root.add(resultTimer).colspan(3);

        recipeBookRoot = new Table();
        recipeBookRoot.setFillParent(true);
        recipeBook = new Table();
        recipeBookSteps = new Table();
        createRecipeTable();

        root.debug();
        
    }

    private void createLayout(Table root) {

    }

    /**
     * Reset values and UI to be in their default state.
     */
    public void init() {
        timer.reset();
        timer.start();
        resultLabel.setVisible(false);
        resultTimer.setVisible(false);
        // displayIngredients.setVisible(false);
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
                }
            );
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
    public void upgradesButton(Stage uiStage) {
        String upgradeUiButtonText;
        TextButton upgrades;

        final UpgradesUi upgradesUi = game.getUpgradesUi();
        upgradesUi.addToStage(uiStage);

        if (activatedShop == false) {
            upgradeUiButtonText = "Upgrades";
        } else {
            upgradeUiButtonText = "Return";
        }
        upgrades =
            game
                .getButtonManager()
                .createTextButton(
                    upgradeUiButtonText,
                    ButtonManager.ButtonColour.BLUE
                );
        upgrades.sizeBy(1f);
        upgrades.addListener(
            new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (activatedShop == false) { // to check whether to hid or unhide the upgrades panel
                        upgradesUi.visible(true);
                        activatedShop = true;
                        upgradesButton(uiStage);
                    } else {
                        upgradesUi.visible(false);
                        activatedShop = false;
                        upgradesButton(uiStage);
                    }
                }
            }
        );

        Table bottomTable = new Table();
        uiStage.addActor(bottomTable);
        bottomTable.padBottom(60).padLeft(120);
        bottomTable.add(upgrades).width(100).left();
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
            return;
        }

        lastShown = recipe;

        recipeBookRoot.setVisible(true);
        recipeBookSteps.clear();
        for (String recipeIngredient : recipe.getRecipeIngredients()) {
            Image image = new Image(
                recipe.getTextureManager().getTexture(recipeIngredient)
            );
            image.getDrawable().setMinHeight(chefDisplay.getHeight());
            image.getDrawable().setMinWidth(chefDisplay.getWidth());
            recipeBookSteps.add(image);
        }

        //recipeBookSteps.row().colspan(i);

    }

    /**
     * Update the vertical list of orders.
     */
    public void updateOrders(Collection<Recipe> orders) {
        generateOrders(orders, scale.get(), scale.get());
    }

    public void resizeOrders(Collection<Recipe> orders, float width, float height) {
         generateOrders(orders, 0.04f * width, 0.075f * height);
    }

    private void generateOrders(Collection<Recipe> orders, float width, float height) {
        orderGroup.clearChildren();
    
        for (Recipe order : orders) {
            Image recipeImage = new Image(order.getTexture());
            recipeImage.getDrawable().setMinHeight(height);
            recipeImage.getDrawable().setMinWidth(width);
            ImageButton recipeButton = game
            .getButtonManager()
            .createImageButton(recipeImage.getDrawable(), ButtonManager.ButtonColour.GREY, 0.5f);

            recipeButton
            .addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    updateRecipeUI(order);   
                }
            });
            orderGroup.addActor(recipeButton);
        }
    }
    


    public void createRecipeTable() {
        recipeBookRoot.clear();
        recipeBookRoot.setVisible(false);
        recipeBook.clear();

        recipeBookSteps.setWidth(80);
        recipeBookSteps.setHeight(80);
        recipeBookRoot.right().padRight(chefDisplay.getWidth());
        
        LabelStyle recipeNameStyle = new LabelStyle(game.getFontManager().getTitleFont(), null);
        Label recipeName = new Label("Recipe", recipeNameStyle);

        recipeBook.add(recipeName).expandX();

        ImageButton hideButton = game
            .getButtonManager()
            .createImageButton(removeBtnDrawable, ButtonColour.BLUE, -1.5f);

        hideButton.setHeight(0.5f);


        hideButton.addListener(
            new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    recipeBookRoot.setVisible(false);
                    lastShown = null;
                }
            }
        );

        recipeBook.add(hideButton).top().right().row();
        
        recipeBookRoot.add(recipeBook).width(chefDisplay.getWidth() * 2f).height(chefDisplay.getHeight() * 2f);
        recipeBook.add(recipeBookSteps).colspan(2).expand();

        TextureRegionDrawable textureRegionDrawableBg =
        new TextureRegionDrawable(
            new Texture(Gdx.files.internal("backgroundimage.jpg"))
        );
        recipeBook.setBackground(textureRegionDrawableBg);
        recipeBookRoot.row();

        uiStage.addActor(recipeBookRoot);
        recipeBook.debug();
        recipeBookRoot.debug();
    }

    public void resize(int width, int height) {
    }
}

package cs.eng1.piazzapanic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.PlayerState.PowerUp;

public class UpgradesUi {

    private static Table table;
    Table root;
    private LabelStyle hudLabelFont;
    private LabelStyle hudTitleFont;
    private LabelStyle hudHeaderFont;

    private PiazzaPanicGame game;

    TextButton buyChef;

    private Value scale, scale1, scale2;
    private Label chefHireCostLabel;
    public boolean chefHireFlag;

    public UpgradesUi(PiazzaPanicGame game) {
        this.game = game;
        root = new Table();
        root.setFillParent(true);
        table = new Table();

        TextureRegionDrawable textureRegionDrawableBg = new TextureRegionDrawable(
                new Texture(
                        Gdx.files.internal(
                                "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_panel.png")));

        root.add(table).width(450).height(350).center();

        table.setBackground(textureRegionDrawableBg.tint(Color.DARK_GRAY));
        table.setVisible(false);

        // fonts used in the "shop"
        FontManager fontManager = new FontManager();

        hudLabelFont = new Label.LabelStyle();
        hudLabelFont.font = fontManager.getLabelFont();

        hudTitleFont = new Label.LabelStyle();
        hudTitleFont.font = fontManager.getTitleFont();

        hudHeaderFont = new Label.LabelStyle();
        hudHeaderFont.font = fontManager.getHeaderFont();

        // setting up the table in the 'Shop'
        this.scale2 = Value.percentWidth(0.3f, table);
        this.scale = Value.percentWidth(0.08f, table);
        this.scale1 = Value.percentWidth(0.15f, table);

        createShopTable();
    }

    /**
     * Initialises the shop table.
     */
    public void createShopTable() {
        Label timeHeader = new Label("Active For:", hudLabelFont);
        Label powerupHeader = new Label("Powerups + Upgrades", hudLabelFont);
        Label costHeader = new Label("Cost:", hudLabelFont);
        Label titleLabel = new Label("Upgrades Shop", hudTitleFont);

        table.add(titleLabel).colspan(3).padRight(15);
        table.row();
        table.add(powerupHeader).width(scale2).height(scale).pad(5);
        table.add(timeHeader).width(scale1).height(scale).pad(5).center();
        table.add(costHeader).width(scale1).height(scale).pad(5);
        table.row();

        for (PowerUp powerUp : PowerUp.values()) {
            createRow(powerUp);
        }

        buyChef = game
                .getButtonManager()
                .createTextButton("Extra chef", ButtonManager.ButtonColour.RED);
        buyChef.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        PlayerState playerState = PlayerState.getInstance();
                        if (playerState.getCash() >= playerState.getChefHireCost(false)) {
                            int purchasedChefs = playerState.getPurchasedChefs();
                            if (purchasedChefs <= 1) {

                                playerState.spendCash(playerState.getChefHireCost(true));

                                chefHireFlag = true;
                                playerState.setPurchasedChefs(purchasedChefs + 1);
                                updateChefHireCost();
                            }
                        }
                    }
                });

        chefHireCostLabel = new Label("", hudLabelFont);
        updateChefHireCost();
        table.add(buyChef).width(scale2).height(scale);
        table.add();
        table.add(chefHireCostLabel);
    }

    /**
     * Updates the cost for hiring a chef after buying one.
     */
    private void updateChefHireCost() {
        if (PlayerState.getInstance().getPurchasedChefs() >= 2) {
            buyChef.setVisible(false);
            chefHireCostLabel.setVisible(false);
        }
        chefHireCostLabel.setText(
                "£" + PlayerState.getInstance().getChefHireCost(false));
    }

    /**
     * Creates a new row in powerUpTable
     *
     * @param powerUp The powerUp the new row is for.
     */
    public void createRow(PowerUp powerUp) {
        PlayerState playerState = PlayerState.getInstance();
        String powerUpName = playerState.getPowerupName(powerUp);
        int cost = playerState.getPowerupCost(powerUp);
        String time = String.valueOf(
                playerState.getBuffDuration(powerUp) / 1000);

        // sets it font, format and value
        Label timerLabel = new Label(String.format(time + " s"), hudLabelFont);

        Label costLabel = new Label(String.format("£" + cost), hudLabelFont);

        TextButton button = game
                .getButtonManager()
                .createTextButton(powerUpName, ButtonManager.ButtonColour.BLUE);

        button.addListener(
                new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        PlayerState playerState = PlayerState.getInstance();
                        if (cost <= playerState.getCash() &&
                                !playerState.getBuffActive(powerUp)) {
                            playerState.spendCash(cost);
                            playerState.activateBuff(powerUp);
                        }
                    }
                });

        table.add(button).width(scale2).height(scale).pad(3);
        table.add(timerLabel).width(scale).height(scale).pad(3);
        table.add(costLabel).width(scale).height(scale).pad(3);
        table.row();
    }

    public void hide() {
        table.setVisible(false);
    }

    public void show() {
        table.setVisible(true);
    }

    public void addToStage(Stage uiStage) {
        uiStage.addActor(root);
    }
}

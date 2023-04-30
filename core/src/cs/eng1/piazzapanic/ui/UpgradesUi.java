package cs.eng1.piazzapanic.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.PlayerState.PowerUp;

public class UpgradesUi {

    private static Table table;
    Table root;
    private LabelStyle hudLabelFont;
    private LabelStyle hudTitleFont;
    private LabelStyle hudHeaderFont;

    private String timerForPowerUp1, timerForPowerUp2, timerForPowerUp3, timerForPowerUp4, timerForPowerUp5,
            timerForChefs;

    private String powerup1Name, powerup2Name, powerup3Name, powerup4Name, powerup5Name, moreChefsName;

    private Label titleLabel, labelForAllPowerups, moneyLabel, labelForAllTimers, timerLabel, activePowerupLabel;

    private int costForPowerUp1, costForPowerUp2, costForPowerUp3, costForPowerUp4, costForPowerUp5, costOfChef;
    private float currentMoney;

    private Label labelForAllCosts, costLabel;

    private PlayerState playerstate;

    private PiazzaPanicGame game;

    TextButton moreChefs;

    private Value scale, scale1, scale2;

    public UpgradesUi(PiazzaPanicGame game) {
        this.game = game;
        root = new Table();
        root.setFillParent(true);

        table = new Table();
        root.add(table).width(450).height(350).center();
        TextureRegionDrawable textureRegionDrawableBg = new TextureRegionDrawable(
                new Texture(Gdx.files.internal("backgroundimage.jpg")));
        table.setBackground(textureRegionDrawableBg);
        table.setVisible(false);

        playerstate = PlayerState.getInstance();

        // fonts used in the "shop"
        FontManager fontManager = new FontManager();

        hudLabelFont = new Label.LabelStyle();
        hudLabelFont.font = fontManager.getLabelFont();

        hudTitleFont = new Label.LabelStyle();
        hudTitleFont.font = fontManager.getTitleFont();

        hudHeaderFont = new Label.LabelStyle();
        hudHeaderFont.font = fontManager.getHeaderFont();

        titleLabel = new Label("Upgrades Shop", hudTitleFont);

        labelForAllTimers = new Label("Active For:", hudLabelFont);
        labelForAllPowerups = new Label("Powerups + Upgrades", hudLabelFont);
        labelForAllCosts = new Label("Cost:", hudLabelFont);

        powerup1Name = "Speedy mover";
        timerForPowerUp1 = "20"; // proof of concept
        costForPowerUp1 = 12;

        powerup2Name = "Faster cooking";
        timerForPowerUp2 = "21"; // proof of concept
        costForPowerUp2 = 13;

        powerup3Name = "Unfailable Cooking";
        timerForPowerUp3 = "22"; // proof of concept
        costForPowerUp3 = 14;

        powerup4Name = "No rep loss";
        timerForPowerUp4 = "23"; // proof of concept
        costForPowerUp4 = 15;

        powerup5Name = "More money";
        costForPowerUp5 = 23;
        timerForPowerUp5 = "40";

        moreChefsName = "Extra chef";
        timerForChefs = "N/A"; // proof of concept
        costOfChef = 50;

        // PlayerState player = PlayerState.getInstance();
        // Array<String> powerupsActive = player.getActivePowerups();
        // if (powerupsActive != null) {
        // for (String x : powerupsActive) {
        // lowerTable.add(x);
        // lowerTable.row();
        // }
        // }

        // setting up the table in the 'Shop'
        this.scale2 = Value.percentWidth(0.3f, table);
        this.scale = Value.percentWidth(0.08f, table);
        this.scale1 = Value.percentWidth(0.15f, table);

        createShopTable();
    }

    public void createShopTable() {
        table.clear();

        currentMoney = playerstate.getCash();

        moneyLabel = new Label("£" + currentMoney, hudTitleFont);

        table.add(titleLabel).colspan(2).padRight(15);
        table.add(moneyLabel);
        table.row();
        table.add(labelForAllPowerups).width(scale2).height(scale).pad(5);
        table
                .add(labelForAllTimers)
                .width(scale1)
                .height(scale)
                .pad(5)
                .center();
        table.add(labelForAllCosts).width(scale1).height(scale).pad(5);
        table.row();
        createRow(powerup1Name, costForPowerUp1, timerForPowerUp1);
        createRow(powerup2Name, costForPowerUp2, timerForPowerUp2);
        createRow(powerup3Name, costForPowerUp3, timerForPowerUp3);
        createRow(powerup4Name, costForPowerUp4, timerForPowerUp4);
        createRow(powerup5Name, costForPowerUp5, timerForPowerUp5);
        createRow(moreChefsName, costOfChef, timerForChefs);
    }

    public void createRow(String name, Integer cost, String time) {
        // sets it font, format and value
        timerLabel = new Label(String.format(time + " s"), hudLabelFont);

        costLabel = new Label(String.format("£" + cost), hudLabelFont);

        TextButton button = game
                .getButtonManager()
                .createTextButton(name, ButtonManager.ButtonColour.BLUE);

        button.addListener(
                new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        if (cost <= currentMoney) {
                            currentMoney -= cost;
                            createShopTable();
                        }
                    }
                });

        table.add(button).width(scale2).height(scale).pad(3);
        table.add(timerLabel).width(scale).height(scale).pad(3);
        table.add(costLabel).width(scale).height(scale).pad(3);
        table.row();
    }

    // foo being the boolean that makes the table visible or not

    public void visible(Boolean val) {
        table.setVisible(val);
    }

    public void addToStage(Stage uiStage) {
        uiStage.addActor(root);
    }
}

package cs.eng1.piazzapanic.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cs.eng1.piazzapanic.PiazzaPanicGame;

public class UpgradesUi {

    private static Table table;
    Table root;
    private LabelStyle hudLabelStyle;

    private int timerForPowerUp1, timerForPowerUp2, timerForPowerUp3,
            timerForPowerUp4, timerForPowerUp5;

    private Label titleLabel;

    private Label LabelForAllTimers, timerLabelPowerUp1, timerLabelPowerUp2,
            timerLabelPowerUp3, timerLabelPowerUp4,
            timerLabelPowerUp5;

    private int costForPowerUp1, costPowerUp2,
            costForPowerUp3, costForPowerUp4,
            costForPowerUp5;

    private Label labelForAllCosts, costLabelPowerUp1, costLabelPowerUp2,
            costLabelPowerUp3, costLabelPowerUp4,
            costLabelPowerUp5;

    TextButton powerup1, powerup2, powerup3, powerup4, powerup5;

    public UpgradesUi(PiazzaPanicGame game) {


        root = new Table();
        root.setFillParent(true);

        table = new Table();
        root.add(table).width(700).height(700).center();
        root.debug();
        table.debug();

        table.setHeight(1000);
        table.setVisible(false);


        FontManager fontManager = new FontManager();

        hudLabelStyle = new Label.LabelStyle();
        hudLabelStyle.font = fontManager.getLabelFont();

        titleLabel = new Label("Upgrades Shop", hudLabelStyle);

        timerForPowerUp1 = 20; // proof of concept
        costForPowerUp1 = 12;

        // sets it font, format and value
        timerLabelPowerUp1 = new Label(String.format("%03d" + "s", timerForPowerUp1),
                hudLabelStyle);

        costLabelPowerUp1 = new Label(String.format("£" + "%03d", costForPowerUp1),
                hudLabelStyle);

        // creating all the different buttons and their relevant values and timers
        TextButton powerup1 = game
                .getButtonManager()
                .createTextButton("speedy mover", ButtonManager.ButtonColour.BLUE);
        powerup1.sizeBy(1f);

        powerup1.addListener(
                new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        // do things
                    }
                });

        TextButton powerup2 = game
                .getButtonManager()
                .createTextButton("faster cooking", ButtonManager.ButtonColour.BLUE);
        powerup2.sizeBy(1f);

        powerup2.addListener(
                new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        // do things
                    }
                });

        TextButton powerup3 = game
                .getButtonManager()
                .createTextButton("cooking unfailable", ButtonManager.ButtonColour.BLUE);
        powerup3.sizeBy(1f);

        powerup3.addListener(
                new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        // do things
                    }
                });

        TextButton powerup4 = game
                .getButtonManager()
                .createTextButton("no rep loss", ButtonManager.ButtonColour.BLUE);
        powerup4.sizeBy(1f);

        powerup4.addListener(
                new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        // do things
                    }
                });

        TextButton powerup5 = game
                .getButtonManager()
                .createTextButton("more money", ButtonManager.ButtonColour.BLUE);
        powerup5.sizeBy(1f);

        powerup5.addListener(
                new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        // do things
                        update();
                    }
                });

        Value scale2 = Value.percentWidth(0.25f, table);
        Value scale = Value.percentWidth(0.05f, table);
        table.add(titleLabel).colspan(3);
        table.row();
        table.add(powerup1).width(scale2).height(scale).pad(5);
        table.add(timerLabelPowerUp1).width(scale).height(scale);
        table.add(costLabelPowerUp1).width(scale).height(scale);
        table.row();
        table.add(powerup2).width(scale2).height(scale).pad(5);
        table.row();
        table.add(powerup3).width(scale2).height(scale).pad(5);
        table.row();
        table.add(powerup4).width(scale2).height(scale).pad(5);
        table.row();
        table.add(powerup5).width(scale2).height(scale).pad(5);

        Pixmap bgPixmap = new Pixmap(100, 100, Pixmap.Format.RGB565);
        bgPixmap.setColor(1, 1, 0, 0.2f);
        bgPixmap.setBlending(Blending.SourceOver);
        bgPixmap.fillRectangle(10, 10, 80, 80);

        TextureRegionDrawable textureRegionDrawableBg = new TextureRegionDrawable(new Texture(bgPixmap));
        table.setBackground(textureRegionDrawableBg);

    }

    // foo being the boolean that makes the table visible or not
    public void visible(Boolean foo) {
        table.setVisible(foo);
    }

    public void addToStage(Stage uiStage) {
        uiStage.addActor(root);
    }

    public void update() {
        table.clear();
    }

}

package cs.eng1.piazzapanic.ui;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;

import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.PlayerState;

public class UpgradesUi {

    private static Table table;
    Table root, lowerTable;
    private LabelStyle hudLabelFont;
    private LabelStyle hudTitleFont;

    private int timerForPowerUp1, timerForPowerUp2, timerForPowerUp3,
            timerForPowerUp4, timerForPowerUp5, currentMoney;
        
    private String  timerForChefs;

    private Label titleLabel, labelForAllPowerups, moneyLabel;

    private Label labelForAllTimers, timerLabelPowerUp1, timerLabelPowerUp2,
            timerLabelPowerUp3, timerLabelPowerUp4,
            timerLabelPowerUp5, timerLabelForChefs;

    private int costForPowerUp1, costForPowerUp2,
            costForPowerUp3, costForPowerUp4,
            costForPowerUp5, costOfChef;

    private Label labelForAllCosts, costLabelPowerUp1, costLabelPowerUp2,
            costLabelPowerUp3, costLabelPowerUp4,
            costLabelPowerUp5, costLabelForChefs;

    TextButton powerup1, powerup2, powerup3, powerup4, powerup5, moreChefs;

    public UpgradesUi(PiazzaPanicGame game) {


        root = new Table();
        root.setFillParent(true);

        table = new Table();
        root.add(table).width(450).height(350).center();
        TextureRegionDrawable textureRegionDrawableBg =
            new TextureRegionDrawable(new Texture(Gdx.files.internal("backgroundimage.jpg")));
        table.setBackground(textureRegionDrawableBg);
        // root.debug();
        // table.debug();

        table.setHeight(1000);
        table.setVisible(false);

        currentMoney = 15; //random value

        //fonts used in the "shop"
        FontManager fontManager = new FontManager();

        hudLabelFont = new Label.LabelStyle();
        hudLabelFont.font = fontManager.getLabelFont();

        hudTitleFont = new Label.LabelStyle();
        hudTitleFont.font = fontManager.getTitleFont();


        titleLabel = new Label("Upgrades Shop", hudTitleFont);
        moneyLabel = new Label("£" + currentMoney, hudTitleFont);

        labelForAllTimers = new Label("Active For:", hudLabelFont);
        labelForAllPowerups= new Label("Powerups + Upgrades", hudLabelFont);
        labelForAllCosts = new Label("Cost:", hudLabelFont);

        
        
        timerForPowerUp1 = 20; // proof of concept
        costForPowerUp1 = 12;
        
        // sets it font, format and value
        timerLabelPowerUp1 = new Label(String.format(timerForPowerUp1 + " s"),
               hudLabelFont);
        
        costLabelPowerUp1 = new Label(String.format("£" +  costForPowerUp1),
                hudLabelFont);
        
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

        


        timerForPowerUp2 = 21; // proof of concept
        costForPowerUp2 = 13;

        // sets it font, format and value
        timerLabelPowerUp2 = new Label(String.format(timerForPowerUp2 + " s"),
                 hudLabelFont);
        
        costLabelPowerUp2 = new Label(String.format("£" +  costForPowerUp2),
                hudLabelFont);
        
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



        timerForPowerUp3 = 22; // proof of concept
        costForPowerUp3 = 14;
                
        // sets it font, format and value
        timerLabelPowerUp3 = new Label(String.format(timerForPowerUp3 + " s"),
                 hudLabelFont);
                
        costLabelPowerUp3 = new Label(String.format("£" +  costForPowerUp3),
                hudLabelFont);

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



        timerForPowerUp4 = 23; // proof of concept
        costForPowerUp4 = 15;
                
        // sets it font, format and value
        timerLabelPowerUp4 = new Label(String.format(timerForPowerUp4 + " s"),
                hudLabelFont);
        
        costLabelPowerUp4 = new Label(String.format("£" +  costForPowerUp4),
                hudLabelFont);

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

        
        timerForPowerUp5 = 24; // proof of concept
        costForPowerUp5 = 16;
                
        // sets it font, format and value
        timerLabelPowerUp5 = new Label(String.format(timerForPowerUp5 + " s"),
                hudLabelFont);
        
        costLabelPowerUp5 = new Label(String.format("£" +  costForPowerUp5),
                hudLabelFont);

        TextButton powerup5 = game
                .getButtonManager()
                .createTextButton("more money", ButtonManager.ButtonColour.BLUE);
        powerup5.sizeBy(1f);

        powerup5.addListener(
                new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        // do things
                    }
                });

        timerForChefs = "N/A"; // proof of concept
        costOfChef = 50;
        
        // sets it font, format and value
        timerLabelForChefs = new Label(String.format(timerForChefs),
                hudLabelFont);
                
        costLabelForChefs = new Label(String.format("£" +  costOfChef),
                hudLabelFont);


        TextButton moreChefs = game
                .getButtonManager()
                .createTextButton("extra chef", ButtonManager.ButtonColour.BLUE);
        moreChefs.sizeBy(1f);

        moreChefs.addListener(
                new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        // do things maybe?
                    }
                });

        lowerTable = new Table();
        lowerTable.setFillParent(true);
        lowerTable.bottom();

        PlayerState player = PlayerState.getInstance();
        Array<String> powerupsActive = player.getActivePowerups();
        if (powerupsActive != null){
        for (String x : powerupsActive) {
                lowerTable.add(x);
                lowerTable.row();
                }
        }



        // setting up the table in the 'Shop'
        Value scale2 = Value.percentWidth(0.3f, table);
        Value scale = Value.percentWidth(0.08f, table);
        Value scale1 = Value.percentWidth(0.15f, table);
        table.add(titleLabel).colspan(2);
        table.add(moneyLabel);
        table.row();
        table.add(labelForAllPowerups).width(scale2).height(scale).pad(5);
        table.add(labelForAllTimers).width(scale1).height(scale).pad(5).center();
        table.add(labelForAllCosts).width(scale1).height(scale).pad(5);
        table.row();
        table.add(powerup1).width(scale2).height(scale).pad(3);
        table.add(timerLabelPowerUp1).width(scale).height(scale).pad(3);
        table.add(costLabelPowerUp1).width(scale).height(scale).pad(3);
        table.row();
        table.add(powerup2).width(scale2).height(scale).pad(3);
        table.add(timerLabelPowerUp2).width(scale).height(scale).pad(3);
        table.add(costLabelPowerUp2).width(scale).height(scale).pad(3);
        table.row();
        table.add(powerup3).width(scale2).height(scale).pad(3);
        table.add(timerLabelPowerUp3).width(scale).height(scale).pad(3);
        table.add(costLabelPowerUp3).width(scale).height(scale).pad(3);
        table.row();
        table.add(powerup4).width(scale2).height(scale).pad(3);
        table.add(timerLabelPowerUp4).width(scale).height(scale).pad(3);
        table.add(costLabelPowerUp4).width(scale).height(scale).pad(3);
        table.row();
        table.add(powerup5).width(scale2).height(scale).pad(3);
        table.add(timerLabelPowerUp5).width(scale).height(scale).pad(3);
        table.add(costLabelPowerUp5).width(scale).height(scale).pad(3);
        table.row();
        table.add(moreChefs).width(scale2).height(scale).pad(3);
        table.add(timerLabelForChefs).width(scale).height(scale).pad(3);
        table.add(costLabelForChefs).width(scale).height(scale).pad(3);

        

    }

    public String whichPowerup(int value){
        String powerupName;
        if (value == 1) {
            powerupName = "Double Chef Speed";
        }else if(value == 2) {
            powerupName = "Double Prep Speed";
        }else if(value == 3) {
            powerupName = "No Fail Prep";
        }else if (value == 4){
            powerupName = "No Rep Loss";
        }else {
            powerupName = "More Money";
        }
        return powerupName;
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

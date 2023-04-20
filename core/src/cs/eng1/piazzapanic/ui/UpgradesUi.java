package cs.eng1.piazzapanic.ui;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import cs.eng1.piazzapanic.PiazzaPanicGame;

public class UpgradesUi{

    private static Table table;

    public UpgradesUi(PiazzaPanicGame game) {
        
        table = new Table();
        table.setFillParent(true);
        table.bottom();
        table.setVisible(false);

        // return button basicall just hides the whole table
        TextButton returnButton = game
            .getButtonManager()
            .createTextButton("return", ButtonManager.ButtonColour.BLUE);
        returnButton.sizeBy(1f);
        
        returnButton.addListener(
            new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    UpgradesUi.visible(false);
                }
            }
        );

        TextButton powerup1 = game
        .getButtonManager()
        .createTextButton("speedy mover", ButtonManager.ButtonColour.BLUE);
        powerup1.sizeBy(1f);
    
        powerup1.addListener(
            new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    // do things
                }
            }
        );

        TextButton powerup2 = game
        .getButtonManager()
        .createTextButton("faster cooking", ButtonManager.ButtonColour.BLUE);
        powerup2.sizeBy(1f);

        powerup2.addListener(
            new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    // do things
                }
            }
        );

        TextButton powerup3 = game
        .getButtonManager()
        .createTextButton("cooking unfailable", ButtonManager.ButtonColour.BLUE);
        powerup3.sizeBy(1f);

        powerup3.addListener(
            new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    // do things
                }
            }
        );

        TextButton powerup4 = game
        .getButtonManager()
        .createTextButton("no rep loss", ButtonManager.ButtonColour.BLUE);
        powerup4.sizeBy(1f);

        powerup4.addListener(
            new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    //do things
                }
            }
        );

        TextButton powerup5 = game
        .getButtonManager()
        .createTextButton("more money", ButtonManager.ButtonColour.BLUE);
        powerup5.sizeBy(1f);

        powerup5.addListener(
            new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    // do things
                }
            }
        );  
        




        Value scale2 = Value.percentWidth(0.12f, table);
        Value scale = Value.percentWidth(0.03f, table);
        table.add(returnButton).width(scale2).height(scale);
        table.add(powerup1).width(scale2).height(scale).pad(1);
        table.add(powerup2).width(scale2).height(scale).pad(1);
        table.add(powerup3).width(scale2).height(scale);
        table.add(powerup4).width(scale2).height(scale);
        table.add(powerup5).width(scale2).height(scale);
    
    }
    // foo being the boolean that makes the table visible or not
    public static void visible(Boolean foo){
        table.setVisible(foo);
    }

    public void addToStage(Stage uiStage) {
        uiStage.addActor(table);
    }

}

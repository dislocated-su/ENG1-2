package cs.eng1.piazzapanic.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import cs.eng1.piazzapanic.PiazzaPanicGame;

public class UpgradesUi{

    private final Table table;
    private int counter = 0;

    public UpgradesUi(PiazzaPanicGame game) {
        
        table = new Table();
        table.setFillParent(true);
        table.left();

        TextButton startButton = game
            .getButtonManager()
            .createTextButton("Upgrades", ButtonManager.ButtonColour.BLUE);
        startButton.sizeBy(3f);
        
        startButton.addListener(
            new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    counter += 1;
                    if (counter % 2 == 1){
                        table.setVisible(true);
                    }
                    else {
                        table.setVisible(false);
                    }
                }
            }
        );
        

    }
}

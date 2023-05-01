package cs.eng1.piazzapanic.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class UIStopwatch extends Label {

    private float totalTime = 0;
    private boolean isRunning = false;

    public UIStopwatch(Label.LabelStyle labelStyle) {
        super("0:00", labelStyle);
    }

    public void reset() {
        stop();
        totalTime = 0;
        setText("0:00");
    }

    public void start() {
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    public void setTime(float time) {
        totalTime = time;
        updateTimer();
    }

    public float getTime() {
        return totalTime;
    }

    @Override
    public void act(float delta) {
        if (isRunning) {
            totalTime += delta;
            updateTimer();
        }
    }

    public void updateTimer() {
        this.setText(getTimeString());
    }

    /**
     * Construct a string with the minutes and seconds correctly based on the total
     * number of seconds
     * that have passed.
     */
    public String getTimeString() {
        DecimalFormat df = new DecimalFormat("#");
        df.setRoundingMode((RoundingMode.FLOOR));
        if (totalTime >= 60) {
            int seconds = (int) (totalTime % 60);
            int minutes = (int) (totalTime / 60);
            if (seconds < 10) {
                return minutes + ":0" + df.format(seconds);
            } else {
                return minutes + ":" + df.format(seconds);
            }
        } else {
            if (totalTime < 10) {
                return "0:0" + df.format(totalTime);
            } else {
                return "0:" + df.format(totalTime);
            }
        }
    }
}

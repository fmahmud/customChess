package chess.gui.objects;

import chess.config.ConfigMaster;
import chess.general.Loggable;

import javax.swing.*;

/**
 * Created by Fez on 11/20/14.
 */
public class DrawableTimer extends Loggable {
    private JPanel canvas;
    private JLabel lblTime, lblTitle;
    private boolean paused = true;
    private boolean ended = true;
    private int seconds, minutes;
    private int direction;

    // Negative one means no max time.
    // Max time in seconds
    private int maxTime;

    public DrawableTimer(String _name) {
        super("Timer "+_name);
        define(_name, -1);
    }

    public DrawableTimer(String _name, int _mt) {
        super("Timer "+_name);
        define(_name, _mt);
    }

    private void define(String n, int _mt) {
        maxTime = _mt;
        if(maxTime > 0) {
            seconds = maxTime % 60;
            minutes = maxTime / 60;
        } else {
            seconds = minutes = 0;
        }
        lblTime = new JLabel(getFormattedTime(minutes, seconds));
        lblTime.setFont(ConfigMaster.headerOneFont);
        lblTitle = new JLabel(n);
        lblTime.setAlignmentX(0.5f);
        lblTitle.setAlignmentX(0.5f);
        canvas = new JPanel();
        canvas.setLayout(new BoxLayout(canvas, BoxLayout.Y_AXIS));
        canvas.add(lblTime);
        canvas.add(lblTitle);
        direction = maxTime > 0 ? -1 : 1;
    }

    public JPanel getCanvas() {
        return canvas;
    }

    public static String getFormattedTime(int mins, int sec) {
        return String.format("%02d", mins) + ":"
                + String.format("%02d", sec);
    }

    public void setTitle(String s) {
        lblTitle.setText(s);
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getTotalSeconds() {
        return minutes * 60 + seconds;
    }

    private void updateLabel() {
        lblTime.setText(getFormattedTime(minutes, seconds));
    }

    public void click() {
        this.paused = !this.paused;
    }

    public void start() {
        ended = false;
        tick();
    }

    public void stop() {
        ended = true;
    }

    private void tick() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (!ended) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!paused) {
                        if (i % 10 == 0) {
                            seconds += direction;
                            if(seconds < 0 || seconds > 59) {
                                seconds = seconds % 60;
                                minutes += direction;
                            }
                            updateLabel();
                        }
                        ++i;
                    }
                }
            }
        });
        thread.start();
    }
}

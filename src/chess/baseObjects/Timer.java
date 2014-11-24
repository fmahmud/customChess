package chess.baseObjects;

import chess.general.Loggable;

import javax.swing.*;

/**
 * Created by Fez on 11/20/14.
 */
public class Timer extends Loggable {
    private String timerLabel;
    private JLabel lblTimeFrame;
    private boolean paused = true;
    private boolean ended = true;
    private long startTime = 0l;
    private int seconds, minutes;

    public Timer(String _name, JLabel _theLabel) {
        super("Timer");
        timerLabel = _name;
        lblTimeFrame = _theLabel;
        seconds = minutes = 0;
    }

    private void updateLabel() {
        String strMins = String.format("%02d", minutes);
        String strSeconds = String.format("%02d", seconds);
        lblTimeFrame.setText(strMins+":"+strSeconds);
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public void start() {
        ended = false;
        paused = false;
        tick();
    }

    public void stop() {
        ended = true;
    }

    private void tick() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!ended) {
                    if(!paused) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ++seconds;
                        if(seconds >= 60) {
                            seconds = 0;
                            minutes++;
                        }
                        updateLabel();
                    }
                }
            }
        });
        thread.start();
    }
}

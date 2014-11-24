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
    private int seconds, minutes;

    public Timer(String _name, JLabel _theLabel) {
        super("Timer");
        timerLabel = _name;
        lblTimeFrame = _theLabel;
        seconds = minutes = 0;
    }

    public int getTotalSeconds() {
        return minutes * 60 + seconds;
    }

    private void updateLabel() {
        String strMins = String.format("%02d", minutes);
        String strSeconds = String.format("%02d", seconds);
        lblTimeFrame.setText(strMins+":"+strSeconds);
    }

    public void click() {
        logLine("Switching paused", 0);
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
                while(!ended) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(!paused) {
                        if(i % 10 == 0) {
                            ++seconds;
                            if (seconds >= 60) {
                                seconds = 0;
                                minutes++;
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

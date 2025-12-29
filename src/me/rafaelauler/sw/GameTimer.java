package me.rafaelauler.sw;

public class GameTimer {

    private int time;

    public GameTimer(int start) {
        this.time = start;
    }

    public boolean tick() {
        return --time <= 0;
    }

    public int getTime() {
        return time;
    }

    public void reset(int value) {
        time = value;
    }
}

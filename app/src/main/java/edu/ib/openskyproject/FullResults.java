package edu.ib.openskyproject;

import java.util.ArrayList;

public class FullResults {

    protected int time;
    protected ArrayList<ArrayList> states;

    public FullResults(int time, ArrayList<ArrayList> states) {
        this.time = time;
        this.states = states;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public ArrayList<ArrayList> getStates() {
        return states;
    }

    public void setStates(ArrayList<ArrayList> states) {
        this.states = states;
    }

    @Override
    public String toString() {
        return "FullResults{" +
                "time=" + time +
                ", states=" + states +
                '}';
    }
}

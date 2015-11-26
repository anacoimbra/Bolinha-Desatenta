package br.android.bolinhadesatenta.app.robot;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.ArrayList;

/**
 * Created by massa on 21/11/15.
 */
public class Game extends Observable {

    public static int COMMAND_LEFT = 0;
    public static int COMMAND_RIGHT= 1;
    public static int COMMAND_UP = 2;
    public static int COMMAND_DOWN = 3;

    public static int COMMAND_PINK = 4;
    public static int COMMAND_ORANGE = 5;
    public static int COMMAND_BLACK = 6;
    public static int COMMAND_GREEN = 7;
    public static int COMMAND_BLUE = 8;
    public static int COMMAND_RED = 9;

    public static int DIFFICULTY_NORMAL = 3;

    private static int MAX_COMMANDS = 9;
    private static int MAX_DIFFICULTY = 10;

    private int turn;
    private ArrayList<Integer> execution;
    private ArrayList<Integer> executedCommands;
    private int difficulty;

    public Game(int difficulty, Observer observer) {
        this.turn = 0;
        this.difficulty = difficulty;
        this.addObserver(observer);
    }

    private int calculateMovements() {
        return turn / difficulty + difficulty;
    }

    public void newTurn() {
        execution = new ArrayList<Integer>();
        executedCommands = new ArrayList<Integer>();

        Random r = new Random();
        int command;

        for ( int i = 0; i < calculateMovements(); i+=1 ) {
            command = r.nextInt(MAX_COMMANDS);
            execution.add(command);
        }
    }


    public void addUserCommand(Integer command) {
        this.executedCommands.add(command);
        if ( calculateMovements() == executedCommands.size()) {
            checkScore();
        }
    }

    private void checkScore() {
        int correct = 0,
            score = 0;
        Log.d("Game",execution.size() + "-" + executedCommands.size());
        for( int i = 0; i < execution.size(); i+=1) {
            if ( execution.get(i) == executedCommands.get(i) ) {
                correct+=1;
            }
        }

        if ( correct/ execution.size() > 1-(MAX_DIFFICULTY-difficulty)/10 ) {
            score = correct * 10;
        }

        Log.d("Game", String.valueOf(score));
        setChanged();
        notifyObservers(score);
    }

    public ArrayList<Integer> getExecution() {
        return this.execution;
    }
}

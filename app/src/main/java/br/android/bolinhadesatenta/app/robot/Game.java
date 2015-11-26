package br.android.bolinhadesatenta.app.robot;

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
            if (command == COMMAND_LEFT) {
                execution.add(COMMAND_LEFT);
            }
            else if (command == COMMAND_RIGHT) {
                execution.add(COMMAND_RIGHT);
            }
            else if (command == COMMAND_UP) {
                execution.add(COMMAND_UP);
            }
            else if (command == COMMAND_DOWN) {
                execution.add(COMMAND_DOWN);
            }
            else if (command == COMMAND_PINK) {
                execution.add(COMMAND_PINK);
            }
            else if (command == COMMAND_ORANGE) {
                execution.add(COMMAND_ORANGE);;
            }
            else if (command == COMMAND_BLACK) {
                execution.add(COMMAND_BLACK);
            }
            else if (command == COMMAND_GREEN) {
                execution.add(COMMAND_GREEN);
            }
            else if (command == COMMAND_BLUE) {
                execution.add(COMMAND_BLUE);
            }
            else if (command == COMMAND_RED) {
                execution.add(COMMAND_RED);
            }
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
        for( int i = 0; i < execution.size(); i+=1) {
            if ( execution.get(i) == executedCommands.get(i) ) {
                correct+=1;
            }
        }

        if ( correct/ execution.size() > 1-(MAX_DIFFICULTY-difficulty)/10 ) {
            score = correct * 10;
        }
        notifyObservers(score);
    }

    public ArrayList<Integer> getExecution() {
        return this.execution;
    }
}

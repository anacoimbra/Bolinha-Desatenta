package br.android.bolinhadesatenta.app.robot;

import android.app.Activity;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.ArrayList;

/**
 * Created by massa on 21/11/15.
 */
public class Game extends Observable {

    public static int COMMAND_LEFT = 1;
    public static int COMMAND_RIGHT= 2;
    public static int COMMAND_UP = 3;
    public static int COMMAND_DOWN = 4;

    public static int COMMAND_PINK = 5;
    public static int COMMAND_YELLOW = 6;
    public static int COMMAND_BLACK = 7;
    public static int COMMAND_GREEN = 8;
    public static int COMMAND_BLUE = 9;
    public static int COMMAND_RED = 10;

    public static int DIFFICULTY_NORMAL = 3;

    private static int MAX_COMMANDS;

    private int turn;
    private ArrayList<Integer> executedCommands;
    private ArrayList<Integer> userCommands;
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
        executedCommands = new ArrayList<Integer>();
        Random r = new Random();
        int command;

        for ( int i = 1; i < calculateMovements(); i+=1 ) {
            command = r.nextInt((MAX_COMMANDS * 1000) % MAX_COMMANDS);
            executedCommands.add(command);
            if (command == COMMAND_LEFT) {
                executedCommands.add(COMMAND_LEFT);
            }
            else if (command == COMMAND_RIGHT) {
                executedCommands.add(COMMAND_RIGHT);
            }
            else if (command == COMMAND_UP) {
                executedCommands.add(COMMAND_UP);
            }
            else if (command == COMMAND_DOWN) {
                executedCommands.add(COMMAND_DOWN);
            }
            else if (command == COMMAND_PINK) {
                executedCommands.add(COMMAND_PINK);
            }
            else if (command == COMMAND_YELLOW) {
                executedCommands.add(COMMAND_YELLOW);;
            }
            else if (command == COMMAND_BLACK) {
                executedCommands.add(COMMAND_BLACK);
            }
            else if (command == COMMAND_GREEN) {
                executedCommands.add(COMMAND_GREEN);
            }
            else if (command == COMMAND_BLUE) {
                executedCommands.add(COMMAND_BLUE);
            }
            else if (command == COMMAND_RED) {
                executedCommands.add(COMMAND_RED);
            }
        }
    }

    public void addUserCommand(Integer command) {
        this.userCommands.add(command);
        if ( calculateMovements() == userCommands.size()) {
            checkPoints();
        }
    }

    private void checkPoints() {
        // comparar comandos do usuário com os aleatórios
        // calcular pontuação
        // enviar para os observerst
        notifyObservers();
    }

    public ArrayList<Integer> getExecutedCommands() {
        return this.executedCommands;
    }
}

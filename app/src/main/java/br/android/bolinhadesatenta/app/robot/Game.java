package br.android.bolinhadesatenta.app.robot;

import android.app.Activity;
import android.widget.Toast;

import com.orbotix.ConvenienceRobot;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Stack;

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

    public boolean isActive;
    private ConvenienceRobot _connectedRobot;
    private int turn;
    private Activity context;
    private Stack<Integer> executedCommands;
    private int difficulty;

    public Game(ConvenienceRobot _connectedRobot, Activity context, int difficulty, Observer observer) {
        this._connectedRobot = _connectedRobot;
        this.context = context;
        this.turn = 0;
        this.difficulty = difficulty;
        this.addObserver(observer);
    }

    private void setActive() {
        this.isActive = true;
    }

    private void setInactive() {
        this.isActive = false;
    }

    private int calculateMovements() {
        return turn / difficulty + difficulty;
    }

    private void setInitialColor(){
        try {
            _connectedRobot.setLed(1f,1f,1f);
        }catch (Exception e){
            Toast.makeText(context, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }

    private void setYellow(){
        try {
            _connectedRobot.setLed(1f, 0.922f, 0.231f);
        }catch (Exception e){
            Toast.makeText(context, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }

    private void setPink(){
        try {
            _connectedRobot.setLed(1f, 0.804f, 0.824f);
        }catch (Exception e){
            Toast.makeText(context, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }

    private void setPurple(){
        try {
            _connectedRobot.setLed(0.612f, 0.153f, 0.69f);
        }catch (Exception e){
            Toast.makeText(context, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }

    private void setOrange(){
        try {
            _connectedRobot.setLed(1f, 0.596f, 0f);
        }catch (Exception e){
            Toast.makeText(context, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }

    public void newTurn() {
        executedCommands = new Stack<Integer>();
        setActive();
        Random r = new Random();
        int command;

        for ( int i = 1; i < calculateMovements(); i+=1 ) {
            command = r.nextInt((MAX_COMMANDS * 1000) % MAX_COMMANDS);
            executedCommands.add(command);
            if (command == COMMAND_LEFT) {
                countLeft();
            }
            else if (command == COMMAND_RIGHT) {
                countRight();
            }
            else if (command == COMMAND_UP) {
                countUp();
            }
            else if (command == COMMAND_DOWN) {
                countDown();
            }
            else if (command == COMMAND_PINK) {
                countPink();
            }
            else if (command == COMMAND_YELLOW) {
                countYellow();
            }
            else if (command == COMMAND_BLACK) {
                countBlack();
            }
            else if (command == COMMAND_GREEN) {
                countGreen();
            }
            else if (command == COMMAND_BLUE) {
                countBlue();
            }
            else if (command == COMMAND_RED) {
                countRed();
            }
        }


        this.setInactive();
    }

    private void countLeft() {
        moveLeft();
        executedCommands.add(COMMAND_LEFT);
    }
    private void countRight() {
        moveRight();
        executedCommands.add(COMMAND_RIGHT);
    }
    private void countUp() {
        moveUp();
        executedCommands.add(COMMAND_UP);
    }
    private void countDown() {
        moveDown();
        executedCommands.add(COMMAND_DOWN);
    }
    private void countPink() {
        paintPink();
        executedCommands.add(COMMAND_PINK);
    }
    private void countYellow() {
        paintYellow();
        executedCommands.add(COMMAND_YELLOW);
    }
    private void countBlack() {
        paintBlack();
        executedCommands.add(COMMAND_BLACK);
    }
    private void countGreen() {
        paintGreen();
        executedCommands.add(COMMAND_GREEN);
    }
    private void countBlue() {
        paintBlue();
        executedCommands.add(COMMAND_BLUE);
    }
    private void countRed() {
        paintRed();
        executedCommands.add(COMMAND_RED);
    }

    private void paintPink() {

    }
    private void paintYellow() {

    }
    private void paintBlack() {

    }
    private void paintGreen() {

    }
    private void paintBlue() {

    }
    private void paintRed() {

    }

    public void moveUp(){
        try {
            _connectedRobot.drive(0, 2);
            try {
                wait(500);
            } catch (Exception ignore) {
            }
            _connectedRobot.stop();
        }catch (Exception e){
            Toast.makeText(context, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }

    public void moveRight(){
        try{
            _connectedRobot.drive(90, 2);
            try {
                wait(500);
            }catch (Exception ignore){}
            _connectedRobot.stop();
        }catch (Exception e){
            Toast.makeText(context, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }

    public void moveLeft(){
        try {
            _connectedRobot.drive(270, 2);
            try {
                wait(500);
            } catch (Exception ignore) {
            }
            _connectedRobot.stop();
        }catch (Exception e){
            Toast.makeText(context, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }

    public void moveDown(){
        try {
            _connectedRobot.drive(180, 2);
            try {
                wait(500);
            } catch (Exception ignore) {
            }
            _connectedRobot.stop();
        }catch (Exception e){
            Toast.makeText(context, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }
}

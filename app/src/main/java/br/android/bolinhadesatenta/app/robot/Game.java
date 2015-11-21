package br.android.bolinhadesatenta.app.robot;

import android.app.Activity;
import android.widget.Toast;

import com.orbotix.ConvenienceRobot;

import java.util.Stack;

/**
 * Created by massa on 21/11/15.
 */
public class Game {

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

    public boolean isActive;
    private ConvenienceRobot _connectedRobot;
    private int turn;
    private Activity context;
    private Stack<int> executedCommands;

    public Game(ConvenienceRobot _connectedRobot, Activity context) {
        this._connectedRobot = _connectedRobot;
        this.context = context;
        this.turn = 0;
    }

    private void setActive() {
        this.isActive = true;
    }

    private void setInactive() {
        this.isActive = false;
    }

    public void newTurn() {
        this.setActive();
        // colocar aleatóriamente comands na pilha executedCommands, para ser comparada depois com a
        //entrada do usuáriog

        this.setInactive();
    }

    public void goUp(){
        try {
            _connectedRobot.drive(0, 2);
            try {
                wait(500);
            } catch (Exception ignore) {
            }
            _connectedRobot.stop();
        }catch (Exception e){
            Toast.makeText(context, "Bolinha não conectada", Toast.LENGTH_LONG).show();
        }
    }

    public void goRight(){
        try{
            _connectedRobot.drive(90, 2);
            try {
                wait(500);
            }catch (Exception ignore){}
            _connectedRobot.stop();
        }catch (Exception e){
            Toast.makeText(context, "Bolinha não conectada", Toast.LENGTH_LONG).show();
        }
    }

    public void goLeft(){
        try {
            _connectedRobot.drive(270, 2);
            try {
                wait(500);
            } catch (Exception ignore) {
            }
            _connectedRobot.stop();
        }catch (Exception e){
            Toast.makeText(context, "Bolinha não conectada", Toast.LENGTH_LONG).show();
        }
    }

    public void goDown(){
        try {
            _connectedRobot.drive(180, 2);
            try {
                wait(500);
            } catch (Exception ignore) {
            }
            _connectedRobot.stop();
        }catch (Exception e){
        }
    }
}

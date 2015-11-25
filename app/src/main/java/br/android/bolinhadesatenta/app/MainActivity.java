package br.android.bolinhadesatenta.app;

import br.android.bolinhadesatenta.app.robot.Game;
import br.android.bolinhadesatenta.app.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.orbotix.ConvenienceRobot;
import com.orbotix.Sphero;
import com.orbotix.classic.DiscoveryAgentClassic;
import com.orbotix.common.DiscoveryAgent;
import com.orbotix.common.DiscoveryAgentEventListener;
import com.orbotix.common.DiscoveryException;
import com.orbotix.common.Robot;
import com.orbotix.common.RobotChangedStateListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MainActivity extends Activity implements DiscoveryAgentEventListener,
        RobotChangedStateListener, Observer {

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    private DiscoveryAgent _currentDiscoveryAgent;

    private ConvenienceRobot _connectedRobot;

    private View mDecorView;

    private Game game;

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDecorView = getWindow().getDecorView();
        this.hideSystemUI();

        setContentView(R.layout.activity_main);

//        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//                            // If the ViewPropertyAnimator API is available
//                            // (Honeycomb MR2 and later), use it to animate the
//                            // in-layout UI controls at the bottom of the
//                            // screen.
//                            if (mControlsHeight == 0) {
//                                mControlsHeight = controlsView.getHeight();
//                            }
//                            if (mShortAnimTime == 0) {
//                                mShortAnimTime = getResources().getInteger(
//                                        android.R.integer.config_shortAnimTime);
//                            }
//                            controlsView.animate()
//                                    .translationY(visible ? 0 : mControlsHeight)
//                                    .setDuration(mShortAnimTime);
//                        } else {
//                            // If the ViewPropertyAnimator APIs aren't
//                            // available, simply show or hide the in-layout UI
//                            // controls.
//                            controlsView.setVisibility(View.GONE);
//                        }
//
//                        if (visible) {
//                            // Schedule a hide().
//                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
//                        }
                    }
                });

        disableButtons();

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSystemUiHider.hide();
            }
        });

        _currentDiscoveryAgent = DiscoveryAgentClassic.getInstance();
        startDiscovery();
    }

    private void enableButtons() {
        this.findViewById(R.id.downBtn).setEnabled(true);
        this.findViewById(R.id.upBtn).setEnabled(true);
        this.findViewById(R.id.leftBtn).setEnabled(true);
        this.findViewById(R.id.rightBtn).setEnabled(true);
        this.findViewById(R.id.pinkBtn).setEnabled(true);
        this.findViewById(R.id.blueBtn).setEnabled(true);
        this.findViewById(R.id.orangeBtn).setEnabled(true);
        this.findViewById(R.id.greenBtn).setEnabled(true);
        this.findViewById(R.id.blackBtn).setEnabled(true);
        this.findViewById(R.id.redBtn).setEnabled(true);
    }


    private void disableButtons() {
        this.findViewById(R.id.downBtn).setEnabled(false);
        this.findViewById(R.id.upBtn).setEnabled(false);
        this.findViewById(R.id.leftBtn).setEnabled(false);
        this.findViewById(R.id.rightBtn).setEnabled(false);
        this.findViewById(R.id.pinkBtn).setEnabled(false);
        this.findViewById(R.id.blueBtn).setEnabled(false);
        this.findViewById(R.id.orangeBtn).setEnabled(false);
        this.findViewById(R.id.greenBtn).setEnabled(false);
        this.findViewById(R.id.blackBtn).setEnabled(false);
        this.findViewById(R.id.redBtn).setEnabled(false);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void handleRobotsAvailable(List<Robot> list) {
        if (_currentDiscoveryAgent instanceof DiscoveryAgentClassic) {
            // If we are using the classic discovery agent, and therefore using Sphero, we'll just connect to the first
            // one available that we get. Note that "available" in classic means paired to the phone and turned on.
            _currentDiscoveryAgent.connect(list.get(0));
        }
    }

    @Override
    public void handleRobotChangedState(Robot robot, RobotChangedStateNotificationType robotChangedStateNotificationType) {
        switch (robotChangedStateNotificationType) {
            // A robot was connected, and is ready for you to send commands to it.
            case Online:
                // When a robot is connected, this is a good time to stop discovery. Discovery takes a lot of system
                // resources, and if left running, will cause your app to eat the user's battery up, and may cause
                // your application to run slowly. To do this, use DiscoveryAgent#stopDiscovery().
                _currentDiscoveryAgent.stopDiscovery();
                // It is also proper form to not allow yourself to re-register for the discovery listeners, so let's
                // unregister for the available notifications here using DiscoveryAgent#removeDiscoveryListener().
                _currentDiscoveryAgent.removeDiscoveryListener(this);
                // Don't forget to turn on UI elements

                // Depending on what was connected, you might want to create a wrapper that allows you to do some
                // common functionality related to the individual robots. You can always of course use the
                // Robot#sendCommand() method, but Ollie#drive() reads a bit better.
                _connectedRobot = new Sphero(robot);

                Log.d("OK", "Sphero conectado");

                _connectedRobot.setZeroHeading();

                startNewGame();

                break;
            case Disconnected:
                Log.d("DESCONECTADO", "Sphero foi desconectado");
                break;
            default:
                Log.v("ERRO", "Not handling state change notification: " + robotChangedStateNotificationType);
                break;
        }
    }

    private void newTurn() {
        // TODO resetar label com mensagem apropriada. depois dos comandos executados, trocar label novamente
        disableButtons(); // lock buttons
        setInitialColor();
        game.newTurn();
        ArrayList<Integer> commands = game.getExecutedCommands();

        for (Integer command : commands) {
            if (command == Game.COMMAND_LEFT) {
                game.addUserCommand(Game.COMMAND_LEFT);
                moveLeft();
            }
            else if (command == Game.COMMAND_RIGHT) {
                game.addUserCommand(Game.COMMAND_RIGHT);
                moveRight();
            }
            else if (command == Game.COMMAND_UP) {
                game.addUserCommand(Game.COMMAND_UP);
                moveUp();
            }
            else if (command == Game.COMMAND_DOWN) {
                game.addUserCommand(Game.COMMAND_DOWN);
                moveDown();
            }
            else if (command == Game.COMMAND_PINK) {
                game.addUserCommand(Game.COMMAND_PINK);
                // TODO set sphero led
            }
            else if (command == Game.COMMAND_YELLOW) {
                game.addUserCommand(Game.COMMAND_YELLOW);
                // TODO set sphero led
            }
            else if (command == Game.COMMAND_BLACK) {
                game.addUserCommand(Game.COMMAND_BLACK);
                // TODO set sphero led
            }
            else if (command == Game.COMMAND_GREEN) {
                game.addUserCommand(Game.COMMAND_GREEN);
                // TODO set sphero led
            }
            else if (command == Game.COMMAND_BLUE) {
                game.addUserCommand(Game.COMMAND_BLUE);
                // TODO set sphero led
            }
            else if (command == Game.COMMAND_RED) {
                game.addUserCommand(Game.COMMAND_RED);
                // TODO set sphero led
            }
        }

        enableButtons(); // unlock buttons
    }

    private void startNewGame() {
        TextView l = (TextView) findViewById(R.id.feedback_message);
        l.setTextColor(getResources().getColor(R.color.ok));
        l.setText(R.string.waiting_sphero);
        game = new Game(Game.DIFFICULTY_NORMAL, this);
        newTurn();
    }

    private void startDiscovery() {
        try {
            // You first need to set up so that the discovery agent will notify you when it finds robots.
            // To do this, you need to implement the DiscoveryAgentEventListener interface (or declare
            // it anonymously) and then register it on the discovery agent with DiscoveryAgent#addDiscoveryListener()
            _currentDiscoveryAgent.addDiscoveryListener(this);
            // Second, you need to make sure that you are notified when a robot changes state. To do this,
            // implement RobotChangedStateListener (or declare it anonymously) and use
            // DiscoveryAgent#addRobotStateListener()
            _currentDiscoveryAgent.addRobotStateListener(this);
            // Then to start looking for a Sphero, you use DiscoveryAgent#startDiscovery()
            // You do need to handle the discovery exception. This can occur in cases where the user has
            // Bluetooth off, or when the discovery cannot be started for some other reason.
            _currentDiscoveryAgent.startDiscovery(this);

            Log.d("ACHOU", "Encontrado sphero");
        } catch (DiscoveryException e) {
            Log.e("ERRO", "Could not start discovery. Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        // TODO recebe pontuação no object data e acrescenta no label de pontuação
        newTurn();
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
            Toast.makeText(this, "Sphero not connected", Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, "Sphero not connected", Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, "Sphero not connected", Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }

    private void setInitialColor(){
        try {
            _connectedRobot.setLed(1f,1f,1f);
        }catch (Exception e){
            Toast.makeText(this, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }

    private void setYellow(){
        try {
            _connectedRobot.setLed(1f, 0.922f, 0.231f);
        }catch (Exception e){
            Toast.makeText(this, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }

    private void setPink(){
        try {
            _connectedRobot.setLed(1f, 0.804f, 0.824f);
        }catch (Exception e){
            Toast.makeText(this, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }

    private void setPurple(){
        try {
            _connectedRobot.setLed(0.612f, 0.153f, 0.69f);
        }catch (Exception e){
            Toast.makeText(this, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }

    private void setOrange(){
        try {
            _connectedRobot.setLed(1f, 0.596f, 0f);
        }catch (Exception e){
            Toast.makeText(this, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }
}

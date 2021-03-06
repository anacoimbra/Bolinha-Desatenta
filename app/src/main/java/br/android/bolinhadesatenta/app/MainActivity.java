package br.android.bolinhadesatenta.app;

import br.android.bolinhadesatenta.app.robot.Game;
import br.android.bolinhadesatenta.app.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
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

    private ImageButton up, down, right, left, black, pink, blue, green, red, orange;

    private static int INITIAL_SCORE = 0;

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

        final View contentView = findViewById(R.id.fullscreen_content);

        up = (ImageButton)findViewById(R.id.upBtn);
        down = (ImageButton)findViewById(R.id.downBtn);
        right = (ImageButton)findViewById(R.id.rightBtn);
        left = (ImageButton)findViewById(R.id.leftBtn);
        pink = (ImageButton)findViewById(R.id.pinkBtn);
        black = (ImageButton)findViewById(R.id.blackBtn);
        green = (ImageButton)findViewById(R.id.greenBtn);
        red = (ImageButton)findViewById(R.id.redBtn);
        blue = (ImageButton)findViewById(R.id.blueBtn);
        orange = (ImageButton)findViewById(R.id.orangeBtn);

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.addUserCommand(Game.COMMAND_UP);
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.addUserCommand(Game.COMMAND_DOWN);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.addUserCommand(Game.COMMAND_RIGHT);
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.addUserCommand(Game.COMMAND_LEFT);
            }
        });

        pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.addUserCommand(Game.COMMAND_PINK);
            }
        });

        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.addUserCommand(Game.COMMAND_ORANGE);
            }
        });

        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.addUserCommand(Game.COMMAND_BLACK);
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.addUserCommand(Game.COMMAND_GREEN);
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.addUserCommand(Game.COMMAND_BLUE);
            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.addUserCommand(Game.COMMAND_RED);
            }
        });

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {

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
        down.setEnabled(true);
        up.setEnabled(true);
        left.setEnabled(true);
        right.setEnabled(true);
        pink.setEnabled(true);
        blue.setEnabled(true);
        orange.setEnabled(true);
        green.setEnabled(true);
        black.setEnabled(true);
        red.setEnabled(true);
    }


    private void disableButtons() {
        down.setEnabled(false);
        up.setEnabled(false);
        left.setEnabled(false);
        right.setEnabled(false);
        pink.setEnabled(false);
        blue.setEnabled(false);
        orange.setEnabled(false);
        green.setEnabled(false);
        black.setEnabled(false);
        red.setEnabled(false);
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

    private synchronized void newTurn() {
        // TODO resetar label com mensagem apropriada. depois dos comandos executados, trocar label novamente
        disableButtons(); // lock buttons
        setInitialColor();
        game.newTurn();

        ArrayList<Integer> commands = game.getExecution();

        for (Integer command : commands) {
            if (command == Game.COMMAND_LEFT) {
                moveLeft();
            }
            else if (command == Game.COMMAND_RIGHT) {
                moveRight();
            }
            else if (command == Game.COMMAND_UP) {
                moveUp();
            }
            else if (command == Game.COMMAND_DOWN) {
                moveDown();
            }
            else if (command == Game.COMMAND_PINK) {
                setPink();
            }
            else if (command == Game.COMMAND_ORANGE) {
                setOrange();
            }
            else if (command == Game.COMMAND_BLACK) {
                setBlack();
            }
            else if (command == Game.COMMAND_GREEN) {
                setGreen();
            }
            else if (command == Game.COMMAND_BLUE) {
                setBlue();
            }
            else if (command == Game.COMMAND_RED) {
                setRed();
            }

            try {
                wait(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            wait(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        enableButtons(); // unlock buttons
    }

    private void startNewGame() {
        TextView scoreTextView = (TextView) findViewById(R.id.scoreLbl);
        TextView l = (TextView) findViewById(R.id.feedback_message);
        l.setTextColor(getResources().getColor(R.color.ok));
        l.setText(R.string.waiting_sphero);
        scoreTextView.setText(R.string.inicial_score);
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
        Log.d("TESTE", data.toString());
        TextView scoreTextView = (TextView) findViewById(R.id.scoreLbl);
        int score = (Integer) data;


        if ( score != 0 ) {
            scoreTextView.setText(Integer.parseInt(scoreTextView.getText().toString()) + score);
            newTurn();
        } else {
            startNewGame();
        }
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

    private void setPink(){
        try {
            _connectedRobot.setLed(1f, 0.804f, 0.824f);
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

    private void setBlack(){
        try {
            _connectedRobot.setLed(0f, 0f, 0f);
        }catch (Exception e){
            Toast.makeText(this, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }

    private void setRed(){
        try {
            _connectedRobot.setLed(1f, 0f, 0f);
        }catch (Exception e){
            Toast.makeText(this, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }

    private void setBlue(){
        try {
            _connectedRobot.setLed(0f, 0f, 1f);
        }catch (Exception e){
            Toast.makeText(this, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }

    private void setGreen(){
        try {
            _connectedRobot.setLed(0f, 1f, 0f);
        }catch (Exception e){
            Toast.makeText(this, "Sphero not connected", Toast.LENGTH_LONG).show();
        }
    }
}

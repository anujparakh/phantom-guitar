package com.example.anuj.phantomguitar;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import nl.littlerobots.bean.message.Acceleration;
import nl.littlerobots.bean.message.Callback;

/**
 * Created by Anuj on 18/08/15.
 */
public class OnTheGoActivity extends ActionBarActivity
{
    Timer timer;
    TimerTask timerTask;
    private static final String tag = "awesome";

    // array to hold old acceleration readings
    ArrayList <Acceleration> oldReadings;

    AudioManager amanager;

    // true if testing phase
    boolean testing = false;

    // true for the first time acceleration is read from the bean

    // the last acceleration reading
    Acceleration lastAcceleration;

    // constatns for max rows and cols of the buttons of chords
    final int MAX_ROWS = 4;
    final int MAX_COLS = 2;
    final int MAX_VOLUME = 15;

    // constant for max old readings
    final int MAX_OLD = 5;

    int selectedRow;
    int selectedCol;

    // an array of custom type chord buttons to hold each chord button
    ChordButton chordButtons[][];

    // the holder for my bean
    BeanHolder holder;

    // function to update chords according to whether they are selected or not

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onthego);

        amanager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (!testing)
        {
            // instantiate the array of chordButtons with empty elements
            chordButtons = new ChordButton[4][2];
            for (int row = 0; row < MAX_ROWS; ++row)
            {
                for (int col = 0; col < MAX_COLS; ++col)
                {
                    chordButtons[row][col] = new ChordButton();
                }
            }

            oldReadings = new ArrayList <Acceleration> ();

            // assign the chordButtons their chords
            populateChords();

            // assign the textViews on screen to the chordButtons
            populateChordButtons();

            //select C as default..
            chordButtons[0][0].isSelected();
            upDateChordButtons();

            // set all touch and click listeners for the chordButtons
            setAllListeners();

            // get the bean holder from the instance of the application
            holder = (BeanHolder) getApplication();

            // get the first 5 acceleration readings per 100 ms..
            // make a callback for reading acceleration the first 5 times
            Callback <Acceleration> firstCall = new Callback<Acceleration>()
            {
                @Override
                public void onResult(Acceleration acceleration)
                {
                   oldReadings.add(acceleration);
                }
            };


            for (int index = 0; index < MAX_OLD; ++index)
            {
                holder.getBean().readAcceleration (firstCall);
            }

            startTimer();

            amanager.setStreamVolume(AudioManager.STREAM_MUSIC, MAX_VOLUME, 0);

        }
        else
        {

        }

    }




    void upDateChordButtons()
    {
        // nested for loop to index through all chordButtons
        for (int row = 0; row < MAX_ROWS; ++row)
        {
            for (int col = 0; col < MAX_COLS; ++col)
            {
                // if the button is selected, highlight it
                if (chordButtons [row] [col].isSelected())
                {
                    chordButtons [row] [col].getButtonView().setBackgroundResource(R.drawable.chord_filled);
                    chordButtons [row] [col].getButtonView().setTextColor(Color.parseColor("#00aaaa"));

                }
                // otherwise make sure it is dehighlighted
                else
                {
                    chordButtons [row] [col].getButtonView().setBackgroundResource(R.drawable.chord_outline);
                    chordButtons [row] [col].getButtonView().setTextColor(Color.parseColor("#aaff69"));
                }
            }
        }
    }


    // method to calculate intensity/volume of the chord


    void playTheChord (int intensity)
    {
        if (intensity != 0)
        {

            //amanager.setStreamVolume(AudioManager.STREAM_MUSIC, intensity, 0);

            chordButtons[selectedRow][selectedCol].play(getApplicationContext());

        }
    }

    int calculateChord ()
    {
        int intensity = 0;

        if  (Math.abs ( oldReadings.get(oldReadings.size() - 1).z() - oldReadings.get(oldReadings.size() - 2).z() ) >= 1)
        {
            intensity = 15;
        }

//        int lastIndex = oldReadings.size() - 1;
//        if (oldReadings.get(lastIndex).z() - oldReadings.get (lastIndex - 1).z() >= 1)
//        {
//            intensity = 15;
//        }
//
//        if (oldReadings.get(lastIndex).z() - oldReadings.get (lastIndex - 2).z() >= 1)
//        {
//            intensity = 12;
//        }
//
//        if (oldReadings.get(lastIndex).z() - oldReadings.get (lastIndex - 3).z() >= 1)
//        {
//            intensity = 9;
//        }
//
//        if (oldReadings.get(lastIndex).z() - oldReadings.get (lastIndex - 4).z() >= 1)
//        {
//            intensity = 6;
//        }

        return intensity;
    }


    // ALL INITIALIZATION METHODS ARE BELOW..

    // start the timer!
    void startTimer ()
    {
        timer = new Timer ();

        makeMyTimerTask();
        // schedule the timer to start in 100 ms and repeat every 200 ms
        timer.scheduleAtFixedRate(timerTask, 50, 200);
    }

    void makeMyTimerTask ()
    {

        final Callback<Acceleration> accCallback = new Callback<Acceleration>() {
            @Override
            public void onResult(Acceleration acceleration)
            {
                oldReadings.add(acceleration);
                playTheChord(calculateChord());
                Log.d(tag, "Time = " + System.currentTimeMillis());
            }
        };
        timerTask = new TimerTask()
        {
            public void run()
            {
                //Log.d(tag, "reached run");
                holder.getBean().readAcceleration(accCallback);
            }
        };

    }


    // set all touch and click listeners for the chordButtons
    void setAllListeners()
    {
        // onTouchListener which is common for all chordButtons
        final View.OnTouchListener onTouchListenerForChordButtons = new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View chordB, MotionEvent event)
            {
                // cast the view as a textView to be used
                TextView chordButton = (TextView) chordB;
                // if touch is down..
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    // highlight the chord
                    chordButton.setBackgroundResource(R.drawable.chord_filled);
                    chordButton.setTextColor(Color.parseColor("#00aaaa"));
                }
                // if touch is up..
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    // de highlight the chord
                    chordButton.setBackgroundResource(R.drawable.chord_outline);
                    chordButton.setTextColor(Color.parseColor("#aaff69"));
                }

                return false;
            }

        };

        // common onClickListener for all chord buttons
        final View.OnClickListener onClickListenerForChordButtons = new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // cast the view as a textView to be used
                TextView selected = (TextView) v;

                // check for all chordButtons using nested for loop
                for (int row = 0; row < MAX_ROWS; ++row)
                {
                    for (int col = 0; col < MAX_COLS; ++col)
                    {
                        // if a chord button is already selected
                        if (chordButtons [row] [col].isSelected())
                        {
                            // if the chord chosen is already selected, theres nothing to be done.. so break
                            if (chordButtons[row][col].getButtonView().getId() == selected.getId()) {
                                chordButtons[row][col].hasBeenSelected (getApplicationContext());
                                break;
                            } else
                            {
                                // otherwise deselect it
                                chordButtons[row][col].deSelect();
                            }
                        }

                        // check if the chordButton indexed is the one the user selected by comparing ids
                        if (chordButtons [row] [col].getButtonView().getId() == selected.getId())
                        {
                            // set as selected
                            chordButtons [row] [col].hasBeenSelected (getApplicationContext());
                            selectedRow = row;
                            selectedCol = col;
                        }
                    }
                }

                // update the screen according to changes
                upDateChordButtons();

            }
        };

        // nested for loop to index all chordButtons
        for (int row = 0; row < MAX_ROWS; ++row)
        {
            for (int col = 0; col < MAX_COLS; ++col)
            {
                // set both onTouch and onClick Listeners to all of the chordButtons
                chordButtons[row][col].getButtonView().setOnTouchListener(onTouchListenerForChordButtons);
                chordButtons[row][col].getButtonView().setOnClickListener(onClickListenerForChordButtons);
            }
        }
    }

    // function to assign values to the chordButtons array from the textViews on screen
    void populateChordButtons()
    {
        // ORDER IS SAME AS ON SCREEN..
        chordButtons[0][0].setButtonView((TextView) findViewById(R.id.c0_0));
        chordButtons[0][1].setButtonView((TextView) findViewById(R.id.c0_1));

        chordButtons[1][0].setButtonView ((TextView) findViewById(R.id.c1_0));
        chordButtons[1][1].setButtonView ((TextView) findViewById(R.id.c1_1));

        chordButtons[2][0].setButtonView ((TextView) findViewById(R.id.c2_0));
        chordButtons[2][1].setButtonView ((TextView) findViewById(R.id.c2_1));

        chordButtons[3][0].setButtonView ((TextView) findViewById(R.id.c3_0));
        chordButtons[3][1].setButtonView ((TextView) findViewById(R.id.c3_1));


    }


    // function to assign predefined values of chords to the chordButtons (temporary)
    void populateChords()
    {
        // ORDER IS SAME AS ON SCREEN..
        chordButtons[0][0].setChord(getApplicationContext(), "C");
        chordButtons[0][1].setChord(getApplicationContext(), "G");

        chordButtons[1][0].setChord(getApplicationContext(), "F");
        chordButtons[1][1].setChord(getApplicationContext(), "D");

        chordButtons[2][0].setChord(getApplicationContext(), "E");
        chordButtons[2][1].setChord(getApplicationContext(), "Em");

        chordButtons[3][0].setChord(getApplicationContext(), "A");
        chordButtons[3][1].setChord(getApplicationContext(), "Am");

    }



}
package com.example.anuj.phantomguitar;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.TextView;

/**
 * Created by Anuj on 18/08/15.
 */
// custom class for storing a chordButton
public class ChordButton
{
    // 3 elements :
    // a textView to store the view on screen
    private TextView buttonView;
    // a boolean to store whether the button is selected
    private boolean selected = false;
    // and a string to store the chord the button represents
    private String chord;
    // an array of Media Players to store the players that wil play the chord
    private MediaPlayer players [];
    // an int to store the res id of the chord that is to be used
    private int chordResId;
    // an int to store the index of the player being used from the pool of players
    private int playerIndex;
    // a const int to store the number of players
    private final int NO_OF_PLAYERS = 3;

    // the rest are access functions for all the elements i.e. getters and setters
    public ChordButton ()
    {
        selected = false;
        chord = new String ();
        players = new MediaPlayer [NO_OF_PLAYERS];
        playerIndex = 0;
    }


    public void setButtonView (TextView newButtonView)
    {
        buttonView = newButtonView;
    }


    public TextView getButtonView ()
    {
        return buttonView;
    }

    public void hasBeenSelected (Context context)
    {
        selected = true;
        // create the mediaPlayer from the chord resource audio file
        for (int i = 0; i < players.length; ++i)
        {
            players [i] = MediaPlayer.create(context, chordResId);
        }
    }

    public void play (Context context)
    {
        players [playerIndex].start();
        ++playerIndex;
        if (playerIndex >= NO_OF_PLAYERS)
        {
            playerIndex = 0;
        }
    }

    public void stop ()
    {

    }

    public void deSelect ()
    {
        selected = false;

        // if the mediaPlayer is already playing, stop it then release it
//        player.stop();
//        player.release();

    }

    public boolean isSelected ()
    {
        return selected;
    }

    public String getChord ()
    {
        return chord;
    }

    public void setChord (Context context, String newChord)
    {
        chord = newChord;
      //  buttonView.setText(chord);
        if (chord.equals("C"))
        {
           // player = MediaPlayer.create(context, R.raw.c_chord);
            chordResId = R.raw.c_chord;
        }
        else if (chord.equals("F"))
        {
//            player = MediaPlayer.create(context, R.raw.f_chord);
            chordResId = R.raw.f_chord;

        }
        else if (chord.equals("G"))
        {
        //    player = MediaPlayer.create(context, R.raw.g_chord);
            chordResId = R.raw.g_chord;

        }
        else if (chord.equals("D"))
        {
        //    player = MediaPlayer.create(context, R.raw.d_chord);
            chordResId = R.raw.d_chord;
        }
        else if (chord.equals("A"))
        {
           // player = MediaPlayer.create(context, R.raw.a_chord);
            chordResId = R.raw.a_chord;
        }
        else if (chord.equals("Am"))
        {
        //    player = MediaPlayer.create(context, R.raw.am_chord);
            chordResId = R.raw.am_chord;
        }
        else if (chord.equals("E"))
        {
            //player = MediaPlayer.create(context, R.raw.e_chord);
            chordResId = R.raw.e_chord;
        }
        else if (chord.equals("Em"))
        {
            //player = MediaPlayer.create(context, R.raw.em_chord);
            chordResId = R.raw.em_chord;

        }
        else
        {
           // player = MediaPlayer.create(context, R.raw.c_chord);
            chordResId = R.raw.c_chord;

        }


    }

}

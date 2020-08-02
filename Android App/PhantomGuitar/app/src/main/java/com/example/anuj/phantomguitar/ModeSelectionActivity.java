package com.example.anuj.phantomguitar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Anuj on 16/08/15.
 */
public class ModeSelectionActivity extends ActionBarActivity
{
    TextView chord_otg;
    boolean checking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modeselection);

        chord_otg = (TextView) findViewById (R.id.chord_otg);

        chord_otg.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    chord_otg.setBackgroundResource(R.drawable.otg_filled);
                    chord_otg.setTextColor(Color.parseColor("#ffff00"));
                }
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    chord_otg.setBackgroundResource(R.drawable.otg_outline);
                    chord_otg.setTextColor(Color.parseColor("#aaff69"));
                }
                return false;
            }
        });

        chord_otg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ModeSelectionActivity.this, OnTheGoActivity.class);
                startActivity(intent);
            }
        });

    }


}

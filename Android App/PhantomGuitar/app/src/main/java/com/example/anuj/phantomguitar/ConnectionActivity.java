package com.example.anuj.phantomguitar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import nl.littlerobots.bean.Bean;
import nl.littlerobots.bean.BeanDiscoveryListener;
import nl.littlerobots.bean.BeanListener;
import nl.littlerobots.bean.BeanManager;


public class ConnectionActivity extends ActionBarActivity
{
    // for testing
    boolean checking = false;

    TextView display;
    TextView connect;
    ArrayList <Bean> beans;
    boolean beansFound;
    int beanNo;
    BeanHolder beanHolder;
    BeanListener beanListener;

    void upDateList ()
    {
        display.setText ("Bean(s) have been found! Touch to connect..");
        if (beans.size() >= 1)
        {
            connect.setAlpha (1.0f);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ConnectionActivity.this);
        ArrayList<String> names = new ArrayList <String>();

        for (Bean bean : beans)
        {
            names.add(bean.getDevice().getName());
        }

        final CharSequence[] cs = names.toArray(new CharSequence[beans.size()]);

        builder.setTitle("Choose your bean :").setItems(cs ,
                new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        beanNo = which;
                        beanHolder.setBean(beans.get(beanNo));
                        beanHolder.getBean().connect(getApplicationContext(), beanListener);
                    }

                });

        builder.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        if (checking)
        {
            Intent intent = new Intent(ConnectionActivity.this, ModeSelectionActivity.class);
            startActivity(intent);
        }
        beanHolder = (BeanHolder) getApplication ();
        beansFound = false; // no beans found originally
        beanNo = -1; // to show that there is no bean
        beans = new ArrayList <Bean> ();



        BeanDiscoveryListener discover = new BeanDiscoveryListener()
        {
            @Override
            public void onBeanDiscovered(Bean bean)
            {
                beansFound = true;
                beans.add(bean);
                upDateList();
            }

            @Override
            public void onDiscoveryComplete()
            {}
        };

        beanListener = new BeanListener()
        {
            @Override
            public void onConnected()
            {
                Intent intent = new Intent(ConnectionActivity.this, ModeSelectionActivity.class);
                startActivity(intent);
            }

            @Override
            public void onConnectionFailed()
            {
                Toast.makeText(getApplicationContext(), "Connection failed!!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDisconnected()
            {}

            @Override
            public void onSerialMessageReceived(byte[] bytes) {}

            @Override
            public void onScratchValueChanged(int i, byte[] bytes) {}
        };






        BeanManager.getInstance().startDiscovery(discover);

        display = (TextView) findViewById(R.id.display);
        connect = (TextView) findViewById(R.id.connect);
        connect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (beansFound) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        connect.setBackgroundResource(R.drawable.connect_filled);
                        connect.setTextColor(Color.parseColor("#ffff00"));
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        connect.setBackgroundResource(R.drawable.connect_outline);
                        connect.setTextColor(Color.parseColor("#aaff69"));
                    }
                }
                return false;
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beansFound) {
                    upDateList();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

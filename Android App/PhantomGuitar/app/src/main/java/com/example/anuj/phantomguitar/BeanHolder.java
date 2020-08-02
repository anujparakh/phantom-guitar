package com.example.anuj.phantomguitar;

import android.app.Application;

import nl.littlerobots.bean.Bean;

/**
 * Created by Anuj on 16/08/15.
 */
public class BeanHolder extends Application
{
    private Bean bean;
    public void setBean (Bean toSet)
    {
        bean = toSet;
    }

    public Bean getBean ()
    {
        return bean;
    }
}

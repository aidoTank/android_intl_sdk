package com.ycgame.t11.gp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.intl.IntlActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLogin(MainActivity.this);
    }
    private void startLogin(Activity activity) {
        Intent i = new Intent(activity, IntlActivity.class);
        activity.startActivity(i);
    }
}

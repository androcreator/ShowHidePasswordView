package com.showhideview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.showhidepasswordview.ShowHidePasswordView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViewByID();

    }

    public  void getViewByID(){
        EditText showHidePasswordView = (EditText) findViewById(R.id.showHidePasswordET);
        showHidePasswordView.setText("vijay");
    }

}

package edu.uwyo.toddt.tipcalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ViewSwitcher;

public class TipCalcActivity extends AppCompatActivity implements tipFragment.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calc);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new tipFragment())
                .commit();
    }


    @Override
    public void onFragmentInteraction(String name){
        Log.d("TipCalcActivity", "Logged Name: " + name);
    }
}

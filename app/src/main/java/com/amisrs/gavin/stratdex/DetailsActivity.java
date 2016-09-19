package com.amisrs.gavin.stratdex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    private TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(null);

        setContentView(R.layout.activity_details);
        nameTextView = (TextView)findViewById(R.id.tv_name);
        Intent intent = getIntent();
        String id = intent.getStringExtra(PokemonSpeciesAdapter.LIST_KEY);
        nameTextView.setText(id);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.lefttoright, R.anim.lefttoright);

    }
}

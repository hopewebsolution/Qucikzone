package com.quikzon.add.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.quikzon.add.MainActivity;
import com.quikzon.add.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Searchactivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.Ivback)
    ImageView Ivback;
    @BindView(R.id.Ivvoice)
    ImageView Ivvoice;
    @BindView(R.id.Tvsearch)
    EditText Tvsearch;
    private static final int REQUEST_CODE = 1234;
    String Query="";
    private SearchView.OnQueryTextListener queryTextListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchactivity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Ivback.setOnClickListener(this);
        Ivvoice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.Ivback:
                    onBackPressed();
                    break;
                case R.id.Ivvoice:
                    voiceSearch();
                    break;
            }
    }
    public void voiceSearch(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something...");
        startActivityForResult(intent, REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
// Populate the wordsList with the String values the recognition engine thought it heard
            final ArrayList< String > matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (!matches.isEmpty())
            {
                Query = matches.get(0);
                Log.d("stirng",Query);
                Tvsearch.setText(Query);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

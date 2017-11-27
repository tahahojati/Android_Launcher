package com.example.professortaha.nerdlauncher;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ProfessorTaha on 11/26/2017.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    abstract public Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentById(R.id.main_fragment) == null){
            fm.beginTransaction()
                    .add(R.id.main_fragment, createFragment())
                    .commit();
        }

    }
}

package com.itpro.buildersbackyard.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.itpro.buildersbackyard.R;


public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    public void addFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.main, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    public void addFragmentwithactivity(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .add(R.id.main, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    public void replaceFragment(Fragment fragment, Fragment fragment1) {
        getFragmentManager().beginTransaction()
                .replace(R.id.main, fragment, fragment.getClass().getSimpleName()).addToBackStack(fragment1.getClass().getName())
                .commit();
    }

    public void replaceFragmentHire(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.main, fragment, fragment.getClass().getSimpleName())
                .commit();
    }


    public void addFragmentWithBackStack(Fragment fragment, Fragment launcherFragment) {
        getFragmentManager().beginTransaction()
                .add(R.id.main, fragment, fragment.getClass().getSimpleName())
                .hide(getFragmentManager().findFragmentByTag(launcherFragment.getClass().getSimpleName()))
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }
    public void removeFragmentWithBackStack(Fragment fragment, Fragment launcherFragment) {
        getFragmentManager().beginTransaction()
                .remove(launcherFragment).commit();

    }
    public void addFragmentWithBackStackWithoutHide(Fragment fragment, Fragment
            launcherFragment) {
        getFragmentManager().beginTransaction()
                .add(android.R.id.content, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    public void addFragmentWithBackStackWithAnimationWithoutHide(Fragment fragment, Fragment
            launcherFragment) {
        getFragmentManager().beginTransaction()
                //.setCustomAnimations(R.anim.slide_up_anim, 0)
                .add(android.R.id.content, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    public void addFragmentWithHide(Fragment fragment, Fragment launcherFragment) {
        getFragmentManager().beginTransaction()
                .add(android.R.id.content, fragment, fragment.getClass().getSimpleName())
                .hide(getFragmentManager().findFragmentByTag(launcherFragment.getClass().getSimpleName()))
                .commit();
    }


}

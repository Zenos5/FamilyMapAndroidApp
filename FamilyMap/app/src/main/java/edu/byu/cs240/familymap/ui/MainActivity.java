package edu.byu.cs240.familymap.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;

import edu.byu.cs240.familymap.R;
import edu.byu.cs240.familymap.model.DataCache;
import model.Event;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

/**
 * Main activity which holds login and map fragments
 */
public class MainActivity extends AppCompatActivity {
    private static final String LOGGED_IN = "LogInIntent";
    private static final String EVENT_ID = "EventID";

    private LoginFragment loginFragment;
    private MapFragment mapFragment;

    /**
     * Creates login fragment
     *
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("In Start: start");
        Iconify.with(new FontAwesomeModule());
        FragmentManager fm = this.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        DataCache.getInstance();
        if (fragment == null) {
            if (!DataCache.isLoggedIn()) {
                invalidateOptionsMenu();
                System.out.println("In Start: null");
                loginFragment = LoginFragment.newInstance();
                Bundle args = new Bundle();
                loginFragment.setArguments(args);
                fm.beginTransaction()
                        .add(R.id.fragmentContainer, loginFragment)
                        .commit();
            } else {
                System.out.println("In Start: null");
                mapFragment = MapFragment.newInstance();
                Bundle args = new Bundle();
                mapFragment.setArguments(args);
                fm.beginTransaction()
                        .replace(R.id.fragmentContainer, mapFragment)
                        .commit();
            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            FragmentManager fm = this.getSupportFragmentManager();
            fragment = fm.findFragmentById(R.id.fragmentContainer);
            System.out.println("In Resume: start");
            if (!DataCache.isLoggedIn()) {
                invalidateOptionsMenu();
                System.out.println("In Resume: null");
                loginFragment = LoginFragment.newInstance();
                Bundle args = new Bundle();
                loginFragment.setArguments(args);
                fm.beginTransaction()
                        .add(R.id.fragmentContainer, loginFragment)
                        .commit();
            } else {
                System.out.println("In Resume: null");
                mapFragment = MapFragment.newInstance();
                Bundle args = new Bundle();
                mapFragment.setArguments(args);
                fm.beginTransaction()
                        .add(R.id.fragmentContainer, mapFragment)
                        .commit();
            }
        }
        else {
            System.out.println("In Resume: start");
            FragmentManager fm = this.getSupportFragmentManager();
            fm.beginTransaction().remove(fragment).commit();
            fragment = fm.findFragmentById(R.id.fragmentContainer);
            if (!DataCache.isLoggedIn()) {
                invalidateOptionsMenu();
                System.out.println("In Resume: not null");
                loginFragment = LoginFragment.newInstance();
                Bundle args = new Bundle();
                loginFragment.setArguments(args);
                fm.beginTransaction()
                        .replace(R.id.fragmentContainer, loginFragment)
                        .commit();
            } else {
                System.out.println("In Resume: not null");
                mapFragment = MapFragment.newInstance();
                Bundle args = new Bundle();
                mapFragment.setArguments(args);
                fm.beginTransaction()
                        .replace(R.id.fragmentContainer, mapFragment)
                        .commit();
            }
        }
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == "") {


        }
    }
    */
}
package edu.byu.cs240.familymap.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import edu.byu.cs240.familymap.R;

/**
 * Event activity which holds event fragments
 * and has an arrow to return to the main activity
 */
public class EventActivity extends AppCompatActivity {
    private static String EVENT_ID = "eventID";
    private MapFragment eventFragment;

    /**
     * Creates event fragment and back arrow
     *
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Iconify.with(new FontAwesomeModule());
        FragmentManager fm = this.getSupportFragmentManager();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.eventContainer);
        if (fragment == null) {
            System.out.println("OnCreate: " + getIntent().getExtras().getString(EVENT_ID));
            eventFragment = MapFragment.newInstance(getIntent().getExtras().getString(EVENT_ID));
            Bundle args = new Bundle();
            args.putString(EVENT_ID, getIntent().getExtras().getString(EVENT_ID));
            eventFragment.setArguments(args);
            fm.beginTransaction()
                    .add(R.id.eventContainer, eventFragment)
                    .commit();
        } else {
            System.out.println("OnCreate: " + getIntent().getExtras().getString(EVENT_ID));
            eventFragment = MapFragment.newInstance(getIntent().getExtras().getString(EVENT_ID));
            Bundle args = new Bundle();
            args.putString(EVENT_ID, getIntent().getExtras().getString(EVENT_ID));
            eventFragment.setArguments(args);
            fm.beginTransaction()
                    .replace(R.id.eventContainer, eventFragment)
                    .commit();
        }
    }

    /**
     * Creates event fragment and back arrow
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_event);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Iconify.with(new FontAwesomeModule());
        FragmentManager fm = this.getSupportFragmentManager();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.eventContainer);
        if (fragment == null) {
            System.out.println("OnResume: " + getIntent().getExtras().getString(EVENT_ID));
            eventFragment = MapFragment.newInstance(getIntent().getExtras().getString(EVENT_ID));
            Bundle args = new Bundle();
            args.putString(EVENT_ID, getIntent().getExtras().getString(EVENT_ID));
            eventFragment.setArguments(args);
            fm.beginTransaction()
                    .add(R.id.eventContainer, eventFragment)
                    .commit();
        } else {
            System.out.println("OnResume: " + getIntent().getExtras().getString(EVENT_ID));
            eventFragment = MapFragment.newInstance(getIntent().getExtras().getString(EVENT_ID));
            Bundle args = new Bundle();
            args.putString(EVENT_ID, getIntent().getExtras().getString(EVENT_ID));
            eventFragment.setArguments(args);
            fm.beginTransaction()
                    .replace(R.id.eventContainer, eventFragment)
                    .commit();
        }

    }

    /**
     * If menu button is pressed, goes back to the main activity
     *
     * @param item item on menu bar
     * @return if item has been processed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
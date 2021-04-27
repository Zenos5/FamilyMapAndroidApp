package edu.byu.cs240.familymap.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import edu.byu.cs240.familymap.R;

/**
 * Settings activity which holds setting fragments
 * and has an arrow to return to the main activity
 */
public class SettingsActivity extends AppCompatActivity {
    private SettingsFragment settingsFragment;

    /**
     * Creates setting fragment and back arrow
     *
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentManager fm = this.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.settingsContainer);
        if (fragment == null) {
            settingsFragment = SettingsFragment.newInstance();
            Bundle args = new Bundle();
            settingsFragment.setArguments(args);
            fm.beginTransaction()
                    .add(R.id.settingsContainer, settingsFragment)
                    .commit();
        } else {
            settingsFragment = SettingsFragment.newInstance();
            Bundle args = new Bundle();
            settingsFragment.setArguments(args);
            fm.beginTransaction()
                    .replace(R.id.settingsContainer, settingsFragment)
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
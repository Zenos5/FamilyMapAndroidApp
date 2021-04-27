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
 * Search activity which holds search fragments
 * and has an arrow to return to the main activity
 */
public class SearchActivity extends AppCompatActivity {
    private SearchFragment searchFragment;

    /**
     * Creates search fragment and back arrow
     *
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Iconify.with(new FontAwesomeModule());
        FragmentManager fm = this.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.searchContainer);
        if (fragment == null) {
            searchFragment = SearchFragment.newInstance();
            Bundle args = new Bundle();
            searchFragment.setArguments(args);
            fm.beginTransaction()
                    .add(R.id.searchContainer, searchFragment)
                    .commit();
        } else {
            searchFragment = SearchFragment.newInstance();
            Bundle args = new Bundle();
            searchFragment.setArguments(args);
            fm.beginTransaction()
                    .replace(R.id.searchContainer, searchFragment)
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
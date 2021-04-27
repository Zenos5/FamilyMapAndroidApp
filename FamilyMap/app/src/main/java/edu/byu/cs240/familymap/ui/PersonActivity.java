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
 * Person activity which holds person fragments
 * and has an arrow to return to the main activity
 */
public class PersonActivity extends AppCompatActivity {
    private static String PERSON_ID = "personID";
    private PersonFragment personFragment;

    /**
     * Creates person fragment and back arrow
     *
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Iconify.with(new FontAwesomeModule());
        FragmentManager fm = this.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.personContainer);
        if (fragment == null) {
            personFragment = PersonFragment.newInstance(getIntent().getExtras().getString(PERSON_ID));
            Bundle args = new Bundle();
            args.putString(PERSON_ID, getIntent().getExtras().getString(PERSON_ID));
            personFragment.setArguments(args);
            fm.beginTransaction()
                    .add(R.id.personContainer, personFragment)
                    .commit();
        } else {
            personFragment = PersonFragment.newInstance(getIntent().getExtras().getString(PERSON_ID));
            Bundle args = new Bundle();
            args.putString(PERSON_ID, getIntent().getExtras().getString(PERSON_ID));
            personFragment.setArguments(args);
            fm.beginTransaction()
                    .replace(R.id.personContainer, personFragment)
                    .commit();
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
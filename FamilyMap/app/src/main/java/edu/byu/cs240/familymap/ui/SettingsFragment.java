package edu.byu.cs240.familymap.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import edu.byu.cs240.familymap.R;
import edu.byu.cs240.familymap.model.DataCache;
import edu.byu.cs240.familymap.model.Settings;

/**
 * Settings fragment for the settings activity
 */
public class SettingsFragment extends Fragment {
    private SwitchCompat lifeStoryLinesSwitch;
    private SwitchCompat familyTreeLinesSwitch;
    private SwitchCompat spouseLinesSwitch;
    private SwitchCompat fathersSideSwitch;
    private SwitchCompat mothersSideSwitch;
    private SwitchCompat maleEventsSwitch;
    private SwitchCompat femaleEventsSwitch;
    private LinearLayout logoutButton;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Creates new instance of settings fragment
     *
     * @return settings fragment
     */
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates a new settings fragment
     *
     * @param savedInstanceState bundle holding arguments
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates widgets and listeners for settings fragment
     *
     * @param inflater inflater for views
     * @param container view context for views
     * @param savedInstanceState bundle holding arguments
     * @return overall view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        lifeStoryLinesSwitch = (SwitchCompat) v.findViewById(R.id.lifeStoryLineSwitch);
        lifeStoryLinesSwitch.setChecked(DataCache.getSettings().isLifeStoryLines());
        lifeStoryLinesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //if 'isChecked' is true do whatever you need...
                Settings settings = DataCache.getSettings();
                settings.setLifeStoryLines(true);
                DataCache.setSettings(settings);
            }
            else {
                Settings settings = DataCache.getSettings();
                settings.setLifeStoryLines(false);
                DataCache.setSettings(settings);
            }
        });

        familyTreeLinesSwitch = (SwitchCompat) v.findViewById(R.id.familyTreeLineSwitch);
        familyTreeLinesSwitch.setChecked(DataCache.getSettings().isFamilyTreeLines());
        familyTreeLinesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //if 'isChecked' is true do whatever you need...
                Settings settings = DataCache.getSettings();
                settings.setFamilyTreeLines(true);
                DataCache.setSettings(settings);
            }
            else {
                Settings settings = DataCache.getSettings();
                settings.setFamilyTreeLines(false);
                DataCache.setSettings(settings);
            }
        });

        spouseLinesSwitch = (SwitchCompat) v.findViewById(R.id.spouseLineSwitch);
        spouseLinesSwitch.setChecked(DataCache.getSettings().isSpouseLines());
        spouseLinesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //if 'isChecked' is true do whatever you need...
                Settings settings = DataCache.getSettings();
                settings.setSpouseLines(true);
                DataCache.setSettings(settings);
            }
            else {
                Settings settings = DataCache.getSettings();
                settings.setSpouseLines(false);
                DataCache.setSettings(settings);
            }
        });

        fathersSideSwitch = (SwitchCompat) v.findViewById(R.id.fatherSideSwitch);
        fathersSideSwitch.setChecked(DataCache.getSettings().isFatherSide());
        fathersSideSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //if 'isChecked' is true do whatever you need...
                Settings settings = DataCache.getSettings();
                settings.setFatherSide(true);
                DataCache.setSettings(settings);
                DataCache.endSync();
            }
            else {
                Settings settings = DataCache.getSettings();
                settings.setFatherSide(false);
                DataCache.setSettings(settings);
                DataCache.endSync();
            }
        });

        mothersSideSwitch = (SwitchCompat) v.findViewById(R.id.motherSideSwitch);
        mothersSideSwitch.setChecked(DataCache.getSettings().isMotherSide());
        mothersSideSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //if 'isChecked' is true do whatever you need...
                Settings settings = DataCache.getSettings();
                settings.setMotherSide(true);
                DataCache.setSettings(settings);
                DataCache.endSync();
            }
            else {
                Settings settings = DataCache.getSettings();
                settings.setMotherSide(false);
                DataCache.setSettings(settings);
                DataCache.endSync();
            }
        });

        maleEventsSwitch = (SwitchCompat) v.findViewById(R.id.maleEventSwitch);
        maleEventsSwitch.setChecked(DataCache.getSettings().isMaleEvents());
        maleEventsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //if 'isChecked' is true do whatever you need...
                Settings settings = DataCache.getSettings();
                settings.setMaleEvents(true);
                DataCache.setSettings(settings);
                DataCache.endSync();
            }
            else {
                Settings settings = DataCache.getSettings();
                settings.setMaleEvents(false);
                DataCache.setSettings(settings);
                DataCache.endSync();
            }
        });

        femaleEventsSwitch = (SwitchCompat) v.findViewById(R.id.femaleEventSwitch);
        femaleEventsSwitch.setChecked(DataCache.getSettings().isFemaleEvents());
        femaleEventsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //if 'isChecked' is true do whatever you need...
                Settings settings = DataCache.getSettings();
                settings.setFemaleEvents(true);
                DataCache.setSettings(settings);
                DataCache.endSync();
            }
            else {
                Settings settings = DataCache.getSettings();
                settings.setFemaleEvents(false);
                DataCache.setSettings(settings);
                DataCache.endSync();
            }
        });

        logoutButton = (LinearLayout) v.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DataCache.clear();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        return v;
    }
}
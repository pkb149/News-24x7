package com.pkb149.news24x7;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by CoderGuru on 08-07-2017.
 */

public class SettingsFragment extends PreferenceFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addPreferencesFromResource(R.xml.pref_visualizer);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

}

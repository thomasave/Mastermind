package thomasave.mastermind;

import android.graphics.Point;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.text.InputType;
import android.view.Display;

public class settings extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsactivity);
        addPreferencesFromResource(R.xml.preferences);
        final ListPreference columns = (ListPreference) findPreference("columns");
        final ListPreference background = (ListPreference) findPreference("backgroundpreference");
        final EditTextPreference editTextPreference = (EditTextPreference) findPreference("rows");
        editTextPreference.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                MainActivity.m_size_changed = true;
                return true;
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int maxcolumns = Math.max(Math.round(Math.min(size.x,size.y) / 154) - 2, 2);

        String[] entries = new String[maxcolumns];
        for(int i=0;i< maxcolumns;i++) entries[i] = Integer.toString(i+3);
        columns.setEntries(entries);
        columns.setDefaultValue(entries[0]);
        columns.setEntryValues(entries);
        columns.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                MainActivity.m_size_changed = true;
                return true;
            }
        });
        background.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                MainActivity.m_background_changed = true;
                return true;
            }
        });
    }
}

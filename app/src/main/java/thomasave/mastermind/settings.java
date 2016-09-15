package thomasave.mastermind;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class settings extends PreferenceActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    Object defaultvalue = null;
    Tracker tracker;
    boolean m_changed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsactivity);
        addPreferencesFromResource(R.xml.preferences);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();
        tracker.setScreenName("Settings");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        final SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
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
        if (sharedpreferences.getString("adpreference","Full").equals("None") ) {
            Log.v("adpreference", "destroy");
            View adfragment = findViewById(R.id.ad_fragmentsettings);
            adfragment.setVisibility(View.INVISIBLE);
        } else {
            Log.v("adpreference", "Not destroy"+ sharedpreferences.getString("adpreference", "Full"));
            AdView adView = (AdView) findViewById(R.id.adView);
            AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
            adView.loadAd(adRequestBuilder.build());
        }

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
        ListPreference ads = (ListPreference) findPreference("adpreference");
        defaultvalue = sharedpreferences.getString("adpreference", "Full");
        ads.setDefaultValue(defaultvalue);
        ads.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                MainActivity.m_ads_changed = true;
                String status = sharedpreferences.getString("adpreference", "Full");
                SharedPreferences.Editor mEdit1 = sharedpreferences.edit();
                mEdit1.putString("adpreference", (String) o);
                mEdit1.apply();
                String statusafter = sharedpreferences.getString("adpreference", "Full");
                if (statusafter.equals("None") && !(status.equals("None"))) {
                    View adfragment = findViewById(R.id.ad_fragmentsettings);
                    adfragment.setVisibility(View.INVISIBLE);
                }
                if (!(statusafter.equals("None")) && status.equals("None")) {
                    AdView adView = (AdView) findViewById(R.id.adView);
                    View adfragment = findViewById(R.id.ad_fragmentsettings);
                    adfragment.setVisibility(View.VISIBLE);
                    AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
                    adView.loadAd(adRequestBuilder.build());
                }
                Log.v("adpreference", statusafter);
                return true;
            }
        });
    }
}

package thomasave.mastermind;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class settings extends PreferenceActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    Object defaultvalue = null;
    Tracker tracker;

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
        EditTextPreference editTextPreference = (EditTextPreference) findPreference("rows");
        editTextPreference.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
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
        ListPreference ads = (ListPreference) findPreference("adpreference");
        defaultvalue = sharedpreferences.getString("adpreference", "Full");
        ads.setDefaultValue(defaultvalue);
        ads.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String status = sharedpreferences.getString("adpreference", "Full");
                SharedPreferences.Editor mEdit1 = sharedpreferences.edit();
                mEdit1.putString("adpreference", (String) o);
                mEdit1.commit();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}

package thomasave.mastermind;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class about extends Activity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    Tracker tracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();
        tracker.setScreenName("About");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.getString("adpreference","Full").equals("None") ) {
            Log.v("adpreference", "destroy");
            View adfragment = findViewById(R.id.ad_fragment);
            adfragment.setVisibility(View.INVISIBLE);
        } else {
            Log.v("adpreference", "Not destroy"+ sharedpreferences.getString("adpreference", "Full"));
            AdView adView = (AdView) findViewById(R.id.adView);
            AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
            adView.loadAd(adRequestBuilder.build());
        }
    }
}

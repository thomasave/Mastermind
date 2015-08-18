package thomasave.mastermind;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Random;

public class MainActivity extends Activity {

    int rowamount;
    int a = 1;
    int[] status;
    String adstatus;
    int round = 1;
    int answer1, answer2, answer3, answer4;
    Button check;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    public static Tracker tracker;
    int b = 0;
    int wincount = 0;
    int lostcount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        rowamount = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("rows", "7"));
        setContentView(R.layout.activity_main);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();
        tracker.setScreenName("Mainactivity");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        check =  (Button) findViewById(R.id.buttontest);
        if (sharedpreferences.getString("adpreference","Full").equals("Full") ) {
            final AdView mAdView = (AdView) this.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            mAdView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                                                        @Override
                                                                        public void onGlobalLayout() {
                                                                            if (b == 0) {
                                                                                RelativeLayout relativelayout = (RelativeLayout) findViewById(R.id.relativetest);
                                                                                relativelayout.setPadding(0, 0, 0, mAdView.getHeight());
                                                                                b = 1;
                                                                            }
                                                                        }
                                                                    }
            );
            adstatus = "Full";
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Ads")
                    .setAction("Full")
                    .build());
        } else {
            Log.v("adpreference", "destroy");
            View adfragment = findViewById(R.id.ad_fragment);
            adfragment.setVisibility(View.INVISIBLE);
            adstatus = sharedpreferences.getString("adpreference","None");
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Ads")
                    .setAction(sharedpreferences.getString("adpreference","None"))
                    .build());
        }

        Setanswers();
        check.setHeight(getheight());
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textviewblack = (TextView) findViewById(round + 6000);
                TextView textviewwhite = (TextView) findViewById(round + 5000);
                textviewblack.setText("");
                textviewwhite.setText("");
                checkanswers(status[(round) + 1000], status[(round) + 2000], status[(round) + 3000], status[(round) + 4000], textviewblack, textviewwhite, round + 1 + 1000);
            }
        });
        status = new int[(rowamount+1)*1000000];
        setbuttons();
        setrounds();
    }

    public void setrounds() {
        if (round <= rowamount) {
            for(int i = 1; i <= 4; i++) {
                Drawable d = getResources().getDrawable(R.drawable.round_button_black);
                final Button button = (Button) findViewById(round+i*1000);
                status[button.getId()] = 0;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        a = button.getId();
                        Log.v("a", "" + a);
                        setnextcolor(getnextcolor(a), a);

                    }
                });
                button.setBackgroundDrawable(d);
                Log.v("i",""+i);
                if(round > 1) {
                    Button previousround = (Button) findViewById((round - 1) + i * 1000);
                    previousround.setOnClickListener(null);
                }
            }
        }
    }

    public void Setanswers() {
        Random r = new Random();
        answer1 = r.nextInt(6);
        answer2 = r.nextInt(6);
        answer3 = r.nextInt(6);
        answer4 = r.nextInt(6);
    }

    public void checkanswers(int status1, int status2, int status3, int status4, TextView scoreblack, TextView scorewhite, int align) {

        int almost4 = 0;
        int almost3 = 0;
        int almost2 = 0;
        int almost1 = 0;
        int amountalmost = 0;
        int amountcorrect = 0;
        int statusused1 = 0;
        int statusused2 = 0;
        int statusused3 = 0;
        int statusused4 = 0;


        if (answer1 == status1) {
            amountcorrect++;
            statusused1 = 5;
            almost1 = 5;
        }
        if (answer2 == status2) {
            amountcorrect++;
            almost2 = 5;
            statusused2 = 5;
        }
        if (answer3 == status3) {
            amountcorrect++;
            almost3 = 5;
            statusused3 = 5;
        }
        if (answer4 == status4) {
            amountcorrect++;
            almost4 = 5;
            statusused4 = 5;
        }

        if (status1 == answer2 && almost2 != 5 && statusused1 != 5){
            amountalmost++;
            almost2 = 5;
            statusused1 = 5;
        }
        if (status1 == answer3 && almost3 != 5 && statusused1 != 5){
            amountalmost++;
            almost3 = 5;
            statusused1 = 5;
        }
        if (status1 == answer4 && almost4 != 5 && statusused1 != 5){
            amountalmost++;
            almost4 = 5;
            statusused1 = 5;
        }

        if (status2 == answer1 && almost1 != 5 && statusused2 != 5){
            amountalmost++;
            almost1 = 5;
            statusused2 = 5;
        }
        if (status2 == answer3 && almost3 != 5 &&statusused2 != 5){
            amountalmost++;
            almost3 = 5;
            statusused2 = 5;
        }
        if (status2 == answer4 && almost4 != 5 &&statusused2 != 5){
            amountalmost++;
            almost4 = 5;
            statusused2 = 5;
        }

        if (status3 == answer2 && almost2 != 5 &&statusused3 != 5){
            amountalmost++;
            almost2 = 5;
            statusused3 = 5;
        }
        if (status3 == answer1 && almost1 != 5 &&statusused3 != 5){
            amountalmost++;
            almost1 = 5;
            statusused3 = 5;
        }
        if (status3 == answer4 && almost4 != 5 &&statusused3 != 5){
            amountalmost++;
            almost4 = 5;
            statusused3 = 5;
        }

        if (status4 == answer2 && almost2 != 5 &&statusused4 != 5){
            amountalmost++;
            almost2 = 5;
            statusused4 = 5;
        }
        if (status4 == answer3 && almost3 != 5 &&statusused4 != 5){
            amountalmost++;
            almost3 = 5;
            statusused4 = 5;
        }
        if (status4 == answer1 && almost1 != 5 &&statusused4 != 5){
            amountalmost++;
            almost1 = 5;
            statusused4 = 5;
        }

        if (amountcorrect == 4) {
            winner();
        } else if((round) == rowamount) {
            lostcount = sharedpreferences.getInt("lostcount",0);
            SharedPreferences.Editor mEdit1 = sharedpreferences.edit();
            mEdit1.putInt("lostcount", lostcount + 1);
            mEdit1.commit();
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Result")
                    .setAction("Lost")
                    .build());
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Oops");
            alert.setCancelable(false);
            alert.setMessage("You lost... Better luck next time.");
            alert.setButton("Restart", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    restartgame();
                }
            });
            alert.show();
        } else {
            if (amountcorrect + amountalmost > 4) {
                AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                        .create();
                alert.setTitle("Oopsie...");
                alert.setCancelable(false);
                alert.setMessage("Something went wrong with the calculations...");
                alert.setButton("Restart", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        restartgame();
                    }
                });
                alert.show();
            } else {
                scoreblack.setVisibility(View.VISIBLE);
                scoreblack.setTextColor(Color.WHITE);
                scoreblack.setTextSize(25);
                scorewhite.setVisibility(View.VISIBLE);
                scorewhite.setTextSize(25);
                scoreblack.setText("" + amountcorrect);
                scorewhite.setText("" + amountalmost);
                round++;
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(check.getMeasuredWidth(), RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp.addRule(RelativeLayout.ALIGN_BOTTOM, align);
                check.setLayoutParams(lp);
                setrounds();


            }
        }
    }
    public void winner() {
        wincount = sharedpreferences.getInt("woncount",0);
        Log.v("Wincount: ",wincount+"");
        SharedPreferences.Editor mEdit1 = sharedpreferences.edit();
        mEdit1.putInt("woncount", wincount + 1);
        mEdit1.commit();
        AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                .create();
        alert.setTitle("We have a winner");
        alert.setMessage("Congratulations, you won!");
        alert.setCancelable(false);
        alert.setButton("Restart", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // closing the application
                restartgame();
            }
        });
        alert.show();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Result")
                .setAction("Win")
                .build());
    }
    public void restartgame() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        System.exit(0);
    }

    public int getnextcolor(int id) {
        int currstatus = status[id];

        if (currstatus < 6) {
            currstatus++;
        }
        if (currstatus >= 6) {

            currstatus = 0;
        }
        status[id] = currstatus;
        return currstatus;
    }

    public void setnextcolor(int currstatus, int id) {

        Button button = (Button) findViewById(id);


        if (currstatus == 0) {
            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button_black));
        }
        if (currstatus == 1) {
            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button_blue));
        }
        if (currstatus == 2) {
            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button_green));
        }
        if (currstatus == 3) {
            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button_red));
        }
        if (currstatus == 4) {
            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button_white));
        }
        if (currstatus == 5) {
            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button_yellow));
        }

    }

    public void setbuttons() {
        int a = rowamount;
        final Button[] buttonscolumn1 = new Button[rowamount];
        for (int i = 0; i < rowamount; i++) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativetest);
            buttonscolumn1[i] = new Button(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(getheight(), getheight());
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            if (i == 0) {
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else {
                lp.addRule(RelativeLayout.ALIGN_TOP, (a+1)+1000);
                lp.topMargin = (int) (getheight()*1.135);
            }
            buttonscolumn1[i].setLayoutParams(lp);
            buttonscolumn1[i].setId(a + 1000);
            buttonscolumn1[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("ID", "" + v.getId());
                }
            });
            Drawable d = getResources().getDrawable(R.drawable.round_button_white);
            buttonscolumn1[i].setBackgroundDrawable(d);
            relativeLayout.addView(buttonscolumn1[i]);
            a--;
        }
        a = rowamount;
        final Button[] buttonscolumn2 = new Button[rowamount];
        for (int i = 0; i < rowamount; i++){
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativetest);
            buttonscolumn2[i] = new Button(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(getheight(), getheight());
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            if (i==0){
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else {
                lp.addRule(RelativeLayout.ALIGN_TOP, (a+1)+1000);
                lp.topMargin = (int) (getheight()*1.135);
            }
            lp.rightMargin = (int) (getheight()*1.135);
            buttonscolumn2[i].setPadding(0,10,10,0);
            buttonscolumn2[i].setLayoutParams(lp);
            buttonscolumn2[i].setId(a + 2000);
            buttonscolumn2[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("ID", "" + v.getId());
                }
            });
            Drawable d = getResources().getDrawable(R.drawable.round_button_white);
            buttonscolumn2[i].setBackgroundDrawable(d);
            relativeLayout.addView(buttonscolumn2[i]);
            a = a - 1;
        }
        a = rowamount;
        final Button[] buttonscolumn3 = new Button[rowamount];
        for (int i = 0; i < rowamount; i++){
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativetest);
            buttonscolumn3[i] = new Button(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(getheight(), getheight());
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            if (i==0){
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else {
                lp.addRule(RelativeLayout.ALIGN_TOP, (a+1)+1000);
                lp.topMargin = (int) (getheight()*1.135);
            }
            lp.rightMargin = (int) (getheight()*1.135)*2;
            buttonscolumn3[i].setPadding(0,10,10,0);
            buttonscolumn3[i].setLayoutParams(lp);
            buttonscolumn3[i].setId(a + 3000);
            buttonscolumn3[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("ID", "" + v.getId());
                }
            });
            Drawable d = getResources().getDrawable(R.drawable.round_button_white);
            buttonscolumn3[i].setBackgroundDrawable(d);
            relativeLayout.addView(buttonscolumn3[i]);
            a = a-1;
        }
        a = rowamount;
        final Button[] buttonscolumn4= new Button[rowamount];
        for (int i = 0; i < rowamount; i++){
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativetest);
            buttonscolumn4[i] = new Button(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(getheight(), getheight());
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            if (i==0){
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else {
                lp.addRule(RelativeLayout.ALIGN_TOP, (a+1)+1000);
                lp.topMargin = (int) (getheight()*1.135);
            }
            lp.rightMargin = (int) (getheight()*1.135)*3;
            buttonscolumn4[i].setPadding(0, 10, 10, 0);
            buttonscolumn4[i].setLayoutParams(lp);
            buttonscolumn4[i].setId(a + 4000);
            buttonscolumn4[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("ID", "" + v.getId());
                }
            });
            Drawable d = getResources().getDrawable(R.drawable.round_button_white);
            buttonscolumn4[i].setBackgroundDrawable(d);
            relativeLayout.addView(buttonscolumn4[i]);
            a = a - 1;
        }
        a = rowamount;
        final TextView[] buttonscolumn6= new TextView[rowamount];
        for (int i = 0; i < rowamount; i++){
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativetest);
            buttonscolumn6[i] = new Button(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(getheight()-10, getheight());
            if (i==0){
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else {
                lp.addRule(RelativeLayout.ALIGN_TOP, (a+1)+1000);
                lp.topMargin = (int) (getheight()*1.135);
            }
            lp.addRule(RelativeLayout.ALIGN_RIGHT,R.id.buttontest);
            buttonscolumn6[i].setPadding(0,0,0,5);
            buttonscolumn6[i].setLayoutParams(lp);
            buttonscolumn6[i].setId(a + 5000);
            buttonscolumn6[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("ID", "" + v.getId());
                }
            });
            Drawable d = getResources().getDrawable(R.drawable.round_button_scorewhite);
            buttonscolumn6[i].setBackgroundDrawable(d);
            buttonscolumn6[i].setVisibility(View.INVISIBLE);
            relativeLayout.addView(buttonscolumn6[i]);
            a = a - 1;
        }
        a = rowamount;
        final TextView[] buttonscolumn5= new TextView[rowamount];
        for (int i = 0; i < rowamount; i++){
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativetest);
            buttonscolumn5[i] = new Button(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(getheight()-10, getheight());
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            if (i==0){
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else {
                lp.addRule(RelativeLayout.ALIGN_TOP, (a+1)+1000);
                lp.topMargin = (int) (getheight()*1.135);
            }
            lp.addRule(RelativeLayout.ALIGN_LEFT, R.id.buttontest);
            buttonscolumn5[i].setPadding(0, 0, 0, 5);
            buttonscolumn5[i].setLayoutParams(lp);
            buttonscolumn5[i].setId(a + 6000);
            buttonscolumn5[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("ID", "" + v.getId());
                }
            });
            Drawable d = getResources().getDrawable(R.drawable.round_button_scoreblack);
            buttonscolumn5[i].setBackgroundDrawable(d);
            buttonscolumn5[i].setVisibility(View.INVISIBLE);
            relativeLayout.addView(buttonscolumn5[i]);
            a = a - 1;
        }
        RelativeLayout.LayoutParams checkparams = new RelativeLayout.LayoutParams(getheight()*2,getheight());
        checkparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        checkparams.addRule(RelativeLayout.ALIGN_BOTTOM, (round) + 1000);
        check.setLayoutParams(checkparams);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public int getheight() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        double height =  width/7.5;
        return (int) height;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent intentabout = new Intent(this, about.class);
            startActivity(intentabout);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent intentsettings = new Intent(this, settings.class);
            startActivity(intentsettings);
            finish();
            System.exit(0);
            return true;
        }
        if (id == R.id.restart) {
            restartgame();
            return true;
        }
        if (id == R.id.stats) {
            Intent intentsettings = new Intent(this, Stats.class);
            startActivity(intentsettings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        tracker.setScreenName("Mainactivity");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
    }
}

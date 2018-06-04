package thomasave.mastermind;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Stats extends Activity {

    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        final SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final TextView woncount = (TextView) findViewById(R.id.gameswoncount);
        final TextView lostcount = (TextView) findViewById(R.id.gameslostcount);
        final TextView winratio = (TextView) findViewById(R.id.winratiocount);
        final TextView totalcount = (TextView) findViewById(R.id.totalgamescount);

        Button reset = (Button) findViewById(R.id.reset);
        final double wins = (double) sharedpreferences.getInt("wincount", 0);
        final double losses = (double) sharedpreferences.getInt("lostcount",0);
        totalcount.setText((int) wins + (int) losses + "");
        woncount.setText((int) wins+"");
        lostcount.setText((int) losses+"");
        double winratiocount = wins/(wins+losses)*100.0;
        winratio.setText((int) winratiocount+"%");
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Stats.this)
                        .setTitle("Reset statistics")
                        .setMessage("Are you sure you want to reset the statistics?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                totalcount.setText(0+"");
                                woncount.setText(0+"");
                                lostcount.setText(0+"");
                                winratio.setText("0%");
                                SharedPreferences.Editor mEdit1 = sharedpreferences.edit();
                                mEdit1.putInt("woncount", 0);
                                mEdit1.putInt("lostcount", 0);
                                mEdit1.apply();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });

    }
}